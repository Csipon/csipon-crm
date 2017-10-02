package com.csipon.crm.domain.proxy;

import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.UserRole;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.Organization;

/**
 * @author Karpunets
 * @since 14.05.2017
 */
public class UserProxy implements User {
    private long id;
    private User user;
    private UserDao userDao;

    public UserProxy(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public void setPassword(String password) {
        getUser().setPassword(password);
    }

    @Override
    public String getFirstName() {
        return getUser().getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        getUser().setFirstName(firstName);
    }

    @Override
    public String getMiddleName() {
        return getUser().getMiddleName();
    }

    @Override
    public void setMiddleName(String middleName) {
        getUser().setMiddleName(middleName);
    }

    @Override
    public String getLastName() {
        return getUser().getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        getUser().setLastName(lastName);
    }

    @Override
    public String getPhone() {
        return getUser().getPhone();
    }

    @Override
    public void setPhone(String phone) {
        getUser().setPhone(phone);
    }

    @Override
    public String getEmail() {
        return getUser().getEmail();
    }

    @Override
    public void setEmail(String email) {
        getUser().setEmail(email);
    }

    @Override
    public boolean isEnable() {
        return getUser().isEnable();
    }

    @Override
    public void setEnable(boolean enable) {
        getUser().setEnable(enable);
    }

    @Override
    public boolean isAccountNonLocked() {
        return getUser().isAccountNonLocked();
    }

    @Override
    public void setAccountNonLocked(boolean accountNonLocked) {
        getUser().setAccountNonLocked(accountNonLocked);
    }

    @Override
    public Address getAddress() {
        return getUser().getAddress();
    }

    @Override
    public void setAddress(Address address) {
        getUser().setAddress(address);
    }

    @Override
    public boolean isContactPerson() {
        return getUser().isContactPerson();
    }

    @Override
    public void setContactPerson(boolean contactPerson) {
        getUser().setContactPerson(contactPerson);
    }

    @Override
    public UserRole getUserRole() {
        return getUser().getUserRole();
    }

    @Override
    public void setUserRole(UserRole userRole) {
        getUser().setUserRole(userRole);
    }

    @Override
    public Organization getOrganization() {
        return getUser().getOrganization();
    }

    @Override
    public void setOrganization(Organization organization) {
        getUser().setOrganization(organization);
    }

    private User getUser() {
        if (user == null) {
            user = userDao.findById(id);
        }
        return user;
    }
}