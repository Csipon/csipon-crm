package com.csipon.crm.dto.bulk;


import java.util.Set;

/**
 * Created by bpogo on 5/15/2017.
 */
public class ProductBulkDto {
    private Set<Long> itemIds;
    private Double defaultPrice;
    private Boolean isDefaultPriceChanged;
    private String statusName;
    private Boolean isStatusNameChanged;
    private String description;
    private Boolean isDescriptionChanged;
    private Long discountId;
    private Boolean isDiscountIdChanged;
    private Long groupId;
    private Boolean isGroupIdChanged;

    public Set<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Boolean isDefaultPriceChanged() {
        return isDefaultPriceChanged != null && isDefaultPriceChanged;
    }

    public void setIsDefaultPriceChanged(Boolean isDefaultPriceChanged) {
        this.isDefaultPriceChanged = isDefaultPriceChanged;
    }

    public Boolean isStatusNameChanged() {
        return isStatusNameChanged != null && isStatusNameChanged;
    }

    public void setIsStatusNameChanged(Boolean isStatusNameChanged) {
        this.isStatusNameChanged = isStatusNameChanged;
    }

    public Boolean isDescriptionChanged() {
        return isDescriptionChanged != null && isDescriptionChanged;
    }

    public void setIsDescriptionChanged(Boolean isDescriptionChanged) {
        this.isDescriptionChanged = isDescriptionChanged;
    }

    public Boolean isDiscountIdChanged() {
        return isDiscountIdChanged != null && isDiscountIdChanged;
    }

    public void setIsDiscountIdChanged(Boolean isDiscountIdChanged) {
        this.isDiscountIdChanged = isDiscountIdChanged;
    }

    public Boolean isGroupIdChanged() {
        return isGroupIdChanged != null && isGroupIdChanged;
    }

    public void setIsGroupIdChanged(Boolean isGroupIdChanged) {
        this.isGroupIdChanged = isGroupIdChanged;
    }
}
