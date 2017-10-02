package com.csipon.crm.dto.bulk;


import java.util.Set;

/**
 * Created by bpogo on 5/15/2017.
 */
public class DiscountBulkDto {
    private Set<Long> itemIds;
    private Boolean isPercentageChanged;
    private Double percentage;
    private Boolean isDescriptionChanged;
    private String description;
    private Boolean isActiveChanged;
    private Boolean active;

    public Set<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Boolean isPercentageChanged() {
        return isPercentageChanged != null && isPercentageChanged;
    }

    public void setIsPercentageChanged(Boolean percentageChanged) {
        isPercentageChanged = percentageChanged;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Boolean isDescriptionChanged() {
        return isDescriptionChanged != null && isDescriptionChanged;
    }

    public void setIsDescriptionChanged(Boolean isDescriptionChanged) {
        this.isDescriptionChanged = isDescriptionChanged;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActiveChanged() {
        return isActiveChanged != null && isActiveChanged;
    }

    public void setIsActiveChanged(Boolean isActiveChanged) {
        this.isActiveChanged = isActiveChanged;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
