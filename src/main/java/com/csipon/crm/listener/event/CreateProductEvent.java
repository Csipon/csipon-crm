package com.csipon.crm.listener.event;

import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.User;
import org.springframework.context.ApplicationEvent;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 16.05.2017
 */
public class CreateProductEvent extends ApplicationEvent {

    private User user;
    private Product product;

    public CreateProductEvent(Object source, Product product, User user) {
        super(source);
        this.user = user;
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
