package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.OrganizationDao;
import com.csipon.crm.domain.model.Organization;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class OrganizationProxy implements Organization {
    private long id;
    private Organization organization;
    private OrganizationDao organizationDao;

    public OrganizationProxy(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getOrganization().getName();
    }

    @Override
    public void setName(String name) {
        getOrganization().setName(name);
    }

    private Organization getOrganization() {
        if (organization == null) {
            organization = organizationDao.findById(id);
        }
        return organization;
    }
}