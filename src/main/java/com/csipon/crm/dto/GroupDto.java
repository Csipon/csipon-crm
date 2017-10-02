package com.csipon.crm.dto;

/**
 * Created by Pasha on 02.05.2017.
 */
public class GroupDto {
    private Long id;
    private String name;
    private Long discountId;
    private Long[] products;


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


    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Long[] getProducts() {
        return products;
    }

    public void setProducts(Long[] products) {
        this.products = products;
    }
}
