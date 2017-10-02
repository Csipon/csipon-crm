package com.csipon.crm.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import java.sql.SQLException;

/**
 * Created by bpogo on 5/2/2017.
 */
public class PostgreSQLErrorCodesTranslator extends SQLErrorCodeSQLExceptionTranslator {
    @Override
    protected DataAccessException customTranslate(String task, String sql, SQLException sqlEx) {
        //TODO: implement ErrorCodesTranslator for DAO layer
        return null;
    }
}
