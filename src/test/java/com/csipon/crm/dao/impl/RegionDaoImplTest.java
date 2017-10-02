package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.RegionDao;
import com.csipon.crm.domain.model.Region;
import com.csipon.crm.domain.real.RealRegion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionDaoImplTest {

    @Autowired
    private RegionDao regionDao;

    private Region regionCreated;

    @Before
    public void create() throws Exception {
        regionCreated = new RealRegion();
        regionCreated.setName("test region name");
        regionDao.create(regionCreated);
        assertNotNull(regionCreated.getId());
    }

    @Test
    public void findAndUpdateAndCount() throws Exception {
        Region regionFoundById = regionDao.findById(regionCreated.getId());
        assertEquals(regionCreated.getName(), regionFoundById.getName());

        Region regionDaoByName = regionDao.findByName(regionCreated.getName());
        assertEquals(regionCreated.getId(), regionDaoByName.getId());

        regionCreated.setName("test update region name Region");
        assertEquals(regionDao.update(regionCreated), regionCreated.getId());

        assertEquals(regionDao.getCount(), new Long(1));
    }

    @After
    public void delete() throws Exception {
        long affectedRows = regionDao.delete(regionCreated);
        assertEquals(affectedRows, 1L);
    }

}