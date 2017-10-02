package com.csipon.crm.domain.request;

/**
 * Created by bpogo on 5/9/2017.
 */
public class DiscountRowRequest extends RowRequest {
    private static final String BEGIN_SQL = "" +
            "SELECT id, title, percentage, active, description " +
            "FROM discount ";

    private static final String BEGIN_SQL_COUNT = "" +
            "SELECT count(*) FROM discount ";
    private Boolean active;


    DiscountRowRequest() {
        super(new String[]{
                "id",
                "title",
                "percentage",
                "active",
                "description"
        });
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
        if (active != null) {
            appendWhere(sql);
            sql.append("active = :active ");
        }
        return sql;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }
}
