package com.csipon.crm.dao;


import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.real.RealGroup;
import com.csipon.crm.domain.request.GroupRowRequest;
import com.csipon.crm.dto.GroupTableDto;

import java.util.List;
import java.util.Set;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */
public interface GroupDao extends CrudDao<Group> {
    List<Group> findByName(String name);

    Long getCount();

    Long getCount(GroupRowRequest request);

    List<GroupTableDto> getPartRows(GroupRowRequest request);

    List<Group> findByIdOrTitle(String pattern);

    List<Group> findByDiscountId(Long id);

    List<Group> findByDiscountIdAndCustomerId(Long discountId, Long customerId);

    boolean bulkUpdate(Set<Long> groupIDs, RealGroup groupTemplate);
}
