package com.netcracker.crm.dao;

import com.netcracker.crm.domain.request.OrderRowRequest;
import com.netcracker.crm.domain.model.Order;

import java.time.LocalDate;
import java.util.List;

/**
 * @author YARUS
 */
public interface OrderDao extends CrudDao<Order> {
    List<Order> findAllByDateFinish(LocalDate date);

    List<Order> findAllByPreferredDate(LocalDate date);

    List<Order> findAllByProductId(Long id);

    List<Order> findAllByCustomerId(Long id);

    List<Order> findAllByCsrId(Long id);

    List<Order> findOrderRows(OrderRowRequest orderRowRequest);

    Long getOrderRowsCount(OrderRowRequest orderRowRequest);

    List<Order> findOrgOrdersByCustId(Long custId);

    List<Order> findByIdOrTitle(String pattern);
}
