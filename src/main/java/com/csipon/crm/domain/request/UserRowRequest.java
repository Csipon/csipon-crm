package com.csipon.crm.domain.request;

/**
 * Created by bpogo on 5/8/2017.
 */
public class UserRowRequest extends RowRequest {
    private static final String BEGIN_SQL = "" +
            "SELECT u.id, email, password, phone, first_name, last_name, middle_name, " +
            "enable, account_non_locked, enable, user_role_id, role.name role_name, contact_person, " +
            "org_id, address_id, r.name, a.formatted_address, o.name " +
            "FROM users u " +
            "INNER JOIN user_roles role ON user_role_id = role.id " +
            "LEFT JOIN organization o ON u.org_id = o.id " +
            "LEFT JOIN address a ON u.address_id = a.id " +
            "LEFT JOIN region r ON a.region_id = r.id ";

    private static final String BEGIN_SQL_COUNT = "" +
            "SELECT count(*) " +
            "FROM users u " +
            "LEFT JOIN organization o ON u.org_id = o.id " +
            "LEFT JOIN address a ON u.address_id = a.id " +
            "LEFT JOIN region r ON a.region_id = r.id ";

    private static final String ORG_USERS_WHERE_SQL = "" +
            " org_id = (SELECT org_id " +
            "FROM users " +
            "WHERE id = :id)";

    private Long roleId;
    private Boolean accountNonLocked;
    private Boolean enable;
    private Boolean contactPerson;
    private Long customerId;

    UserRowRequest() {
        super(new String[]{
                "u.id",
                "first_name",
                "middle_name",
                "last_name",
                "email",
                "phone",
                "r.name",
                "contact_person",
                "a.formatted_address",
                "o.name",
                "enable"
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
        if (roleId != null) {
            appendWhere(sql);
            sql.append("user_role_id = :user_role_id ");
        }
        if (accountNonLocked != null) {
            appendWhere(sql);
            sql.append("account_non_locked = :account_non_locked ");
        }
        if (enable != null) {
            appendWhere(sql);
            sql.append("enable = :enable ");
        }
        if (contactPerson != null) {
            appendWhere(sql);
            sql.append("contact_person = :contact_person ");
        }
        return sql;
    }

    @Override
    protected StringBuilder appendWhereParam(StringBuilder sql) {
        if (customerId != null) {
            appendWhere(sql);
            sql.append(ORG_USERS_WHERE_SQL);
        }
        return sql;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Boolean contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
