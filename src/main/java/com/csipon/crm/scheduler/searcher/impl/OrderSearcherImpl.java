package com.csipon.crm.scheduler.searcher.impl;

import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.OrderStatus;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.scheduler.OrderSchedulerSqlGenerator;
import com.csipon.crm.scheduler.searcher.OrderSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasha on 12.05.2017.
 */
@Component
public class OrderSearcherImpl implements OrderSearcher {

    private final OrderDao orderDao;
    private final OrderSchedulerSqlGenerator sqlGenerator;

    @Autowired
    public OrderSearcherImpl(OrderDao orderDao, OrderSchedulerSqlGenerator sqlGenerator) {
        this.orderDao = orderDao;
        this.sqlGenerator = sqlGenerator;
    }

    public List<Order> searchForActivate(List<User> csr) {
        if (csr.isEmpty()) {
            return new ArrayList<>();
        }
        LocalDateTime toDate = getSearchTime();
        return orderDao.findAllByPrefDateAndStatus(sqlGenerator, csr, toDate, OrderStatus.PROCESSING);
    }

    @Override
    public List<Order> searchForPause(List<User> csr) {
        if (csr.isEmpty()) {
            return new ArrayList<>();
        }
        return orderDao.findAllByStatus(sqlGenerator, csr, OrderStatus.REQUEST_TO_PAUSE);
    }

    @Override
    public List<Order> searchForResume(List<User> csr) {
        if (csr.isEmpty()) {
            return new ArrayList<>();
        }
        return orderDao.findAllByStatus(sqlGenerator, csr, OrderStatus.REQUEST_TO_RESUME);
    }

    @Override
    public List<Order> searchForDisable(List<User> csr) {
        if (csr.isEmpty()) {
            return new ArrayList<>();
        }
        return orderDao.findAllByStatus(sqlGenerator, csr, OrderStatus.REQUEST_TO_DISABLE);
    }

    @Override
    public List<Order> searchCsrProcessingOrder(Long csrId) {
        LocalDateTime toDate = getSearchTime();
        return orderDao.findAllByCsrId(toDate, OrderStatus.PROCESSING, csrId);
    }


    @Override
    public List<Order> searchCsrPauseOrder(Long csrId) {
        return orderDao.findAllByCsrId(OrderStatus.REQUEST_TO_PAUSE, csrId);
    }

    @Override
    public List<Order> searchCsrResumeOrder(Long csrId) {
        return orderDao.findAllByCsrId(OrderStatus.REQUEST_TO_RESUME, csrId);
    }

    @Override
    public List<Order> searchCsrDisableOrder(Long csrId) {
        return orderDao.findAllByCsrId(OrderStatus.REQUEST_TO_DISABLE, csrId);
    }

    private LocalDateTime getSearchTime() {
        return LocalDateTime.now().plusDays(1);
    }
}
