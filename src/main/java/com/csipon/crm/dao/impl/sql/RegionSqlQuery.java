package com.csipon.crm.dao.impl.sql;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */
public final class RegionSqlQuery {
    public static final String PARAM_REGION_TABLE = "region";
    public static final String PARAM_REGION_ID = "id";
    public static final String PARAM_REGION_NAME = "name";

    public static final String PARAM_PATTERN = "pattern";

    public static final String SQL_UPDATE_REGION = "UPDATE region " +
            "SET name=:name " +
            "WHERE id=:id;";

    public static final String SQL_DELETE_REGION = "DELETE FROM region " +
            "WHERE id=:id;";

    public static final String SQL_FIND_REGION_BY_ID = "SELECT id, name " +
            "FROM region " +
            "WHERE id=:id;";

    public static final String SQL_FIND_REGION_BY_NAME = "SELECT id, name " +
            "FROM region " +
            "WHERE UPPER(name) like UPPER(:name) " +
            "ORDER BY id;";

    public static final String SQL_GET_REGION_COUNT = "SELECT count(*) " +
            "FROM region;";

    public static final String SQL_FIND_REGION_BY_ID_OR_NAME = "SELECT id, name " +
            "FROM region " +
            "WHERE concat(id, ' ', name) ILIKE :pattern " +
            "ORDER BY id;";
}
