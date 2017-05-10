package com.netcracker.crm.dao.impl.sql;

/**
 * @author YARUS
 */
public final class OrderSqlQuery {

    public static final String PARAM_ORDER_TABLE = "orders";
    public static final String PARAM_ORDER_ID = "id";
    public static final String PARAM_ORDER_DATE_FINISH = "date_finish";
    public static final String PARAM_ORDER_PREFERRED_DATE = "preferred_date";
    public static final String PARAM_ORDER_STATUS = "status_id";
    public static final String PARAM_CUSTOMER_ID = "customer_id";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_CSR_ID = "csr_id";

    public static final String PARAM_PATTERN = "pattern";

    public static final String PARAM_ORDER_ROW_STATUS = "status_id";
    public static final String PARAM_ORDER_ROW_PRODUCT_STATUS = "product_status_id";

    public static final String SQL_UPDATE_ORDER = "UPDATE orders "
            + "SET date_finish = :date_finish, preferred_date = :preferred_date, "
            + "status_id = :status_id, customer_id = :customer_id, product_id = :product_id, "
            + "csr_id = :csr_id WHERE id = :id;";

    public static final String SQL_DELETE_ORDER = "DELETE FROM orders WHERE id = :id;";

    public static final String SQL_FIND_ORDER_BY_ID = "SELECT id, date_finish, "
            + "preferred_date, status_id, customer_id, product_id, csr_id "
            + "FROM orders WHERE id = :id;";

    public static final String SQL_FIND_ALL_ORDER_BY_DATE_FINISH = "SELECT id, "
            + "date_finish, preferred_date, status_id, customer_id, product_id, "
            + "csr_id FROM orders WHERE date_trunc('day', date_finish) = :date_finish;";

    public static final String SQL_FIND_ALL_ORDER_BY_PREFERRED_DATE = "SELECT id, "
            + "date_finish, preferred_date, status_id, customer_id, product_id, "
            + "csr_id FROM orders WHERE date_trunc('day', preferred_date) = :preferred_date;";

    public static final String SQL_FIND_ALL_ORDER_BY_PRODUCT_ID = "SELECT id, "
            + "date_finish, preferred_date, status_id, customer_id, product_id, "
            + "csr_id FROM orders WHERE product_id = :product_id;";

    public static final String SQL_FIND_ALL_ORDER_BY_CUSTOMER_ID = "SELECT id, "
            + "date_finish, preferred_date, status_id, customer_id, product_id, "
            + "csr_id FROM orders WHERE customer_id = :customer_id "
            + "ORDER BY date_finish desc";

    public static final String SQL_FIND_ALL_ORDER_BY_CSR_ID = "SELECT id, "
            + "date_finish, preferred_date, status_id, customer_id, product_id, "
            + "csr_id FROM orders WHERE csr_id = :csr_id;";

    public static final String SQL_FIND_ORG_ORDERS_BY_CUSTOMER_ID = "SELECT id, date_finish, " +
            "preferred_date, status_id, customer_id, product_id, csr_id " +
            "FROM orders " +
            "WHERE customer_id IN (SELECT id " +
            "FROM users " +
            "WHERE org_id = (SELECT org_id " +
            "FROM users " +
            "WHERE id = :customer_id)) " +
            "ORDER BY date_finish desc";

    public static final String SQL_FIND_ORDER_BY_ID_OR_PRODUCT_TITLE = "SELECT o.id, date_finish, " +
            "preferred_date, o.status_id, customer_id, product_id, csr_id " +
            "FROM orders o " +
            "INNER JOIN product p ON o.product_id = p.id" +
            "WHERE concat(o.id, ' ', p.title) ILIKE :pattern " +
            "ORDER BY date_finish desc;";
}
