package com.csipon.crm.dao.impl.sql;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 28.04.2017
 */

public final class RegionGroupsSqlQuery {
    public static final String PARAM_RG_TABLE = "region_groups";
    public static final String PARAM_RG_ID = "id";
    public static final String PARAM_RG_GROUP_ID = "group_id";
    public static final String PARAM_RG_REGION_ID = "region_id";

    public static final String SQL_DELETE_RG = "DELETE FROM region_groups " +
            "WHERE region_id=:region_id AND group_id=:group_id";

    public static final String SQL_FIND_REGIONS_BY_GROUP = "SELECT r.id id, r.name, r.discount_id " +
            "FROM region_groups rg " +
            "INNER JOIN region r " +
            "ON rg.region_id = r.id " +
            "WHERE group_id=:group_id;";

    public static final String SQL_FIND_GROUPS_BY_REGION = "SELECT g.id id, g.name, g.discount_id " +
            "FROM region_groups rg " +
            "INNER JOIN groups g " +
            "ON rg.group_id = g.id " +
            "WHERE region_id=:region_id;";
}
