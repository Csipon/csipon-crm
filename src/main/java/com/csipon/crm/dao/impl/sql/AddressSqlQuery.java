package com.csipon.crm.dao.impl.sql;

/**
 * Created by bpogo on 4/24/2017.
 */
public final class AddressSqlQuery {
    private AddressSqlQuery() {
    }

    public static final String PARAM_ADDRESS_TABLE = "address";
    public static final String PARAM_ADDRESS_ID = "id";
    public static final String PARAM_ADDRESS_LATITUDE = "latitude";
    public static final String PARAM_ADDRESS_LONGITUDE = "longitude";
    public static final String PARAM_ADDRESS_FORMATTED_ADDRESS = "formatted_address";
    public static final String PARAM_ADDRESS_DETAILS = "details";
    public static final String PARAM_ADDRESS_REGION_ID = "region_id";

    public static final String SQL_FIND_ADDRESS_BY_ID = "" +
            "SELECT id , latitude, longitude, formatted_address, details, region_id " +
            "FROM address " +
            "WHERE id = :id;";
    public static final String SQL_UPDATE_ADDRESS = "UPDATE address " +
            "SET id=:id, latitude=:latitude, longitude=:longitude, region_id=:region_id, details=:details " +
            "WHERE id=:id;";

    public static final String SQL_DELETE_ADDRESS = "DELETE FROM address " +
            "WHERE id=:id;";

}
