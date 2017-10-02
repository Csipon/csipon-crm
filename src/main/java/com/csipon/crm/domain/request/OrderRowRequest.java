package com.csipon.crm.domain.request;

/**
 * @author Karpunets
 * @since 03.05.2017
 */
public class OrderRowRequest extends RowRequest {

    private Integer statusId;
    private Integer productStatusId;
    private Long customerId;
    private boolean isContactPerson;

    private static final String BEGIN_SQL = ""
            + "SELECT o.id, product_id, p.title, customer_id, csr_id, date_finish, preferred_date, o.status_id "
            + "FROM orders o "
            + "INNER JOIN product p ON o.product_id = p.id ";
    private static final String BEGIN_SQL_COUNT = "SELECT count(*) "
            + "FROM orders o "
            + "INNER JOIN product p ON o.product_id = p.id ";

    private static final String ORG_ORDERS_WHERE_SQL = "" +
            " customer_id in (SELECT id " +
            "FROM users " +
            "WHERE org_id = (SELECT org_id " +
            "FROM users " +
            "WHERE id = :customer_id))";

    public OrderRowRequest() {
        super(new String[]{
                "o.id",
                "product_id",
                "p.title",
                "customer_id",
                "csr_id",
                "date_finish",
                "preferred_date"
        });
    }


    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(Integer productStatusId) {
        this.productStatusId = productStatusId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    @Override
    protected String beginSql() {
        return BEGIN_SQL;
    }

    @Override
    protected String beginSqlCount() {
        return BEGIN_SQL_COUNT;
    }

    @Override
    protected StringBuilder appendWhereStatus(StringBuilder sql) {
        if (statusId != null) {
            appendWhere(sql);
            sql.append("o.status_id = :status_id ");
        }
        if (productStatusId != null) {
            appendWhere(sql);
            sql.append("p.status_id = :product_status_id ");
        }
        return sql;
    }

    @Override
    protected StringBuilder appendWhereParam(StringBuilder sql) {
        if (customerId != null) {
            appendWhere(sql);
            if(isContactPerson){
                sql.append(ORG_ORDERS_WHERE_SQL);
            } else {
                sql.append("customer_id = :customer_id ");
            }
        }
        return sql;
    }

    public void setIsContactPerson(boolean isContactPerson) {
        this.isContactPerson = isContactPerson;
    }
}
