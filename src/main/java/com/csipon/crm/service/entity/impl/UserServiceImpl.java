package com.csipon.crm.service.entity.impl;

import com.csipon.crm.dao.UserDao;
import com.csipon.crm.dao.UserTokenDao;
import com.csipon.crm.domain.UserToken;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.UserRole;
import com.csipon.crm.domain.real.RealUser;
import com.csipon.crm.domain.request.UserRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.UserDto;
import com.csipon.crm.dto.mapper.ModelMapper;
import com.csipon.crm.dto.mapper.impl.UserMapper;
import com.csipon.crm.dto.row.UserRowDto;
import com.csipon.crm.exception.RegistrationException;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.email.AbstractEmailSender;
import com.csipon.crm.service.email.EmailParam;
import com.csipon.crm.service.email.EmailParamKeys;
import com.csipon.crm.service.email.EmailType;
import com.csipon.crm.service.email.senders.ChangeSender;
import com.csipon.crm.service.entity.UserService;
import com.csipon.crm.service.security.RandomString;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;
import com.timgroup.jgravatar.GravatarRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by bpogo on 4/30/2017.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int PASSWORD_LENGTH = 10;
    private static final String TOKEN_WILD_CARD = "%token%";
    private static final String LOCAL_ACTIVATION_LINK_TEMPLATE = "http://localhost:8888/user/registration/confirm?token=" + TOKEN_WILD_CARD;
    private static final String PRODUCTION_ACTIVATION_LINK_TEMPLATE = "http://nc-project.tk/user/registration/confirm?token=" + TOKEN_WILD_CARD;
    private static final String DEFAULT_AVATAR = "https://ssl.gstatic.com/images/branding/product/1x/avatar_circle_blue_512dp.png";

    @Resource
    private Environment env;

    private final UserDao userDao;
    private final UserTokenDao tokenDao;
    private final AbstractEmailSender emailSender;
    private final AbstractEmailSender changeSender;
    private final PasswordEncoder encoder;
    private final SessionRegistry sessionRegistry;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserTokenDao tokenDao, PasswordEncoder encoder,
                           @Qualifier("registrationSender") AbstractEmailSender emailSender,
                           @Qualifier("changeSender") ChangeSender changeSender,
                           SessionRegistry sessionRegistry, UserMapper userMapper) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.emailSender = emailSender;
        this.changeSender = changeSender;
        this.encoder = encoder;
        this.sessionRegistry = sessionRegistry;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public Long createUser(UserDto userDto) throws RegistrationException {
        RandomString random = new RandomString(PASSWORD_LENGTH);
        String password = random.nextString();
        userDto.setPassword(password);
        encodePassword(userDto);

        User user = ModelMapper.map(userMapper.dtoToModelForCreate(), userDto, RealUser.class);

        userDao.create(user);
        String registrationToken = createUserRegistrationToken(user);
        sendRegistrationEmail(user, password, registrationToken);

        return user.getId();
    }

    @Override
    @Transactional
    public boolean activateUser(String token) {
        UserToken userToken = tokenDao.getUserToken(token);
        if (userToken != null && !userToken.isUsed()) {
            tokenDao.updateToken(token, true);
            User userToActivate = userDao.findById(userToken.getUser().getId());
            userToActivate.setEnable(true);
            userToActivate.setAccountNonLocked(true);
            userDao.update(userToActivate);
            return true;
        }
        return false;
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public User update(UserDto userDto) {
        User user = ModelMapper.map(userMapper.dtoToModel(), userDto, RealUser.class);
        userDao.update(user);
        return user;
    }

    @Override
    public User update(User user) {
        userDao.update(user);
        return user;
    }

    @Override
    public boolean updatePassword(User user, String oldPassword, String newPassword) {
        if (encoder.matches(oldPassword, user.getPassword())) {
            String encodedPassword = encoder.encode(newPassword);
            user.setPassword(encodedPassword);
            if (userDao.updatePassword(user, encodedPassword) != 0) {
                EmailParam emailMap = new EmailParam(EmailType.CHANGE);
                emailMap.put(EmailParamKeys.USER, user);
                emailMap.put(EmailParamKeys.CHANGE_TYPE, "password");
                emailMap.put(EmailParamKeys.CHANGE_VALUE, newPassword);
                try {
                    changeSender.send(emailMap);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUsers(UserRowRequest userRowRequest, User principal, boolean individual) {
        UserRole role = principal.getUserRole();
        if (role.equals(UserRole.ROLE_CUSTOMER) && principal.isContactPerson()) {
            userRowRequest.setCustomerId(principal.getId());
        }
        Map<String, Object> response = new HashMap<>();
        Long length = userDao.getUserRowsCount(userRowRequest);
        response.put("length", length);
        List<User> users = userDao.findUsers(userRowRequest);

        List<UserRowDto> dtoRows = ModelMapper.mapList(userMapper.modelToRowDto(), users, UserRowDto.class);
        response.put("rows", dtoRows);
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AutocompleteDto> getUserLastNamesByPattern(String pattern, User principal) {
        UserRole role = principal.getUserRole();
        List<User> users;
        if (role.equals(UserRole.ROLE_CUSTOMER) && principal.isContactPerson()) {
            users = userDao.findOrgUsersByPattern(pattern, principal);
        } else {
            users = userDao.findUsersByPattern(pattern);
        }
        return ModelMapper.mapList(userMapper.modelLastNameToAutocomplete(), users, AutocompleteDto.class);
    }

    public List<User> getOnlineCsrs() {
        List principals = sessionRegistry.getAllPrincipals();
        List<User> csrList = new ArrayList<>();
        for (Object o : principals) {
            if (o instanceof UserDetailsImpl) {
                User user = (User) o;
                if (user.getUserRole() == UserRole.ROLE_CSR) {
                    csrList.add(user);
                }
            }
        }
        return csrList;
    }

    @Override
    @Transactional
    public String getAvatar(Long id) {
        User user = userDao.findById(id);
        Gravatar gravatar = new Gravatar();
        gravatar.setSize(500);
        gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
        gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
        if (user != null) {
            byte[] byteUrl = gravatar.download(user.getEmail());
            String url = gravatar.getUrl(user.getEmail());
            if (byteUrl != null) {
                return url;
            }
        }
        return DEFAULT_AVATAR;
    }

    private String createUserRegistrationToken(User user) {
        UserToken userToken = new UserToken();
        userToken.setToken(generateToken());
        userToken.setUsed(false);
        userToken.setUser(user);
        userToken.setSendDate(LocalDateTime.now());

        tokenDao.create(userToken);

        return userToken.getToken();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void sendRegistrationEmail(User user, String password, String token) throws RegistrationException {
        EmailParam emailParam = new EmailParam(EmailType.REGISTRATION);

        String activationLink;
        if (env.acceptsProfiles("production")) {
            activationLink = PRODUCTION_ACTIVATION_LINK_TEMPLATE.replaceAll(TOKEN_WILD_CARD, token);
        } else {
            activationLink = LOCAL_ACTIVATION_LINK_TEMPLATE.replaceAll(TOKEN_WILD_CARD, token);
        }
        emailParam.put(EmailParamKeys.USER_REFERENCE, activationLink);
        emailParam.put(EmailParamKeys.USER, user);
        emailParam.put(EmailParamKeys.USER_PASSWORD, password);

        try {
            emailSender.send(emailParam);
            log.info("Registration email was sent to user with id: " + user.getId());
        } catch (MessagingException e) {
            throw new RegistrationException("Registration email wasn't sent.", e);
        }
    }

    private void encodePassword(UserDto userDto) {
        String encodedPassword = encoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
    }
}
