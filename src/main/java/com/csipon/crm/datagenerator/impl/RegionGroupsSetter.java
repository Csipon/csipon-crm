package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dao.RegionGroupsDao;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pasha on 06.05.2017.
 */
@Service
public class RegionGroupsSetter extends AbstractSetter {
    private List<Region> regions;
    private List<Group> groups;
    private Map<String, List<Product>> productInRegion;
    private Map<String, List<Group>> regionGroup;


    @Autowired
    private RegionGroupsDao regionGroupsDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List generate(int numbers) {
        regionGroup = new HashMap<>();
        for (Region region : regions){
            List<Group> groupList = new ArrayList<>();
            for (Group group : getRegionGroups(groups)){
                regionGroupsDao.create(region, group);
                groupList.add(group);
            }
            regionGroup.put(region.getName(), groupList);
        }
        generateProductListInRegion();
        return new ArrayList();
    }


    private List<Group> getRegionGroups(List<Group> groups){
        List<Group> groupList = new ArrayList<>();
        for (Group group : groups){
            if (Math.random() > 0.6){
                groupList.add(group);
            }
        }
        return groupList;
    }

    @Override
    public Object generateObject() {
        return null;
    }


    private void generateProductListInRegion(){
        productInRegion = new HashMap<>();
        for (Map.Entry<String, List<Group>> map : regionGroup.entrySet()){
            productInRegion.put(map.getKey(), getProductList(map.getValue()));
        }
    }

    private List<Product> getProductList(List<Group> list){
        List<Product> result =  new ArrayList<>();
        for (Group g : list){
            result.addAll(productDao.findAllByGroupId(g.getId()));
        }
        return result;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Map<String, List<Product>> getProductInRegion() {
        return productInRegion;
    }
}
