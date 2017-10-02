package com.csipon.crm.dto;

import java.util.List;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RegionDto {
    Long id;
    List<Long> groupIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }
}
