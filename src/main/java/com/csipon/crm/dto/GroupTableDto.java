package com.csipon.crm.dto;

/**
 * Created by Pasha on 09.05.2017.
 */
public class GroupTableDto {

    private Long id;
    private String name;
    private String discountName;
    private Double discountValue;
    private Boolean discountActive;
    private Long numberProducts;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Boolean getDiscountActive() {
        return discountActive;
    }

    public void setDiscountActive(Boolean discountActive) {
        this.discountActive = discountActive;
    }

    public Long getNumberProducts() {
        return numberProducts;
    }

    public void setNumberProducts(Long numberProducts) {
        this.numberProducts = numberProducts;
    }
}
