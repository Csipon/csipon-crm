
package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.ProductParamDao;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductParam;

/**
 *
 * @author YARUS
 */
public class ProductParamProxy implements ProductParam{

    private long id;
    private ProductParam productParam;
    private ProductParamDao productParamDao;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getParamName() {
        return getProductParam().getParamName();
    }

    @Override
    public void setParamName(String paramName) {
        getProductParam().setParamName(paramName);
    }

    @Override
    public String getValue() {
        return getProductParam().getValue();
    }

    @Override
    public void setValue(String value) {
        getProductParam().setValue(value);
    }

    @Override
    public Product getProduct() {
        return getProductParam().getProduct();
    }

    @Override
    public void setProduct(Product product) {
        getProductParam().setProduct(product);
    }
    
    private ProductParam getProductParam() {
        if (productParam == null) {
            productParam = productParamDao.findById(id);
        }
        return productParam;
    }
}
