package com.csipon.crm.service.entity.impl;

import com.csipon.crm.dao.HistoryDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.domain.model.History;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.scheduler.cacher.impl.OrderCache;
import com.csipon.crm.service.email.AbstractEmailSender;
import com.csipon.crm.service.email.EmailParamKeys;
import com.csipon.crm.service.email.EmailType;
import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.service.email.EmailParam;
import com.csipon.crm.service.entity.OrderLifecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

/**
 * Created by bpogo on 5/10/2017.
 */
@Service
public class OrderLifecycleServiceImpl implements OrderLifecycleService {
    private final OrderDao orderDao;
    private final HistoryDao historyDao;
    private final ProductDao productDao;
    private final UserDao userDao;
    private final OrderCache orderCache;
    private final AbstractEmailSender sender;

    @Autowired
    public OrderLifecycleServiceImpl(OrderDao orderDao, HistoryDao historyDao,
                                     ProductDao productDao, UserDao userDao, OrderCache orderCache,
                                     @Qualifier("orderSender") AbstractEmailSender sender) {
        this.orderDao = orderDao;
        this.historyDao = historyDao;
        this.productDao = productDao;
        this.userDao = userDao;
        this.orderCache = orderCache;
        this.sender = sender;
    }

    @Override
    public boolean createOrder(Order order) {
        if (orderDao.create(order) != null) {
            History history = order.getState().newOrder();
            if (historyDao.create(history) != null){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean processOrder(Long orderId, Long csrId) {
        if (orderId == null || csrId == null) return false;

        Order order = orderDao.findById(orderId);
        User csr = userDao.findById(csrId);
        if (csr != null || order != null) {
            order.setCsr(csr);
            History history = order.getState().processOrder();
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean activateOrder(Long orderId) {
        if (orderId == null) return false;

        Order order = orderDao.findById(orderId);
        if (order != null) {
            History history = order.getState().activateOrder();
            orderCache.removeFromActivateCache(order.getCsr().getId(), orderId);
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean pauseOrder(Long orderId) {
        if (orderId == null) return false;

        Order order = orderDao.findById(orderId);
        if (order != null) {
            History history = order.getState().pauseOrder();
            orderCache.removeFromPauseCache(order.getCsr().getId(), orderId);
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean resumeOrder(Long orderId) {
        if (orderId == null) return false;

        Order order = orderDao.findById(orderId);
        if (order != null) {
            History history = order.getState().resumeOrder();
            orderCache.removeFromResumeCache(order.getCsr().getId(), orderId);
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean disableOrder(Long orderId) {
        if (orderId == null) return false;

        Order order = orderDao.findById(orderId);
        if (order != null) {
            History history = order.getState().disableOrder();
            orderCache.removeFromDisableCache(order.getCsr().getId(), orderId);
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean requestToResumeOrder(Long orderId) {
        if (orderId == null) return false;

        Order order = orderDao.findById(orderId);
        if (order != null) {
            History history = order.getState().requestToResumeOrder();
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean requestToPauseOrder(Long orderId) {
        if (orderId == null) return false;

        Order order = orderDao.findById(orderId);
        if (order != null) {
            History history = order.getState().requestToPauseOrder();
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean requestToDisableOrder(Long orderId) {
        if (orderId == null) return false;

        Order order = orderDao.findById(orderId);
        if (order != null) {
            History history = order.getState().requestToDisableOrder();
            if (saveCondition(order, history)){
                sendOrderStatus(order);
                return true;
            }
        }
        return false;
    }

    private boolean saveCondition(Order order, History history) {
        return historyDao.create(history) != null && orderDao.update(order) != null;
    }


    private void sendOrderStatus(Order order){
        EmailParam emailParam = new EmailParam(EmailType.ORDER_STATUS);
        emailParam.put(EmailParamKeys.ORDER, order);
        try {
            sender.send(emailParam);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
