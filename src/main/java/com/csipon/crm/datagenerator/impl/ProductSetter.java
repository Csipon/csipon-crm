package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductStatus;
import com.csipon.crm.domain.real.RealProduct;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasha on 05.05.2017.
 */

@Service
public class ProductSetter extends AbstractSetter<Product> {

    private int defaultPrice = 10_000;

    @Autowired
    private ProductDao productDao;
    private List<String> prodName = new ArrayList<>();
    private List<Discount> discounts;
    private List<Group> groups;
    private int counter;
    @Value(value = "classpath:testdata/products.json")
    private Resource resource;

    @Override
    public List<Product> generate(int numbers) {
        fillProducts();
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < numbers; i++) {
            Product product = generateObject();
            productDao.create(product);
            products.add(product);
        }
        return products;
    }

    private void fillProducts(){
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(resource.getInputStream());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String service = (String) person.get("service");
            prodName.add(service);
        }
    }

    @Override
    public Product generateObject() {
        Product product = new RealProduct();
        product.setDiscount(getDiscount());
        product.setGroup(getGroup());
        product.setDefaultPrice((double) random.nextInt(defaultPrice));
        product.setTitle(prodName.get(counter++));
        product.setDescription("Products description");
        product.setStatus(ProductStatus.values()[random.nextInt(ProductStatus.values().length)]);
        return product;
    }


    private Group getGroup(){
        return Math.random() < 0.8 ? groups.get(random.nextInt(groups.size())) : null;
    }

    private Discount getDiscount(){
        return Math.random() > 0.5 ? discounts.remove(random.nextInt(discounts.size())) : null;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
