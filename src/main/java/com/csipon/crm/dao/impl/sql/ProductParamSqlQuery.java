package com.csipon.crm.dao.impl.sql;

/**
 *
 * @author YARUS
 */
public class ProductParamSqlQuery {
    public static final String PARAM_PRODUCT_PARAM_TABLE = "product_param";
    public static final String PARAM_PRODUCT_PARAM_ID = "id";
    public static final String PARAM_PRODUCT_PARAM_NAME = "param_name";
    public static final String PARAM_PRODUCT_PARAM_VALUE = "value";    
    public static final String PARAM_PRODUCT_PARAM_PRODUCT_ID = "product_id";
    
    public static final String SQL_UPDATE_PRODUCT_PARAM = ""
            + "UPDATE product_param "
            + "SET param_name = :param_name, value = :value, "
            + "product_id = :product_id WHERE id = :id;";

    public static final String SQL_FIND_PRODUCT_PARAM_BY_ID = ""
            + "SELECT id, param_name, value, product_id "            
            + "FROM product_param "
            + "WHERE id = :id;";

    public static final String SQL_FIND_PRODUCT_PARAM_BY_NAME = ""
            + "SELECT id, param_name, value, product_id "            
            + "FROM product_param "
            + "WHERE param_name = :param_name;";

    public static final String SQL_FIND_PRODUCT_PARAMS_BY_PRODUCT_ID = ""
            + "SELECT id, param_name, value, product_id "           
            + "FROM product_param "
            + "WHERE product_id = :product_id "
            + "ORDER BY id;";

    public static final String SQL_FIND_PARAM_BY_ID_OR_NAME = "SELECT DISTINCT ON (param_name) id, param_name, value, product_id " +
            "FROM product_param " +
            "WHERE concat(id, ' ', param_name) ILIKE :pattern " +
            "ORDER BY param_name, id;";

    public static final String SQL_DELETE_PRODUCT_PARAM = "DELETE FROM product_param WHERE id = :id;";
    
}
