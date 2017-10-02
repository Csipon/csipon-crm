package com.csipon.crm.dto.row;

/**
 * @author Karpunets
 * @since 05.05.2017
 */
public class ProductRowDto {

    private Long id;
    private String title;
    private String status;
    private Double price;
    private Long discount;
    private String discountTitle;
    private Double discountPercentage;
    private Boolean discountActive;
    private Long group;
    private String groupName;
    private Long groupDiscount;
    private String groupDiscountTitle;
    private Double groupDiscountPercentage;
    private Boolean groupDiscountActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Boolean getDiscountActive() {
        return discountActive;
    }

    public void setDiscountActive(Boolean discountActive) {
        this.discountActive = discountActive;
    }

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    public String getDiscountTitle() {
        return discountTitle;
    }

    public void setDiscountTitle(String discountTitle) {
        this.discountTitle = discountTitle;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getGroupDiscount() {
        return groupDiscount;
    }

    public void setGroupDiscount(Long groupDiscount) {
        this.groupDiscount = groupDiscount;
    }

    public String getGroupDiscountTitle() {
        return groupDiscountTitle;
    }

    public void setGroupDiscountTitle(String groupDiscountTitle) {
        this.groupDiscountTitle = groupDiscountTitle;
    }

    public Double getGroupDiscountPercentage() {
        return groupDiscountPercentage;
    }

    public void setGroupDiscountPercentage(Double groupDiscountPercentage) {
        this.groupDiscountPercentage = groupDiscountPercentage;
    }

    public Boolean getGroupDiscountActive() {
        return groupDiscountActive;
    }

    public void setGroupDiscountActive(Boolean groupDiscountActive) {
        this.groupDiscountActive = groupDiscountActive;
    }
}
