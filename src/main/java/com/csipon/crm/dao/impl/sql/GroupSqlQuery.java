package com.csipon.crm.dao.impl.sql;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */
public final class GroupSqlQuery {
    public static final String PARAM_GROUP_TABLE = "groups";
    public static final String PARAM_GROUP_ID = "id";
    public static final String PARAM_GROUP_NAME = "name";
    public static final String PARAM_GROUP_DISCOUNT_ID = "discount_id";
    public static final String PARAM_GROUP_CUSTOMER_ID = "customer_id";


    public static final String PARAM_GROUP_ROW_DISCOUNT_TITLE = "title";
    public static final String PARAM_GROUP_ROW_DISCOUNT_VALUE = "percentage";
    public static final String PARAM_GROUP_ROW_DISCOUNT_ACTIVE = "active";
    public static final String PARAM_GROUP_ROW_PRODUCT_COUNT = "products";

    //BULK
    public static final String PARAM_GROUP_IDS = "group_ids";

    public static final String PARAM_PATTERN = "pattern";

    public static final String SQL_UPDATE_GROUP = "UPDATE groups " +
            "SET name=:name, discount_id=:discount_id " +
            "WHERE id=:id;";

    public static final String SQL_DELETE_GROUP = "DELETE from groups " +
            "WHERE id=:id;";

    public static final String SQL_FIND_GROUP_BY_ID = "SELECT id, name, discount_id " +
            "FROM groups " +
            "WHERE id=:id;";

    public static final String SQL_FIND_GROUP_BY_NAME = "SELECT id, name, discount_id " +
            "FROM groups " +
            "WHERE UPPER(name) like UPPER(:name) " +
            "ORDER BY id;";

    public static final String SQL_FIND_GROUP_BY_ID_OR_TITLE = "SELECT id, name, discount_id " +
            "FROM groups " +
            "WHERE concat(id, ' ', name) ILIKE :pattern " +
            "ORDER BY id;";

    public static final String SQL_FIND_GROUP_BY_DISCOUNT_ID = "SELECT id, name, discount_id " +
            "FROM groups " +
            "WHERE discount_id = :discount_id " +
            "ORDER BY id;";

    public static final String SQL_FIND_GROUP_BY_DISCOUNT_ID_AND_CUSTOMER_ID = "SELECT g.id, g.name, g.discount_id " +
            "FROM groups g " +
            "INNER JOIN region_groups rg ON rg.group_id=g.id " +
            "INNER JOIN region r ON rg.region_id = r.id " +
            "INNER JOIN address a ON a.region_id = r.id " +
            "INNER JOIN users u ON u.address_id = a.id " +
            "WHERE g.discount_id = :discount_id " +
            "AND u.id = :customer_id " +
            "ORDER BY g.id; ";

    public static final String SQL_GET_GROUP_COUNT = "SELECT count(*) " +
            "FROM groups;";

    public static final String SQL_GROUP_BULK_UPDATE = "" +
            "SELECT update_groups(ARRAY [:group_ids ] :: BIGINT[], :discount_id);";
}


