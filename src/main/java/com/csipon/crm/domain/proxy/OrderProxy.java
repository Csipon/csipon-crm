package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.OrderStatus;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.state.order.OrderState;

import java.time.LocalDateTime;

/**
 * @author Karpunets
 * @since 14.05.2017
 */
public class OrderProxy implements Order {
    private long id;
    private Order order;
    private OrderDao orderDao;

    public OrderProxy(OrderDao orderDao) {
        this.orderDao = orderDao;
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
    public OrderStatus getStatus() {
        return getOrder().getStatus();
    }

    @Override
    public void setStatus(OrderStatus status) {
        getOrder().setStatus(status);
    }

    @Override
    public User getCustomer() {
        return getOrder().getCustomer();
    }

    @Override
    public void setCustomer(User customer) {
        getOrder().setCustomer(customer);
    }

    @Override
    public Product getProduct() {
        return getOrder().getProduct();
    }

    @Override
    public void setProduct(Product product) {
        getOrder().setProduct(product);
    }

    @Override
    public User getCsr() {
        return getOrder().getCsr();
    }

    @Override
    public void setCsr(User csr) {
        getOrder().setCsr(csr);
    }

    @Override
    public LocalDateTime getDate() {
        return getOrder().getDate();
    }

    @Override
    public void setDate(LocalDateTime date) {
        getOrder().setDate(date);
    }

    @Override
    public LocalDateTime getPreferedDate() {
        return getOrder().getPreferedDate();
    }

    @Override
    public void setPreferedDate(LocalDateTime preferedDate) {
        getOrder().setPreferedDate(preferedDate);
    }

    @Override
    public OrderState getState() {
        return getOrder().getState();
    }

    @Override
    public void setState(OrderState state) {
        getOrder().setState(state);
    }

    private Order getOrder() {
        if (order == null) {
            order = orderDao.findById(id);
        }
        return order;
    }
}