package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.*;
import com.netcracker.crm.domain.model.*;
import com.csipon.crm.domain.real.RealOrder;
import com.csipon.crm.domain.real.RealProduct;
import com.csipon.crm.domain.real.RealUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Karpunets
 * @since 01.06.2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoImplTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    private User userCreated;
    private Order orderCreated;
    private Product productCreated;

    @Before
    public void create() throws Exception {
        userCreated = new RealUser();
        userCreated.setPassword("test password");
        userCreated.setFirstName("test first name");
        userCreated.setMiddleName("test middle name");
        userCreated.setEmail("test email");
        userCreated.setEnable(false);
        userCreated.setAccountNonLocked(false);
        userCreated.setContactPerson(false);
        userCreated.setUserRole(UserRole.ROLE_CUSTOMER);

        orderCreated = new RealOrder();
        orderCreated.setStatus(OrderStatus.NEW);
        orderCreated.setDate(LocalDateTime.now());
        orderCreated.setPreferedDate(LocalDateTime.now());
        orderCreated.setCustomer(userCreated);
        orderCreated.setCsr(userCreated);

        productCreated = new RealProduct();
        productCreated.setTitle("test product title");
        productCreated.setStatus(ProductStatus.OUTDATED);
        orderCreated.setProduct(productCreated);

        assertNotNull(orderDao.create(orderCreated));
    }

    @Test
    public void findAndUpdate() throws Exception {
        Order orderFoundById = orderDao.findById(orderCreated.getId());
        assertEquals(orderCreated.getStatus(), orderFoundById.getStatus());

        List<Order> ordersFoundByProductId = orderDao.findAllByProductId(productCreated.getId());
        assertEquals(orderCreated.getId(), ordersFoundByProductId.get(0).getId());

        List<Order> ordersFoundByCsrId = orderDao.findAllByCsrId(userCreated.getId());
        assertEquals(orderCreated.getId(), ordersFoundByCsrId.get(0).getId());

        List<Order> ordersFoundByCustomerId = orderDao.findAllByCustomerId(userCreated.getId());
        assertEquals(orderCreated.getId(), ordersFoundByCustomerId.get(0).getId());

        List<Order> ordersFoundByDateFinish = orderDao.findAllByDateFinish(orderCreated.getDate().toLocalDate());
        assertEquals(orderCreated.getId(), ordersFoundByDateFinish.get(0).getId());

        List<Order> ordersFoundByPreferredDate = orderDao.findAllByPreferredDate(orderCreated.getPreferedDate().toLocalDate());
        assertEquals(orderCreated.getId(), ordersFoundByPreferredDate.get(0).getId());

        orderCreated.setStatus(OrderStatus.DISABLED);
        assertEquals(orderDao.update(orderCreated), orderCreated.getId());
    }

    @After
    public void delete() throws Exception {
        long affectedRows = orderDao.delete(orderCreated);
        assertEquals(affectedRows, 1L);
        productDao.delete(productCreated);
        userDao.delete(userCreated);
    }

}
