package com.csipon.crm.dao;

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
}
