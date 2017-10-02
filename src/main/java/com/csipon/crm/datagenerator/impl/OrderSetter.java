package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.domain.model.*;
import com.netcracker.crm.domain.model.*;
import com.csipon.crm.domain.real.RealOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Pasha on 05.05.2017.
 */
@Service
public class OrderSetter extends AbstractSetter<Order> {
    private List<User> customers;
    private List<User> csrs;
    private Map<String, List<Product>> products;

    private final OrderDao orderDao;

    @Autowired
    public OrderSetter(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<Order> generate(int numbers) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < numbers; i++) {
            Order order = generateObject();
            orderDao.create(order);
            orders.add(order);
        }
        return orders;
    }

    @Override
    public Order generateObject() {
        Order order = new RealOrder();
        order.setDate(getOrderDate());
        order.setPreferedDate(getPreferDate());
        setProductCustomer(order);
        OrderStatus orderStatus = getStatus();
        order.setStatus(orderStatus);
        if (orderStatus != OrderStatus.NEW) {
            order.setCsr(getCsr());
        }
        return order;
    }

    private OrderStatus getStatus() {
        return OrderStatus.values()[random.nextInt(OrderStatus.values().length)];
    }

    private User getCsr() {
        return csrs.get(random.nextInt(csrs.size()));
    }

    private User getCustomer() {
        return customers.get(random.nextInt(customers.size()));
    }


    private LocalDateTime getOrderDate() {
        return LocalDateTime.now().minusDays(random.nextInt(150) + 1);
    }

    private LocalDateTime getPreferDate() {
        return LocalDateTime.now().plusDays(random.nextInt(30));
    }


    public void setProductCustomer(Order order) {
        User customer = getCustomer();
        List<Product> productList = new ArrayList<>();
        Region customerRegion = customer.getAddress().getRegion();
        productList.addAll(products.get(customerRegion.getName()));
        Product product;
        while (true) {
            if (productList.size() == 0) {
                customer = getCustomer();
                customerRegion = customer.getAddress().getRegion();
                productList.addAll(products.get(customerRegion.getName()));
            }
            if (productList.size() != 0) {
                int num = random.nextInt(productList.size());
                if (num > 0) {
                    product = productList.remove(num);
                } else {
                    continue;
                }
                if (product.getStatus() != ProductStatus.PLANNED) {
                    break;
                }
            }

        }
        order.setCustomer(customer);
        order.setProduct(product);
    }

    public void setCustomers(List<User> customers) {
        this.customers = customers;
    }

    public void setCsrs(List<User> csrs) {
        this.csrs = csrs;
    }

    public void setProducts(Map<String, List<Product>> products) {
        this.products = products;
    }
}
