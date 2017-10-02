package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.GroupDao;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.real.RealGroup;
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
public class GroupDaoImplTest {

    @Autowired
    private GroupDao groupDao;

    private Group groupCreated;

    @Before
    public void create() throws Exception {
        groupCreated = new RealGroup();
        groupCreated.setName("test group name");
        groupDao.create(groupCreated);
        assertNotNull(groupCreated.getId());
    }

    @Test
    public void findAndUpdateAndCount() throws Exception {
        Group groupFoundById = groupDao.findById(groupCreated.getId());
        assertEquals(groupCreated.getName(), groupFoundById.getName());

        List<Group> groupsFoundByName = groupDao.findByName(groupCreated.getName());
        assertEquals(groupCreated.getId(), groupsFoundByName.get(0).getId());

        groupCreated.setName("test update group name");
        assertEquals(groupDao.update(groupCreated), groupCreated.getId());

        assertEquals(groupDao.getCount(), new Long(1));
    }

    @After
    public void delete() throws Exception {
        long affectedRows = groupDao.delete(groupCreated);
        assertEquals(affectedRows, 1L);
    }

}