package com.csipon.crm.dao;

import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.request.OrderRowRequest;
import com.csipon.crm.scheduler.OrderSchedulerSqlGenerator;
import com.csipon.crm.domain.model.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author YARUS
 */
public interface OrderDao extends CrudDao<Order> {
    List<Order> findAllByDateFinish(LocalDate date);

    List<Order> findAllByPreferredDate(LocalDate date);

    List<Order> findAllByPrefDateAndStatus(OrderSchedulerSqlGenerator generator, List<User> csrs,
                                           LocalDateTime to, OrderStatus orderStatus);

    List<Order> findAllByStatus(OrderSchedulerSqlGenerator generator, List<User> csrs,
                                OrderStatus orderStatus);

    List<Order> findAllByProductId(Long id);

    List<Order> findAllByCustomerIds(List<Long> id, LocalDate from, LocalDate to, int orderByIndex);

    List<Order> findAllByCustomerId(Long id);

    List<Order> findAllByCsrId(Long id);

    List<Order> findAllByCsrId(LocalDateTime to, OrderStatus orderStatus, Long id);

    List<Order> findAllByCsrId(OrderStatus orderStatus, Long id);

    List<Order> findOrderRows(OrderRowRequest orderRowRequest);

    Long getOrderRowsCount(OrderRowRequest orderRowRequest);

    List<Order> findOrgOrdersByIdOrTitle(String pattern, Long customerId);

    List<Order> findByIdOrTitleByCustomer(String pattern, Long customerId);

    Boolean hasCustomerProduct(Long productId, Long customerId);

    Long checkOwnershipOfContactPerson(Long orderId, Long id);

    Long checkOwnershipOfCustomer(Long orderId, Long id);
}
