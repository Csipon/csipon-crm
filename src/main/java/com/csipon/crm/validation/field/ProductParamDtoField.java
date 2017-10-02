
package com.csipon.crm.validation.field;

/**
 *
 * @author YARUS
 */
public enum ProductParamDtoField implements DtoField{
    
    PARAM_NAME("paramName", "Name"),
    VALUE("value", "Value");
    
    private final String name;
    private final String errorName;
    
    ProductParamDtoField(String dtoName, String errorName) {
        this.name = dtoName;
        this.errorName = errorName;
    }    
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getErrorName() {
        return this.errorName;
    }
    
}
