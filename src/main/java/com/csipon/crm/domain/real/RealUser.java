package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.Organization;
import com.csipon.crm.domain.model.UserRole;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class RealUser implements User {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String phone;
    @Column
    private String email;
    @Column
    private boolean enable;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;
    @JoinColumn(name = "address_id", table = "address", referencedColumnName = "id")
    private Address address;
    @Column(name = "contact_person")
    private boolean contactPerson;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;
    @JoinColumn(name = "organization_id", table = "organization", referencedColumnName = "id")
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