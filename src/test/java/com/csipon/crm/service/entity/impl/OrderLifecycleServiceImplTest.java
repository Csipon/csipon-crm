package com.csipon.crm.service.entity.impl;

import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.*;
import com.csipon.crm.domain.real.RealOrder;
import com.csipon.crm.domain.real.RealProduct;
import com.csipon.crm.domain.real.RealUser;
import com.csipon.crm.service.entity.OrderLifecycleService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by bpogo on 5/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderLifecycleServiceImplTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLifecycleService lifecycleService;

    private User csr;
    private User customer;
    private Order order;
    private Product product;

    @Before
    public void setUp() throws Exception {
        customer = new RealUser();
        customer.setPassword("test password");
        customer.setFirstName("test first name");
        customer.setMiddleName("test middle name");
        customer.setEmail(UUID.randomUUID().toString());
        customer.setEnable(false);
        customer.setAccountNonLocked(false);
        customer.setContactPerson(false);
        customer.setUserRole(UserRole.ROLE_CUSTOMER);

        csr = new RealUser();
        csr.setPassword("test password");
        csr.setFirstName("test first name");
        csr.setMiddleName("test middle name");
        csr.setEmail(UUID.randomUUID().toString());
        csr.setEnable(false);
        csr.setAccountNonLocked(false);
        csr.setContactPerson(false);
        csr.setUserRole(UserRole.ROLE_CSR);

        this.order = new RealOrder();
        this.order.setDate(LocalDateTime.now());
        this.order.setPreferedDate(LocalDateTime.now());
        this.order.setCustomer(customer);
        this.order.setCsr(customer);

        product = new RealProduct();
        product.setTitle("test product title");
        product.setStatus(ProductStatus.ACTUAL);
        this.order.setProduct(product);

        userDao.create(customer);
        userDao.create(csr);
        productDao.create(product);
        orderDao.create(order);
    }

    @After
    public void tearDown() throws Exception {
        orderDao.delete(order.getId());
        productDao.delete(product.getId());
        userDao.delete(customer.getId());
        userDao.delete(csr.getId());
    }

    @Test
    public void processOrder() throws Exception {
        boolean isAccepted = lifecycleService.processOrder(order.getId(), csr.getId());
        Order acceptedOrder = orderDao.findById(order.getId());

        assertTrue(isAccepted);
        assertEquals(OrderStatus.PROCESSING, acceptedOrder.getStatus());
    }

    @Test
    public void activateOrder() throws Exception {
        lifecycleService.processOrder(order.getId(), csr.getId());
        boolean isActivated = lifecycleService.activateOrder(order.getId());
        Order activatedOrder = orderDao.findById(order.getId());

        assertTrue(isActivated);
        assertEquals(OrderStatus.ACTIVE, activatedOrder.getStatus());
    }

    @Test
    public void pauseOrder() throws Exception {
        lifecycleService.processOrder(order.getId(), csr.getId());
        lifecycleService.activateOrder(order.getId());
        lifecycleService.requestToPauseOrder(order.getId());
        boolean isPaused = lifecycleService.pauseOrder(order.getId());
        Order pausedOrder = orderDao.findById(order.getId());

        assertTrue(isPaused);
        assertEquals(OrderStatus.PAUSED, pausedOrder.getStatus());
    }

    @Test
    public void resumeOrderFromPausedToActive() throws Exception {
        lifecycleService.processOrder(order.getId(), csr.getId());
        lifecycleService.activateOrder(order.getId());
        lifecycleService.requestToPauseOrder(order.getId());
        lifecycleService.pauseOrder(order.getId());
        lifecycleService.requestToResumeOrder(order.getId());
        boolean isResumed = lifecycleService.resumeOrder(order.getId());
        Order activatedOrder = orderDao.findById(order.getId());

        assertTrue(isResumed);
        assertEquals(OrderStatus.ACTIVE, activatedOrder.getStatus());
    }

    @Test
    public void cancelOrderFromActive() throws Exception {
        lifecycleService.processOrder(order.getId(), csr.getId());
        lifecycleService.activateOrder(order.getId());
        lifecycleService.requestToDisableOrder(order.getId());
        boolean isDisabled = lifecycleService.disableOrder(order.getId());
        Order activatedOrder = orderDao.findById(order.getId());

        assertTrue(isDisabled);
        assertEquals(OrderStatus.DISABLED, activatedOrder.getStatus());
    }

    @Test
    public void cancelOrderFromPaused() throws Exception {
        lifecycleService.processOrder(order.getId(), csr.getId());
        lifecycleService.activateOrder(order.getId());
        lifecycleService.requestToPauseOrder(order.getId());
        lifecycleService.pauseOrder(order.getId());
        lifecycleService.requestToDisableOrder(order.getId());
        boolean isDisabled = lifecycleService.disableOrder(order.getId());
        Order activatedOrder = orderDao.findById(order.getId());

        assertTrue(isDisabled);
        assertEquals(OrderStatus.DISABLED, activatedOrder.getStatus());
    }
}