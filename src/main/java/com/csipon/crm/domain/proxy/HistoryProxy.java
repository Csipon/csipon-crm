package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.HistoryDao;
import com.csipon.crm.domain.model.*;
import com.netcracker.crm.domain.model.*;

import java.time.LocalDateTime;

/**
 * @author Karpunets
 * @since 14.05.2017
 */
public class HistoryProxy implements History {
    private long id;
    private History history;
    private HistoryDao historyDao;

    public HistoryProxy(HistoryDao historyDao) {
        this.historyDao = historyDao;
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
    public Status getNewStatus() {
        return getHistory().getNewStatus();
    }

    @Override
    public void setNewStatus(Status newStatus) {
        getHistory().setNewStatus(newStatus);
    }

    @Override
    public LocalDateTime getDateChangeStatus() {
        return getHistory().getDateChangeStatus();
    }

    @Override
    public void setDateChangeStatus(LocalDateTime dateChangeStatus) {
        getHistory().setDateChangeStatus(dateChangeStatus);
    }

    @Override
    public String getDescChangeStatus() {
        return getHistory().getDescChangeStatus();
    }

    @Override
    public void setDescChangeStatus(String descChangeStatus) {
        getHistory().setDescChangeStatus(descChangeStatus);
    }

    @Override
    public Order getOrder() {
        return getHistory().getOrder();
    }

    @Override
    public void setOrder(Order order) {
        getHistory().setOrder(order);
    }

    @Override
    public Complaint getComplaint() {
        return getHistory().getComplaint();
    }

    @Override
    public void setComplaint(Complaint complaint) {
        getHistory().setComplaint(complaint);
    }

    @Override
    public Product getProduct() {
        return getHistory().getProduct();
    }

    @Override
    public void setProduct(Product product) {
        getHistory().setProduct(product);
    }

    private History getHistory() {
        if (history == null) {
            history = historyDao.findById(id);
        }
        return history;
    }
}