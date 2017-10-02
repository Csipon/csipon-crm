package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.dao.RegionDao;
import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.domain.real.RealRegion;
import com.csipon.crm.domain.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pasha on 06.05.2017.
 */
@Service
public class RegionSetter extends AbstractSetter<Region> {

    @Autowired
    private RegionDao regionDao;
    private String[] regionsName = {
                    "Vinnyts'ka oblast" ,
                    "Volyns'ka oblast" ,
                    "Dnipropetrovsk Oblast" ,
                    "Donetsk Oblast" ,
                    "Zhytomyrs'ka oblast" ,
                    "Zakarpats'ka oblast" ,
                    "Zaporiz'ka oblast" ,
                    "Ivano-Frankivs'ka oblast" ,
                    "Kyiv city" ,
                    "Kirovohrads'ka oblast" ,
                    "Luhans'ka oblast" ,
                    "Lviv Oblast" ,
                    "Mykolaivs'ka oblast" ,
                    "Odessa Oblast" ,
                    "Poltavs'ka oblast" ,
                    "Rivnens'ka oblast" ,
                    "Sums'ka oblast" ,
                    "Ternopil's'ka oblast" ,
                    "Kharkiv Oblast" ,
                    "Khersons'ka oblast" ,
                    "Khmel'nyts'ka oblast" ,
                    "Cherkas'ka oblast" ,
                    "Chernivets'ka oblast" ,
                    "Chernihivs'ka oblast" ,
                    "Sevastopol' city"
    };

    @Override
    public List<Region> generate(int numbers) {
        List<Region> regions = new ArrayList<>();

        for (String aRegionsName : regionsName) {
            Region region = generateObject();
            region.setName(aRegionsName);
            regionDao.create(region);
            regions.add(region);
        }
        return regions;
    }

    @Override
    public Region generateObject() {
        return new RealRegion();
    }
}
