package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.OrganizationDao;
import com.csipon.crm.domain.model.Organization;
import com.csipon.crm.domain.real.RealOrganization;
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
public class OrganizationDaoImplTest {

    @Autowired
    private OrganizationDao organizationDao;

    private Organization orgCreated;

    @Before
    public void create() throws Exception {
        orgCreated = new RealOrganization();
        orgCreated.setName("test organization name");
        organizationDao.create(orgCreated);
        assertNotNull(orgCreated.getId());
    }

    @Test
    public void findAndUpdate() throws Exception {
        Organization orgFoundById = organizationDao.findById(orgCreated.getId());
        assertEquals(orgCreated.getName(), orgFoundById.getName());

        Organization orgFoundByName = organizationDao.findByName(orgCreated.getName());
        assertEquals(orgCreated.getId(), orgFoundByName.getId());

        orgCreated.setName("test updated organization name");
        assertEquals(organizationDao.update(orgCreated), orgCreated.getId());

    }

    @After
    public void delete() throws Exception {
        long affectedRows = organizationDao.delete(orgCreated);
        assertEquals(affectedRows, 1L);
    }

}