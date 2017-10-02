package com.csipon.crm.datagenerator.impl;

import com.csipon.crm.datagenerator.AbstractSetter;
import com.csipon.crm.dao.OrganizationDao;
import com.csipon.crm.domain.model.Organization;
import com.csipon.crm.domain.real.RealOrganization;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by Pasha on 05.05.2017.
 */
@Service
public class OrganizationSetter extends AbstractSetter<Organization> {
    @Autowired
    private OrganizationDao organizationDao;
    private Set<String> orgNames = new HashSet<>();
    private Iterator<String> iterator;
    @Value(value = "classpath:testdata/organization.json")
    private Resource resource;

    @Override
    public List<Organization> generate(int numbers) {
        fillOrganizations();
        List<Organization> organizations = new ArrayList<>();

        for (int i = 0; i < numbers; i++) {
            Organization organization = generateObject();
            organizationDao.create(organization);
            organizations.add(organization);
        }
        return organizations;
    }

    @Override
    public Organization generateObject() {
        Organization organization = new RealOrganization();
        organization.setName(iterator.next());
        return organization;
    }


    private void fillOrganizations() {
        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(resource.getInputStream());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String org = (String) person.get("organization");
            orgNames.add(org);
        }
        iterator = orgNames.iterator();
    }
}
