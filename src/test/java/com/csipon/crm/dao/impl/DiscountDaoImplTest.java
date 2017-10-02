package com.csipon.crm.dao.impl;

import com.csipon.crm.domain.real.RealDiscount;
import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.domain.model.Discount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountDaoImplTest {

    @Autowired
    private DiscountDao discountDao;

    private Discount discountCreated;

    @Before
    public void create() throws Exception {
        discountCreated = new RealDiscount();
        discountCreated.setTitle("test discount title");
        discountCreated.setDescription("test discount description");
        discountCreated.setPercentage(0.5);
        discountCreated.setActive(true);
        assertNotNull(discountDao.create(discountCreated));
    }

    @Test
    public void findAndUpdateAndCount() throws Exception {
        Discount discountFoundById = discountDao.findById(discountCreated.getId());
        assertEquals(discountCreated.getTitle(), discountFoundById.getTitle());

        List<Discount> discountsFoundByName = discountDao.findByTitle(discountCreated.getTitle());
        assertEquals(discountCreated.getId(), discountsFoundByName.get(0).getId());

        discountCreated.setTitle("test update discount title");
        assertEquals(discountDao.update(discountCreated), discountCreated.getId());

        assertEquals(discountDao.getCount(), new Long(1));
    }

    @After
    public void delete() throws Exception {
        long affectedRows = discountDao.delete(discountCreated);
        assertEquals(affectedRows, 1L);
    }

}