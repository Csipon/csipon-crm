package com.csipon.crm.dao.impl.sql;

/**
 * Created by bpogo on 4/22/2017.
 */
public final class UserSqlQuery {
    private UserSqlQuery() {
    }

    public static final String PARAM_USER_TABLE = "users";
    public static final String PARAM_USER_ID = "id";
    public static final String PARAM_USER_EMAIL = "email";
    public static final String PARAM_USER_PHONE = "phone";
    public static final String PARAM_USER_PASSWORD = "password";
    public static final String PARAM_USER_FIRST_NAME = "first_name";
    public static final String PARAM_USER_LAST_NAME = "last_name";
    public static final String PARAM_USER_MIDDLE_NAME = "middle_name";
    public static final String PARAM_USER_IS_ENABLE = "enable";
    public static final String PARAM_USER_CONTACT_PERSON = "contact_person";
    public static final String PARAM_USER_ACCOUNT_NON_LOCKED = "account_non_locked";

    public static final String PARAM_USER_ROLE_ID = "user_role_id";
    public static final String PARAM_USER_ROLE_NAME = "role_name";

    public static final String PARAM_USER_ORG_ID = "org_id";
    public static final String PARAM_USER_ADDRESS_ID = "address_id";

    public static final String PARAM_PATTERN = "pattern";

    public static final String SQL_FIND_USER_BY_EMAIL = "" +
            "SELECT u.id, email, password, phone, first_name, last_name, middle_name, " +
            "enable, account_non_locked, user_role_id, role.name role_name, contact_person, " +
            "org_id, address_id " +
            "FROM users u " +
            "INNER JOIN user_roles role ON user_role_id = role.id " +
            "WHERE email = :email;";

    public static final String SQL_FIND_USER_BY_ID = "" +
            "SELECT u.id, email, password, phone, first_name, last_name, middle_name, " +
            "enable, account_non_locked, user_role_id, role.name role_name, contact_person, " +
            "org_id, address_id " +
            "FROM users u " +
            "INNER JOIN user_roles role ON user_role_id = role.id " +
            "WHERE u.id = :id;";

    public static final String SQL_UPDATE_USER = "UPDATE users " +
            "SET first_name=:first_name, middle_name=:middle_name, last_name=:last_name, " +
            "phone=:phone, email=:email, enable=:enable, account_non_locked=:account_non_locked, " +
            "contact_person=:contact_person, address_id=:address_id, user_role_id=:user_role_id, org_id=:org_id " +
            "WHERE id=:id;";

    public static final String SQL_UPDATE_PASSWORD = "UPDATE users " +
            "SET password=:password " +
            "WHERE id=:id;";

    public static final String SQL_DELETE_USER = "DELETE FROM users WHERE id = :id;";

    public static final String SQL_USERS_UPDATE_LOCKED = "UPDATE users " +
            "SET account_non_locked = :account_non_locked " +
            "WHERE email = :email;";
    public static final String SQL_USERS_COUNT = "SELECT count(*) " +
            "FROM users " +
            "WHERE email = :email;";
    public static final String SQL_USERS_UPDATE_PASSWORD = "UPDATE users " +
            "SET password = :password " +
            "WHERE email = :email;";
    public static final String SQL_FIND_USER_BY_PATTERN = "" +
            "SELECT u.id, email, password, phone, first_name, last_name, middle_name, " +
            "enable, account_non_locked, user_role_id, role.name role_name, contact_person, " +
            "org_id, address_id " +
            "FROM users u " +
            "INNER JOIN user_roles role ON user_role_id = role.id " +
            "WHERE concat(u.id, ' ', first_name, ' ', last_name) ILIKE :pattern " +
            "LIMIT 20;";

    public static final String SQL_FIND_ORG_USER_BY_PATTERN = "" +
            "SELECT u.id, email, password, phone, first_name, last_name, middle_name, " +
            "enable, account_non_locked, user_role_id, role.name role_name, contact_person, " +
            "org_id, address_id " +
            "FROM users u " +
            "INNER JOIN user_roles role ON user_role_id = role.id " +
            "WHERE concat(u.id, ' ', first_name, ' ', last_name) ILIKE :pattern and u.id in( " +
            "SELECT id FROM users WHERE org_id = ( " +
            "SELECT org_id FROM users WHERE id=:id)) " +
            "LIMIT 20;";
}
