package com.csipon.crm.service.entity;

import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.request.UserRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.UserDto;
import com.csipon.crm.exception.RegistrationException;

import java.util.List;
import java.util.Map;

/**
 * Created by bpogo on 4/30/2017.
 */
public interface UserService {
    Long createUser(UserDto userDto) throws RegistrationException;

    boolean activateUser(String userToken);

    Map<String, Object> getUsers(UserRowRequest userRowRequest, User user, boolean individual);

    User getUserById(Long id);

    User update(UserDto userDto);

    User update(User user);

    boolean updatePassword(User user, String oldPassword, String newPassword);

    List<AutocompleteDto> getUserLastNamesByPattern(String pattern, User user);

    String getAvatar(Long id);

    List<User> getOnlineCsrs();
}
