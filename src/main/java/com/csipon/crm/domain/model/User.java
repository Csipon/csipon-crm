package com.csipon.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface User {

    Long getId();

    void setId(Long id);

    String getPassword();

    void setPassword(String password);

    String getFirstName();

    void setFirstName(String firstName);

    String getMiddleName();

    void setMiddleName(String middleName);

    String getLastName();

    void setLastName(String lastName);

    String getPhone();

    void setPhone(String phone);

    String getEmail();

    void setEmail(String email);

    boolean isEnable();

    void setEnable(boolean enable);

    boolean isAccountNonLocked();

    void setAccountNonLocked(boolean accountNonLocked);

    Address getAddress();

    void setAddress(Address address);

    boolean isContactPerson();

    void setContactPerson(boolean contactPerson);

    UserRole getUserRole();

    void setUserRole(UserRole userRole);

    Organization getOrganization();

    void setOrganization(Organization organization);


}
