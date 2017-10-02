package com.csipon.crm.dao.impl.sql;

/**
 * Created by Pasha on 02.05.2017.
 */
public class UserTokenSqlQuery {
    private UserTokenSqlQuery() {
    }

    public static final String PARAM_USER_TOKEN_TABLE = "user_register_token";
    public static final String PARAM_USER_TOKEN_ID = "id";
    public static final String PARAM_USER_TOKEN_TOKEN = "token";
    public static final String PARAM_USER_TOKEN_DATE_SEND = "date_send";
    public static final String PARAM_USER_TOKEN_USED = "used";
    public static final String PARAM_USER_TOKEN_USER_ID = "user_id";

    public static final String SQL_FIND_USER_TOKEN_BY_TOKEN  = "" +
            "SELECT id, user_id, token, date_send, used " +
            "FROM user_register_token " +
            "WHERE token = :token;";
    public static final String SQL_USER_REGISTER_TOKEN_UPDATE = "" +
            "UPDATE user_register_token " +
            "SET used=:used " +
            "WHERE token=:token;";
}
