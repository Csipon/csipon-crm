package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.Organization;
import com.csipon.crm.domain.model.UserRole;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealUser implements User {
    private Long id;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private boolean enable;
    private boolean accountNonLocked;
    private Address address;
    private boolean contactPerson;
    private UserRole userRole;
    private Organization organization;


    public RealUser() {
    }

    public RealUser(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.enable = user.isEnable();
        this.accountNonLocked = user.isAccountNonLocked();
        this.address = user.getAddress();
        this.contactPerson = user.isContactPerson();
        this.userRole = user.getUserRole();
        this.organization = user.getOrganization();
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
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getMiddleName() {
        return middleName;
    }

    @Override
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean isContactPerson() {
        return contactPerson;
    }

    @Override
    public void setContactPerson(boolean contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public UserRole getUserRole() {
        return userRole;
    }

    @Override
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public Organization getOrganization() {
        return organization;
    }

    @Override
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}