package com.csipon.crm.dao.impl.sql;

/**
 * Created by bpogo on 4/24/2017.
 */
public final class OrganizationSqlQuery {
    private OrganizationSqlQuery() {
    }

    public static final String PARAM_ORG_TABLE = "organization";
    public static final String PARAM_ORG_ID = "id";
    public static final String PARAM_ORG_NAME = "name";

    public static final String SQL_FIND_ORGANIZATION_BY_ID = "SELECT id, name FROM organization WHERE id = :id;";
    public static final String SQL_FIND_ORGANIZATION_BY_NAME = "SELECT id, name FROM organization WHERE name = :name;";

    public static final String SQL_UPDATE_ORGANIZATION = "UPDATE organization " +
            "SET name=:name " +
            "WHERE id=:id;";

    public static final String SQL_DELETE_ORGANIZATION = "DELETE FROM organization " +
            "WHERE id=:id;";

    public static final String SQL_FIND_ALL_ORGANIZATIONS = "" +
            "SELECT id, name FROM organization;";
}
