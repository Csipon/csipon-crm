package com.csipon.crm.dto.bulk;


import java.util.Set;

/**
 * Created by bpogo on 5/15/2017.
 */
public class GroupBulkDto {
    private Set<Long> itemIds;
    private Long discountId;
    private Boolean isDiscountIdChanged;

    public Set<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Boolean isDiscountIdChanged() {
        return isDiscountIdChanged;
    }

    public void setIsDiscountIdChanged(Boolean isDiscountIdChanged) {
        this.isDiscountIdChanged = isDiscountIdChanged;
    }
}
