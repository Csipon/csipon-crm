package com.csipon.crm.domain.model;


/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface Product {

    Long getId();

    void setId(Long id);

    String getTitle();

    void setTitle(String title);

    Double getDefaultPrice();

    void setDefaultPrice(Double defaultPrice);

    ProductStatus getStatus();

    void setStatus(ProductStatus status);

    String getDescription();

    void setDescription(String description);

    Discount getDiscount();

    void setDiscount(Discount discount);

    Group getGroup();

    void setGroup(Group group);
}
