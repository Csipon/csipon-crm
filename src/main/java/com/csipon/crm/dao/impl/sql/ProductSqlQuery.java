package com.csipon.crm.dao.impl.sql;

/**
 * Created by Karpunets on 4/29/2017.
 */
public final class ProductSqlQuery {
    public static final String PARAM_PRODUCT_TABLE = "product";
    public static final String PARAM_PRODUCT_ID = "id";
    public static final String PARAM_PRODUCT_TITLE = "title";
    public static final String PARAM_PRODUCT_DEFAULT_PRICE = "default_price";
    public static final String PARAM_PRODUCT_STATUS_ID = "status_id";
    public static final String PARAM_PRODUCT_DESCRIPTION = "description";
    public static final String PARAM_PRODUCT_DISCOUNT_ID = "discount_id";
    public static final String PARAM_PRODUCT_GROUP_ID = "group_id";

    public static final String PARAM_PRODUCT_CUSTOMER_ID = "customer_id";
    public static final String PARAM_PRODUCT_REGION_ID = "region_id";

    public static final String PARAM_PRODUCT_ROW_STATUS = "status_id";
    public static final String PARAM_PRODUCT_ROW_PRODUCT_DISCOUNT_ACTIVE = "product_discount_active";
    public static final String PARAM_PRODUCT_ROW_GROUP_DISCOUNT_ACTIVE = "group_discount_active";

    //BULK
    public static final String PARAM_PRODUCT_IDS = "product_ids";

    public static final String PARAM_PATTERN = "pattern";

    public static final String SQL_UPDATE_PRODUCT = "UPDATE product "
            + "SET title = :title, default_price = :default_price, "
            + "status_id = :status_id, description = :description, "
            + "discount_id = :discount_id, group_id = :group_id WHERE id = :id;";

    public static final String SQL_FIND_PRODUCT_BY_ID = ""
            + "SELECT id, title, default_price,"
            + "status_id, description, discount_id, group_id "
            + "FROM product "
            + "WHERE id = :id;";

    public static final String SQL_FIND_PRODUCT_BY_TITLE = ""
            + "SELECT id, title, default_price,"
            + "status_id, description, discount_id, group_id "
            + "FROM product "
            + "WHERE title = :title;";

    public static final String SQL_FIND_ALL_PRODUCT_BY_GROUP_ID = ""
            + "SELECT id, title, default_price,"
            + "status_id, description, discount_id, group_id "
            + "FROM product "
            + "WHERE group_id = :group_id;";

    public static final String SQL_FIND_PRODUCT_WITHOUT_GROUP_BY_ID_OR_TITLE = ""
            + "SELECT id, title, default_price,"
            + "status_id, description, discount_id, group_id "
            + "FROM product "
            + "WHERE group_id IS null AND concat(id, ' ', title) ILIKE :pattern;";

    public static final String SQL_FIND_ALL_PRODUCT_BY_ID_OR_TITLE = ""
            + "SELECT id, title, default_price,"
            + "status_id, description, discount_id, group_id "
            + "FROM product "
            + "WHERE concat(id, ' ', title) ILIKE :pattern;";


    public static final String SQL_FIND_ACTUAL_PRODUCT_BY_ID_OR_TITLE_AND_CUSTOMER_ID = ""
            + "SELECT p.id, title, default_price,"
            + "status_id, description, discount_id, group_id "
            + "FROM product p "
            + "INNER JOIN statuses s ON p.status_id = s.id "
            + "WHERE p.id IN ( "
            + " SELECT product_id "
            + " FROM orders"
            + " WHERE customer_id = :customer_id "
            + ") AND concat(p.id, ' ', title) ILIKE :pattern "
            + "AND s.name = 'ACTUAL';";

    public static final String SQL_FIND_POSSIBLE_PRODUCT_BY_ID_OR_TITLE_AND_CUSTOMER_ID = ""
            + "SELECT p.id, title, default_price,"
            + "status_id, description, p.discount_id, group_id "
            + "FROM product p "
            + "INNER JOIN statuses s ON p.status_id = s.id "
            + "LEFT JOIN groups g ON p.group_id = g.id "
            + "WHERE group_id IN ( "
            + " SELECT group_id "
            + " FROM region_groups "
            + " WHERE region_id = :region_id "
            + ") AND s.name = 'ACTUAL' "
            + "AND p.id NOT IN ( "
            + " SELECT product_id "
            + " FROM orders "
            + " WHERE customer_id = :customer_id"
            + ") AND concat(p.id, ' ', title) ILIKE :pattern;";

    public static final String SQL_HAS_CUSTOMER_ACCESS_TO_PRODUCT = ""
            + "SELECT CAST(COUNT(p.id) AS BIT) "
            + "FROM product p "
            + "INNER JOIN statuses s ON p.status_id = s.id "
            + "INNER JOIN region_groups rg ON p.group_id = rg.group_id "
            + "INNER JOIN address a ON a.region_id = rg.region_id "
            + "INNER JOIN users u ON u.address_id = a.id "
            + "WHERE u.id = :customer_id AND p.id = :id AND (s.name = 'ACTUAL' OR s.name = 'OUTDATED');";

    public static final String SQL_DELETE_PRODUCT = "DELETE FROM product WHERE id = :id;";

    public static final String SQL_GROUP_PRODUCTS_BY_STATUS = "" +
            "SELECT count(result.status_count) " +
            "FROM " +
            "(SELECT count(p.*) status_count " +
            "FROM product p " +
            "WHERE p.id IN ( :product_ids ) " +
            "GROUP BY p.status_id ) AS result;";

    public static final String SQL_PRODUCT_BULK_UPDATE = "" +
            "SELECT update_product(ARRAY [:product_ids ] :: BIGINT[], :discount_id, :group_id, :default_price, :description);";

    public static final String SQL_FIND_PRODUCTS_BY_DISCOUNT_ID = "SELECT id, title, " +
            "default_price, status_id, description, discount_id, group_id " +
            "FROM product " +
            "WHERE discount_id = :discount_id " +
            "ORDER BY id;";

    public static final String SQL_FIND_PRODUCTS_BY_DISCOUNT_ID_AND_CUSTOMER_ID = "SELECT p.id, title, default_price, status_id, description, p.discount_id, group_id " +
            "FROM product p " +
            "LEFT JOIN groups g ON p.group_id = g.id " +
            "INNER JOIN statuses s ON s.id=p.status_id " +
            "WHERE p.discount_id = :discount_id " +
            "AND (group_id is NULL " +
            "OR group_id IN (SELECT group_id " +
            "FROM region_groups rg " +
            "INNER JOIN region r ON rg.region_id = r.id " +
            "INNER JOIN address a ON a.region_id = r.id " +
            "INNER JOIN users u ON u.address_id = a.id " +
            "WHERE u.id = :customer_id)) " +
            "AND s.name = 'ACTUAL' " +
            "ORDER BY id;";
}
