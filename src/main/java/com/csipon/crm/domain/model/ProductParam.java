
package com.csipon.crm.domain.model;

/**
 *
 * @author YARUS
 */
public interface ProductParam {
    Long getId();

    void setId(Long id);

    String getParamName();

    void setParamName(String paramName);
    
    String getValue();

    void setValue(String value);
    
    Product getProduct();

    void setProduct(Product product);
}
