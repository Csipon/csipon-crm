package com.csipon.crm.dao;

import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.Organization;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.request.UserRowRequest;

import java.util.List;

/**
 * Created by bpogo on 4/22/2017.
 */
public interface UserDao extends CrudDao<User> {

    User findByEmail(String email);

    long updatePassword(User user, String password);

    Long getUserRowsCount(UserRowRequest userRowRequest);

    List<User> findUsers(UserRowRequest userRowRequest);

    List<User> findUsersByPattern(String pattern);

    List<User> findOrgUsersByPattern(String pattern, User user);


    AddressDao getAddressDao();
    OrganizationDao getOrganizationDao();

    default Long getAddressId(Address address) {
        if (address == null)
            return null;
        Long addressId = address.getId();
        if (addressId != null) {
            return addressId;
        }
        addressId = getAddressDao().create(address);

        return addressId;
    }

    default Long getOrgId(Organization org) {
        Long orgId = null;
        if (org != null) {
            orgId = org.getId();
            if (orgId != null) {
                return orgId;
            }
            orgId = getOrganizationDao().create(org);
        }
        return orgId;
    }
}
