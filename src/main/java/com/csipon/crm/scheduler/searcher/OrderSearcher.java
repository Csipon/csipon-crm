package com.csipon.crm.scheduler.searcher;

import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;

import java.util.List;

/**
 * Created by Pasha on 12.05.2017.
 */
public interface OrderSearcher {
    List<Order> searchForActivate(List<User> csr);

    List<Order> searchForPause(List<User> csr);

    List<Order> searchForResume(List<User> csr);

    List<Order> searchForDisable(List<User> csr);

    List<Order> searchCsrProcessingOrder(Long csrId);

    List<Order> searchCsrPauseOrder(Long csrId);

    List<Order> searchCsrResumeOrder(Long csrId);

    List<Order> searchCsrDisableOrder(Long csrId);
}
