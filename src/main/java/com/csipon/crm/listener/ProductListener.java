package com.csipon.crm.listener;

import com.csipon.crm.dao.HistoryDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.listener.event.ChangeStatusProductEvent;
import com.csipon.crm.listener.event.CreateProductEvent;
import com.csipon.crm.domain.real.RealHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 16.05.2017
 */

@Component
public class ProductListener {

    private HistoryDao historyDao;
    private ProductDao productDao;

    @Autowired
    public ProductListener(HistoryDao historyDao, ProductDao productDao) {
        this.historyDao = historyDao;
        this.productDao = productDao;
    }

    @EventListener
    public void createProduct(CreateProductEvent event) {
        User user = event.getUser();
        Product product = event.getProduct();
        History history = generateHistory(product);
        String role = getRole(user);
        history.setDescChangeStatus(role + " with id " +
                user.getId() + " created Product");
        historyDao.create(history);
    }

    @EventListener(condition = "((#event.product.status.name.equals('PLANNED') || #event.product.status.name.equals('ACTUAL')) && " +
            "#event.changeToStatus.name.equals('OUTDATED')) || " +
            "(#event.product.status.name.equals('PLANNED') && #event.changeToStatus.name.equals('ACTUAL'))")
    public void outdatedStatus(ChangeStatusProductEvent event) {
        User user = event.getUser();
        Product product = event.getProduct();
        product.setStatus(event.getChangeToStatus());
        History history = generateHistory(product);
        String role = getRole(user);
        history.setDescChangeStatus("Status was changed by " + role + " with id " +
                user.getId());
        if(saveStatusAndHistory(product, history)){
            event.setDone(true);
        }
    }

    private String getRole(User user) {
        String role = user.getUserRole().getName();
        return role.substring(role.indexOf("_") + 1);
    }

    private History generateHistory(Product product) {
        History history = new RealHistory();
        history.setDateChangeStatus(LocalDateTime.now());
        history.setNewStatus(product.getStatus());
        history.setProduct(product);
        return history;
    }

    private boolean saveStatusAndHistory(Product product, History history) {
        boolean result = false;
        if (productDao.update(product) > 0 && historyDao.create(history) > 0) {
            result = true;
        }
        return result;
    }
}
