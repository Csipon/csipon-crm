package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.AddressDao;
import com.csipon.crm.dao.RegionDao;
import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.real.RealAddress;
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
 * @author Karpunets
 * @since 01.05.2017
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressDaoImplTest {

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private RegionDao regionDao;

    private Address addressCreated;
    private Region regionCreated;

    @Before
    public void create() throws Exception {
        addressCreated = new RealAddress();
        addressCreated.setLatitude(14.6);
        addressCreated.setLongitude(14.6);

        regionCreated = new RealRegion();
        regionCreated.setName("test region name");
        addressCreated.setRegion(regionCreated);
        addressDao.create(addressCreated);
        assertNotNull(addressCreated.getId());
    }

    @Test
    public void findAndUpdate() throws Exception {
        Address addressFoundById = addressDao.findById(addressCreated.getId());
        assertEquals(addressCreated.getRegion().getId(), addressFoundById.getRegion().getId());

        addressCreated.setLatitude(77.7);
        assertEquals(addressDao.update(addressCreated), addressCreated.getId());
    }

    @After
    public void delete() throws Exception {
        long affectedRows = addressDao.delete(addressCreated);
        assertEquals(affectedRows, 1L);
        regionDao.delete(regionCreated);
    }

}