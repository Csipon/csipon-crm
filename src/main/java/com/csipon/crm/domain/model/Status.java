package com.csipon.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface Status {
    public Long getId();
    public String getName();


    static Status getStatusByID(Long id) {
        for (ComplaintStatus complaintStatus: ComplaintStatus.values()) {
            if (complaintStatus.getId().equals(id)) {
                return complaintStatus;
            }
        }
        for (OrderStatus orderStatus: OrderStatus.values()) {
            if (orderStatus.getId().equals(id)) {
                return orderStatus;
            }
        }
        for (ProductStatus productStatus: ProductStatus.values()) {
            if (productStatus.getId().equals(id)) {
                return productStatus;
            }
        }
        return null;
    }
}
