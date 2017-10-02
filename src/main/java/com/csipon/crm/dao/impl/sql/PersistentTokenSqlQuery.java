package com.csipon.crm.dao.impl.sql;

/**
 * Created by Pasha on 23.04.2017.
 */
public final class PersistentTokenSqlQuery {
    private PersistentTokenSqlQuery() {
    }

    public static final String PARAM_TOKEN_TABLE= "persistent_logins";
    public static final String PARAM_TOKEN_USERNAME = "username";
    public static final String PARAM_TOKEN_SERIES = "series";
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_TOKEN_LAST_USED = "last_used";

    public static final String TOKEN_BY_SERIES = "SELECT username,series,token,last_used " +
            "FROM persistent_logins " +
            "WHERE series = :series;";
    public static final String UPDATE_TOKEN = "UPDATE persistent_logins " +
            "SET token = :token, last_used = :last_used " +
            "WHERE series = :series;";
    public static final String REMOVE_USER_TOKEN = "DELETE FROM persistent_logins " +
            "WHERE username = :username;";
}
