package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.ComplaintDao;
import com.csipon.crm.domain.model.Complaint;
import com.csipon.crm.domain.model.ComplaintStatus;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;

import java.time.LocalDateTime;

/**
 * @author Karpunets
 * @since 14.05.2017
 */
public class ComplaintProxy implements Complaint {
    private long id;
    private Complaint complaint;

    private ComplaintDao complaintDao;

    public ComplaintProxy(ComplaintDao complaintDao) {
        this.complaintDao = complaintDao;
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
        return getComplaint().getTitle();
    }

    @Override
    public void setTitle(String title) {
        getComplaint().setTitle(title);
    }

    @Override
    public String getMessage() {
        return getComplaint().getMessage();
    }

    @Override
    public void setMessage(String message) {
        getComplaint().setMessage(message);
    }

    @Override
    public ComplaintStatus getStatus() {
        return getComplaint().getStatus();
    }

    @Override
    public void setStatus(ComplaintStatus status) {
        getComplaint().setStatus(status);
    }

    @Override
    public LocalDateTime getDate() {
        return getComplaint().getDate();
    }

    @Override
    public void setDate(LocalDateTime date) {
        getComplaint().setDate(date);
    }

    @Override
    public User getCustomer() {
        return getComplaint().getCustomer();
    }

    @Override
    public void setCustomer(User customer) {
        getComplaint().setCustomer(customer);
    }

    @Override
    public User getPmg() {
        return getComplaint().getPmg();
    }

    @Override
    public void setPmg(User pmg) {
        getComplaint().setPmg(pmg);
    }

    @Override
    public Order getOrder() {
        return getComplaint().getOrder();
    }

    @Override
    public void setOrder(Order order) {
        getComplaint().setOrder(order);
    }

    private Complaint getComplaint() {
        if (complaint == null) {
            complaint = complaintDao.findById(id);
        }
        return complaint;
    }
}
