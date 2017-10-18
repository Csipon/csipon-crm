package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Discount;

import javax.persistence.*;

@Entity
@Table(name = "discount")
public class RealDiscount implements Discount {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @Column
    private Double percentage;
    @Column
    private String description;
    @Column
    private Boolean active;

    public RealDiscount() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Double getPercentage() {
        return percentage;
    }

    @Override
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Boolean isActive() {
        return active != null && active;
    }


    public Boolean getActive() {
        return active;
    }

    @Override
    public void setActive(Boolean active) {
        this.active = active;
    }
}