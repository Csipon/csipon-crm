package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.domain.real.RealDiscount;
import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.model.Discount;
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
public class DiscountSetter extends AbstractSetter<Discount> {

    @Autowired
    private DiscountDao discountDao;
    private List<String> descs = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    @Value(value = "classpath:testdata/discount.json")
    private Resource resource;


    public List<Discount> generate(int numbers) {
        fillDiscount();
        List<Discount> idList = new ArrayList<>();
        for (int i = 0; i < numbers; i++) {
            Discount discount = generateObject();
            discountDao.create(discount);
            idList.add(discount);
        }
        return idList;
    }

    private void fillDiscount() {
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(resource.getInputStream());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String desc = (String) person.get("desc");
            String title = (String) person.get("title");
            descs.add(desc);
            titles.add(title);
        }
    }


    public Discount generateObject() {
        Discount discount = new RealDiscount();
        discount.setTitle(titles.get(random.nextInt(titles.size())));
        discount.setPercentage((double) Math.round(Math.random() * 100));
        discount.setDescription(descs.get(random.nextInt(descs.size())));
        discount.setActive(random.nextBoolean());
        return discount;
    }


}
