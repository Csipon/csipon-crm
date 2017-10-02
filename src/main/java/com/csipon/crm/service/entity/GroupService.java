package com.csipon.crm.service.entity;

import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.GroupDto;
import com.csipon.crm.dto.bulk.GroupBulkDto;
import com.csipon.crm.domain.model.Region;
import com.csipon.crm.domain.request.GroupRowRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by Pasha on 01.05.2017.
 */
public interface GroupService {

    Group create(GroupDto groupDto);

    List<AutocompleteDto> getAutocompleteGroup(String pattern);

    Map<String, Object> getGroupPage(GroupRowRequest request);

    Group getGroupById(Long id);

    boolean update(GroupDto groupDto);

    List<Group> getGroupsByRegion(Region region);

    List<Group> getGroupsByDiscountId(Long id, User user);

    boolean bulkUpdate(GroupBulkDto bulkDto);
}
