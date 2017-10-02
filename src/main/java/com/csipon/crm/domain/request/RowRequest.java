package com.csipon.crm.domain.request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Karpunets
 * @since 05.05.2017
 */
public abstract class RowRequest {

    public static final String PARAM_KEYWORD = "keyword";
    public static final String PARAM_ROW_LIMIT = "row_limit";
    public static final String PARAM_ROW_OFFSET = "row_offset";
    private static final Pattern PATTERN_KEYWORDS = Pattern.compile("^([0-9]{1,2}):(.+)$");
    private static final String END_SQL = " LIMIT :row_limit OFFSET :row_offset;";

    private Integer orderBy;
    private Boolean desc;
    private String keywords;
    private Long rowLimit;
    private Long rowOffset;

    private String[] columns;
    private String[] keywordsArray;
    private boolean appendedWhere;

    RowRequest(String[] columns) {
        this.columns = columns;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getRowLimit() {
        return rowLimit;
    }

    public void setRowLimit(Long rowLimit) {
        this.rowLimit = rowLimit;
    }

    public Long getRowOffset() {
        return rowOffset;
    }

    public void setRowOffset(Long rowOffset) {
        this.rowOffset = rowOffset;
    }

    public String[] getKeywordsArray() {
        return keywordsArray;
    }

    public String getSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(beginSql());
        appendWhereKeywords(sql);
        appendWhereParam(sql);
        appendWhereStatus(sql);
        sql.append(" ORDER BY ").append(orderBy);
        if (desc) {
            sql.append(" DESC");
        } else {
            sql.append(" ASC");
        }
        sql.append(END_SQL);
        appendedWhere = false;
        return sql.toString();
    }

    public String getSqlCount() {
        StringBuilder sql = new StringBuilder();
        sql.append(beginSqlCount());
        appendWhereKeywords(sql);
        appendWhereParam(sql);
        appendWhereStatus(sql);
        appendedWhere = false;
        return sql.toString();
    }

    protected abstract String beginSql();

    protected abstract String beginSqlCount();

    protected StringBuilder appendWhereParam(StringBuilder sql) {
        return sql;
    }

    protected StringBuilder appendWhereStatus(StringBuilder sql) {
        return sql;
    }

    StringBuilder appendWhere(StringBuilder sql) {
        if (appendedWhere) {
            sql.append(" AND ");
        } else {
            sql.append(" WHERE ");
            appendedWhere = true;
        }
        return sql;
    }

    private StringBuilder appendWhereKeywords(StringBuilder sql) {
        if (!this.keywords.isEmpty()) {
            String[] keywords = this.keywords.split("\n");
            keywordsArray = new String[keywords.length];
            sql.append(" WHERE ");
            appendedWhere = true;
            for (int numKeywords = 0; numKeywords < keywords.length; numKeywords++) {
                Matcher m = PATTERN_KEYWORDS.matcher(keywords[numKeywords]);
                if (m.matches()) {
                    int numColumns = Integer.parseInt(m.group(1)) - 1;
                    keywordsArray[numKeywords] = m.group(2);
                    sql.append("cast(").append(columns[numColumns])
                            .append(" as text) ILIKE :").append(PARAM_KEYWORD).append(numKeywords);
                } else {
                    for (int i = 0; i < columns.length; i++) {
                        if (i == 0) {
                            sql.append(" ( ");
                        } else {
                            sql.append(" OR ");
                        }
                        sql.append("cast(").append(columns[i])
                                .append(" as text) ILIKE :").append(PARAM_KEYWORD).append(numKeywords);
                    }
                    sql.append(")");
                    keywordsArray[numKeywords] = keywords[numKeywords];
                }
                if (numKeywords != keywords.length - 1) {
                    sql.append(" AND ");
                }
            }
        }
        return sql;
    }


}
