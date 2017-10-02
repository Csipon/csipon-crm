package com.csipon.crm.dao.impl.sql;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */
public final class DiscountSqlQuery {
    public static final String PARAM_DISCOUNT_TABLE = "discount";
    public static final String PARAM_DISCOUNT_ID = "id";
    public static final String PARAM_DISCOUNT_TITLE = "title";
    public static final String PARAM_DISCOUNT_PERCENTAGE = "percentage";
    public static final String PARAM_DISCOUNT_DESCRIPTION = "description";
    public static final String PARAM_DISCOUNT_ACTIVE = "active";

    public static final String PARAM_PATTERN = "pattern";

    //BULK
    public static final String PARAM_DISCOUNT_IDS = "discount_ids";

    public static final String SQL_UPDATE_DISCOUNT = "UPDATE discount " +
            "SET title=:title, percentage=:percentage, description=:description, " +
            "active = :active " +
            "WHERE id=:id;";


    public static final String SQL_DELETE_DISCOUNT = "DELETE FROM discount " +
            "WHERE id=?;";

    public static final String SQL_FIND_DISC_BY_ID = "SELECT id, title, percentage, description, active " +
            "FROM discount " +
            "WHERE id=:id;";

    public static final String SQL_FIND_DISC_BY_TITLE = "SELECT id, title, percentage, description, active " +
            "FROM discount " +
            "WHERE UPPER(title) like UPPER(:title) " +
            "ORDER BY id;";

    public static final String SQL_FIND_DISC_BY_ID_OR_TITLE = "SELECT id, title, percentage, description, active " +
            "FROM discount " +
            "WHERE concat(id, ' ', title) ILIKE :pattern " +
            "ORDER BY id;";

    public static final String SQL_GET_DISC_COUNT = "SELECT count(*) " +
            "FROM discount;";

    public static final String SQL_DISCOUNT_BULK_UPDATE = "" +
            "SELECT update_discount(ARRAY [:discount_ids ] :: BIGINT[], :active, :percentage, :description);";
}
