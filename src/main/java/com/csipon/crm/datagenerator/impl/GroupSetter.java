package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.dao.GroupDao;
import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.real.RealGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasha on 05.05.2017.
 */
@Service
public class GroupSetter extends AbstractSetter<Group> {

    @Autowired
    private GroupDao groupDao;
    private int counter;
    private List<Discount> discounts;
    private String[] groupNames = {
            "Triple-buffered",
            "fresh-thinking",
            "hybrid",
            "optimal",
            "Fully-configurable",
            "5th generation",
            "Up-sized",
            "Networked",
            "multimedia",
            "logistical",
    };

    @Override
    public List<Group> generate(int numbers) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < numbers; i++) {
            Group group = generateObject();
            groupDao.create(group);
            groups.add(group);
        }
        return groups;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    @Override
    public Group generateObject() {
        Group group = new RealGroup();
        group.setName(groupNames[counter++]);
        group.setDiscount(getDiscount());
        return group;
    }


    private Discount getDiscount() {
        return Math.random() > 0.5 ? discounts.remove(random.nextInt(discounts.size())) : null;
    }
}
