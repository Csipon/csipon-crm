package com.csipon.crm.listener.event;

import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductStatus;
import com.csipon.crm.domain.model.User;
import org.springframework.context.ApplicationEvent;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 16.05.2017
 */

public class ChangeStatusProductEvent extends ApplicationEvent {

    private boolean isDone;
    private Product product;
    private User user;
    private ProductStatus changeToStatus;

    public ChangeStatusProductEvent(Object source, Product product, User user, ProductStatus changeToStatus) {
        super(source);
        this.product = product;
        this.user = user;
        this.changeToStatus = changeToStatus;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProductStatus getChangeToStatus() {
        return changeToStatus;
    }

    public void setChangeToStatus(ProductStatus changeToStatus) {
        this.changeToStatus = changeToStatus;
    }
}
