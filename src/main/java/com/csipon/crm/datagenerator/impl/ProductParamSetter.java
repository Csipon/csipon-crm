
package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.real.RealProductParam;
import com.csipon.crm.dao.ProductParamDao;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author YARUS
 */
@Service
public class ProductParamSetter extends AbstractSetter<ProductParam> {

    @Autowired
    private ProductParamDao productParamDao;
    private List<String> paramNames = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private List<Product> products = new ArrayList<>(); 
    private Product actualProduct = null;
    
    //minimal count param on products
    private final int MIN_COUNT_PARAM = 2;
    private final int MAX_COUNT_PARAM = 6;
    private int countParamOnProduct;
    
    private int counter;
    private int counterParamName;
    private int counterValue;    
    
    @Value(value = "classpath:testdata/productParams.json")
    private Resource resource;
    
    @Override
    public List<ProductParam> generate(int numbers) {
        fillProductParam();
        List<ProductParam> idList = new ArrayList<>();
        for (int i = 0; i < numbers; i++) { 
            countParamOnProduct = MIN_COUNT_PARAM + random.nextInt(MAX_COUNT_PARAM - MIN_COUNT_PARAM);
            for(int j = 0; j < countParamOnProduct; j++){
                ProductParam productParam = generateObject();
                productParamDao.create(productParam);
                idList.add(productParam);
            }
        }
        return idList;
    }
    
    private void fillProductParam() {
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(resource.getInputStream());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String paramName = (String) person.get("paramName");
            String value = (String) person.get("value");
            paramNames.add(paramName);
            values.add(value);
        }
    }

    @Override
    public ProductParam generateObject() {
        ProductParam productParam = new RealProductParam();
        productParam.setProduct(getProduct());
        if(++counterParamName >= paramNames.size())
            counterParamName = 0;
        if(++counterValue >= values.size())
            counterValue = 0;        
        
        productParam.setParamName(paramNames.get(counterParamName));
        productParam.setValue(values.get(counterValue));
        
        return productParam;
    }
    
    private Product getProduct(){                
        if(counter++ >= countParamOnProduct){
            counter = 0;            
            actualProduct = products.remove(random.nextInt(products.size()));
        } 
        return actualProduct;
    }    
    
    public void setProducts(List<Product> products) {        
        this.products = products;        
        actualProduct = products.remove(random.nextInt(products.size()));
    }
}
