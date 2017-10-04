package com.csipon.crm.security;

import com.csipon.crm.dao.UserAttemptsDao;
import com.csipon.crm.domain.UserAttempts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Pasha on 23.04.2017.
 */
@Component("authenticationProvider")
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {
    //    timeout for user which has been locked
    private static final long TIME_OUT = 300000L;
    private static final long MINUTE_IN_MILLISECONDS = 60000L;

    private final UserAttemptsDao userAttemptsDao;

    @Autowired
    public LimitLoginAuthenticationProvider(UserAttemptsDao userAttemptsDao, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(passwordEncoder);
        this.userAttemptsDao = userAttemptsDao;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAttempts userAttempts = userAttemptsDao.getUserAttempts(authentication.getName());
        try {
            if (userAttempts != null && checkTimeout(userAttempts)) {
                userAttemptsDao.lockUserAccount(authentication.getName(), false);
            }
            Authentication auth = super.authenticate(authentication);
            userAttemptsDao.resetFailAttempts(authentication.getName());
            return auth;
        } catch (BadCredentialsException e) {
            userAttemptsDao.updateFailAttempts(authentication.getName());
            throw e;
        } catch (LockedException e) {
            String error;
            if (userAttempts != null) {
                error = getInformMessage(authentication.getName(), userAttempts.getLastModified());
            } else {
                error = e.getMessage();
            }
            throw new LockedException(error);
        }

    }

    private boolean checkTimeout(UserAttempts userAttempts) {
        return userAttempts.getLastModified() != null &&
                LocalDateTime.now().isAfter(userAttempts.getLastModified().plusMinutes(5));
    }

    private String getInformMessage(String userMail, LocalDateTime lastModified) {
        Date time = Date.from(lastModified.atZone(ZoneId.systemDefault()).toInstant());
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        long timeWait = (TIME_OUT + time.getTime()) - now.getTime();
        long minutes = getMinutes(timeWait);
        long seconds = getSeconds(timeWait);
        StringBuilder builder = new StringBuilder();
        builder.append("User account is locked!<br> Email : ");
        builder.append(userMail);
        builder.append("<br>Please wait : ");
        if (minutes > 0) {
            builder.append(minutes);
            builder.append(" minutes ");
        }
        builder.append(seconds);
        builder.append(" seconds");
        return builder.toString();

    }

    private long getMinutes(long time) {
        return time / MINUTE_IN_MILLISECONDS;
    }

    private long getSeconds(long time) {
        long result = time % MINUTE_IN_MILLISECONDS;
        return result / 1000;
    }
}
