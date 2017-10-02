package com.csipon.crm.domain.request;

/**
 * Created by Pasha on 09.05.2017.
 */
public class GroupRowRequest extends RowRequest {

    private Boolean discountActive;

    public GroupRowRequest() {
        super(new String[]{
                "g.id",
                "g.name",
                "products",
                "d.title",
                "d.percentage",
                "d.active"
        });
    }

    private static final String BEGIN_SQL = "" +
            "SELECT g.id, g.name, (SELECT count(*) FROM product p WHERE p.group_id = g.id) products" +
            ", d.title, d.percentage, d.active " +
            "FROM groups g " +
            "LEFT JOIN discount d ON g.discount_id = d.id";

    private static final String BEGIN_SQL_COUNT = "" +
            "SELECT count(*) " +
            "FROM groups g " +
            "LEFT JOIN discount d ON g.discount_id = d.id";

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
        if (discountActive != null) {
            appendWhere(sql);
            sql.append("d.active = :active ");
        }
        return sql;
    }


    public Boolean getDiscountActive() {
        return discountActive;
    }

    public void setDiscountActive(Boolean discountActive) {
        this.discountActive = discountActive;
    }
}
