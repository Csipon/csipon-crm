package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.dao.AddressDao;
import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.Region;
import com.csipon.crm.domain.real.RealAddress;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pasha on 06.05.2017.
 */
@Service
public class AddressSetter extends AbstractSetter<Address> {

    @Autowired
    private AddressDao addressDao;
    private Map<String, Double[]> coordinates;
    private List<Region> regions;
    private List<String> addresses = new ArrayList<>();
    @Value(value = "classpath:testdata/address.json")
    private Resource resource;

    public AddressSetter() {
        coordinates = new HashMap<>();
        coordinates.put("Vinnyts'ka oblast", new Double[]{49.2222442, 28.2201498});
        coordinates.put("Volyns'ka oblast", new Double[]{51.1228564, 23.7340557});
        coordinates.put("Dnipropetrovsk Oblast", new Double[]{48.4622135, 34.8602725});
        coordinates.put("Donetsk Oblast", new Double[]{48.0090787, 37.7266495});
        coordinates.put("Zhytomyrs'ka oblast", new Double[]{50.2616343, 28.6571026});
        coordinates.put("Zakarpats'ka oblast", new Double[]{48.4198761, 22.6893599});
        coordinates.put("Zaporiz'ka oblast", new Double[]{47.8373371, 35.156266});
        coordinates.put("Ivano-Frankivs'ka oblast", new Double[]{48.9210355, 24.7107286});
        coordinates.put("Kyiv city", new Double[]{50.4422959, 30.510855});
        coordinates.put("Kirovohrads'ka oblast", new Double[]{48.5069023, 32.2186981});
        coordinates.put("Luhans'ka oblast", new Double[]{48.5741215, 39.3127339});
        coordinates.put("Lviv Oblast", new Double[]{49.8385912, 23.9826908});
        coordinates.put("Mykolaivs'ka oblast", new Double[]{46.9473476, 32.0079053});
        coordinates.put("Odessa Oblast", new Double[]{46.4378669, 30.6660367});
        coordinates.put("Poltavs'ka oblast", new Double[]{49.5921452, 34.5350945});
        coordinates.put("Rivnens'ka oblast", new Double[]{50.6217245, 26.2380531});
        coordinates.put("Sums'ka oblast", new Double[]{50.9070345, 34.8017295});
        coordinates.put("Ternopil's'ka oblast", new Double[]{49.5548673, 25.5853203});
        coordinates.put("Kharkiv Oblast", new Double[]{49.9834121, 36.2187939});
        coordinates.put("Khersons'ka oblast", new Double[]{46.6529746, 32.6190003});
        coordinates.put("Khmel'nyts'ka oblast", new Double[]{49.4109281, 26.9832122});
        coordinates.put("Cherkas'ka oblast", new Double[]{49.424199, 32.0412613});
        coordinates.put("Chernivets'ka oblast", new Double[]{48.322176, 25.9169583});
        coordinates.put("Chernihivs'ka oblast", new Double[]{51.50123, 31.2689603});
        coordinates.put("Sevastopol' city", new Double[]{44.589539, 33.6549923});
    }

    @Override
    public List<Address> generate(int numbers) {
        List<Address> addresses = new ArrayList<>();
        fillAddress();
        for (int i = 0; i < numbers; i++) {
            Address address = generateObject();
            addressDao.create(address);
            addresses.add(address);
        }
        return addresses;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    private void fillAddress() {
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(resource.getInputStream());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String address = (String) person.get("address");
            addresses.add(address);
        }
    }

    @Override
    public Address generateObject() {
        Address address = new RealAddress();
        String addr = addresses.get(random.nextInt(addresses.size()));
        address.setDetails(addr);
        address.setRegion(getRegion());
        address.setLatitude(coordinates.get(address.getRegion().getName())[0]);
        address.setLongitude(coordinates.get(address.getRegion().getName())[1]);
        address.setFormattedAddress(addr);
        return address;
    }


    private Region getRegion() {
        return regions.get(random.nextInt(regions.size()));
    }
}
