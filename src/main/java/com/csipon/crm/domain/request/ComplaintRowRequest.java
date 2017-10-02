package com.csipon.crm.domain.request;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 06.05.2017
 */
public class ComplaintRowRequest extends RowRequest {

    private Long statusId;
    private Long orderStatusId;
    private Long productStatusId;
    private Long pmgId;
    private Long custId;
    private boolean isContactPerson;

    private static final String ORG_COMPLAINTS_WHERE_SQL = "c.customer_id IN (SELECT id " +
            "FROM users " +
            "WHERE org_id = (SELECT org_id " +
            "FROM users " +
            "WHERE id = :customer_id))";

    private static final String BEGIN_SQL = ""
            + "SELECT c.id, c.title, c.customer_id , c.order_id, p.title, c.pmg_id, c.date, c.message,  o.status_id order_status_id, p.status_id product_status_id, c.status_id status_id  "
            + "FROM complaint c "
            + "INNER JOIN orders o ON c.order_id = o.id "
            + "INNER JOIN product p ON o.product_id = p.id ";

    private static final String BEGIN_SQL_COUNT = "SELECT count(*) "
            + "FROM complaint c "
            + "INNER JOIN orders o ON c.order_id = o.id "
            + "INNER JOIN product p ON o.product_id = p.id ";


    @Override
    protected String beginSql() {
        return BEGIN_SQL;
    }

    @Override
    protected String beginSqlCount() {
        return BEGIN_SQL_COUNT;
    }

    public ComplaintRowRequest() {
        super(new String[]{
                "c.id",
                "c.title",
                "c.customer_id ",
                "c.order_id",
                "p.title",
                "c.pmg_id",
                "c.date",
        });
    }

    @Override
    protected StringBuilder appendWhereStatus(StringBuilder sql) {
        if (statusId != null) {
            appendWhere(sql);
            sql.append("c.status_id = :status_id ");
        }
        if (orderStatusId != null) {
            appendWhere(sql);
            sql.append("o.status_id = :order_status_id ");
        }
        if (productStatusId != null) {
            appendWhere(sql);
            sql.append("p.status_id = :product_status_id ");
        }
        return sql;
    }

    @Override
    protected StringBuilder appendWhereParam(StringBuilder sql) {
        if (pmgId != null) {
            appendWhere(sql);
            sql.append("c.pmg_id = :pmg_id ");
        }
        if (custId != null) {
            if(isContactPerson){
                appendWhere(sql);
                sql.append(ORG_COMPLAINTS_WHERE_SQL);
            } else {
                appendWhere(sql);
                sql.append("c.customer_id = :customer_id ");
            }
        }
        return sql;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Long orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public Long getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(Long productStatusId) {
        this.productStatusId = productStatusId;
    }


    public Long getPmgId() {
        return pmgId;
    }

    public void setPmgId(Long pmgId) {
        this.pmgId = pmgId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Boolean getContactPerson() {
        return isContactPerson;
    }

    public void setContactPerson(Boolean contactPerson) {
        isContactPerson = contactPerson;
    }
}
