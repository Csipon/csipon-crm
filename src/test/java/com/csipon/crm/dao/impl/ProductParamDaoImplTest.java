package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.dao.GroupDao;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.domain.real.RealDiscount;
import com.csipon.crm.domain.real.RealGroup;
import com.csipon.crm.domain.real.RealProduct;
import com.csipon.crm.domain.real.RealProductParam;
import com.csipon.crm.dao.ProductParamDao;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductParam;
import com.csipon.crm.domain.model.ProductStatus;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author YARUS
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductParamDaoImplTest {
    
    @Autowired
    private DiscountDao discountDao;
    
    @Autowired
    private GroupDao groupDao;
        
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private ProductParamDao productParamDao;
    
    private Discount discountCreated;
    private Group groupCreated;
    private Product productCreated;
    private ProductParam productParamCreated;
    
    @Before
    public void create() throws Exception {
        discountCreated = new RealDiscount();
        discountCreated.setTitle("test title discount");
        discountCreated.setPercentage(60.4);
        discountCreated.setDescription("test description discount");
        discountCreated.setActive(false);

        groupCreated = new RealGroup();
        groupCreated.setName("test name group");

        productCreated = new RealProduct();
        productCreated.setTitle("test title product");
        productCreated.setDefaultPrice(7.7);
        productCreated.setStatus(ProductStatus.ACTUAL);
        productCreated.setDescription("test description product");
        productCreated.setDiscount(discountCreated);
        productCreated.setGroup(groupCreated);
        
        productParamCreated = new RealProductParam();
        productParamCreated.setParamName("test name for param for ptoduct");
        productParamCreated.setValue("test value for param for ptoduct");
        productParamCreated.setProduct(productCreated);
        
        assertNotNull(productParamDao.create(productParamCreated));
    }
    
    @Test
    public void findAndUpdate() throws Exception {
        ProductParam productParamFoundById = productParamDao.findById(productParamCreated.getId());
        assertEquals(productParamCreated.getParamName(), productParamFoundById.getParamName());        
        assertEquals(productParamCreated.getValue(), productParamFoundById.getValue());
        
        List<ProductParam> productParamsFoundByParamName = productParamDao.findAllByParamName(productParamCreated.getParamName());
        assertEquals(productParamCreated.getId(), productParamsFoundByParamName.get(0).getId());

        List<ProductParam> productParamsFoundByProductId = productParamDao.findAllByProductId(productCreated.getId());
        assertEquals(productParamCreated.getId(), productParamsFoundByProductId.get(0).getId());

        productParamCreated.setValue("updated test value for param for ptoduct");
        assertEquals(productParamDao.update(productParamCreated), productParamCreated.getId());
    }

    @After
    public void delete() throws Exception {
        long affectedRows = productParamDao.delete(productParamCreated);
        assertEquals(affectedRows, 1L);
        productDao.delete(productCreated);
        discountDao.delete(discountCreated);
        groupDao.delete(groupCreated);        
    }    
}
