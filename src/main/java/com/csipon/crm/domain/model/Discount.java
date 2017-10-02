package com.csipon.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface Discount {

    Long getId();

    void setId(Long id);

    String getTitle();

    void setTitle(String title);

    Double getPercentage();

    void setPercentage(Double percentage);

    String getDescription();

    void setDescription(String description);

    Boolean isActive();

    void setActive(Boolean active);
}
