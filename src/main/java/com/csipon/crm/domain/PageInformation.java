package com.csipon.crm.domain;

import com.csipon.crm.domain.model.UserRole;

import java.util.Set;

/**
 * @author Karpunets
 * @since 21.04.2017
 */
public class PageInformation {

    private String title;
    private String icon;
    private String href;
    private Set<UserRole> roles;

    public PageInformation() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
}
