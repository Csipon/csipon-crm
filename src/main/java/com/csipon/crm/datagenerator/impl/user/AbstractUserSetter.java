package com.csipon.crm.datagenerator.impl.user;

import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.real.RealUser;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.UserRole;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasha on 05.05.2017.
 */
public abstract class AbstractUserSetter extends AbstractSetter<User> {

    private int counter;
    private String password = "$2a$10$mJfq5rmvQR66o1xBN2xMzeptwYaxogOToWzvbVUeEHol.pe/jABia";
    private static final int NAMES_NUMBERS = 1000;
    private List<String> firstNames = new ArrayList<>();
    private List<String> lastNames = new ArrayList<>();
    private List<String> middleNames = new ArrayList<>();

    @Autowired
    private UserDao userDao;
    @Value(value = "classpath:testdata/userNames.json")
    private Resource resource;

    @Override
    public List<User> generate(int numbers) {
        List<User> users = new ArrayList<>();
        try {
            fillNames();
            for (int i = 0; i < numbers; i++) {
                User user = generateObject();
                userDao.create(user);
                users.add(user);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return users;
    }

    private void fillNames() throws IOException, ParseException {
        JSONArray a = (JSONArray) parser.parse(resource.getInputStream());

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String firstName = (String) person.get("firstName");
            String lastName = (String) person.get("lastName");
            String middleName = (String) person.get("midleName");
            firstNames.add(firstName);
            lastNames.add(lastName);
            middleNames.add(middleName);
        }
    }

    @Override
    public User generateObject() {
        User user = new RealUser();
        user.setAccountNonLocked(true);
        user.setEnable(true);
        user.setFirstName(firstNames.get(random.nextInt(NAMES_NUMBERS)));
        user.setMiddleName(middleNames.get(random.nextInt(NAMES_NUMBERS)));
        user.setLastName(lastNames.get(random.nextInt(NAMES_NUMBERS)));
        user.setPhone("0000000000");
        user.setUserRole(getRole());
        setContactPerson(user, counter);
        setOrganization(user);
        setAddress(user);
        user.setEmail(getEmail(counter));
        user.setPassword(password);
        counter++;
        return user;
    }

    protected abstract UserRole getRole();

    protected abstract String getEmail(int counter);

    protected abstract void setOrganization(User user);

    protected abstract void setAddress(User user);

    protected abstract void setContactPerson(User user, int counter);


}
