package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.UserDao;
import com.csipon.crm.dao.UserTokenDao;
import com.csipon.crm.domain.UserToken;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.proxy.UserProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.csipon.crm.dao.impl.sql.UserTokenSqlQuery.*;

/**
 * Created by Pasha on 02.05.2017.
 */
@Repository
public class UserTokenDaoImpl implements UserTokenDao {
    private static final Logger log = LoggerFactory.getLogger(UserTokenDaoImpl.class);

    private UserDao userDao;

    private SimpleJdbcInsert userTokenInsert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public UserTokenDaoImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.userTokenInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARAM_USER_TOKEN_TABLE)
                .usingGeneratedKeyColumns(PARAM_USER_TOKEN_ID);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public Long create(UserToken userToken) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_USER_TOKEN_ID, userToken.getId())
                .addValue(PARAM_USER_TOKEN_USED, userToken.isUsed())
                .addValue(PARAM_USER_TOKEN_USER_ID, userToken.getUser().getId())
                .addValue(PARAM_USER_TOKEN_TOKEN, userToken.getToken())
                .addValue(PARAM_USER_TOKEN_DATE_SEND, userToken.getSendDate());

        long id = userTokenInsert.executeAndReturnKey(params).longValue();
        log.info("UserToken with id: " + id + " is successfully created.");
        userToken.setId(id);
        return id;
    }

    @Override
    public boolean updateToken(String token, boolean used) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_USER_TOKEN_USED, used)
                .addValue(PARAM_USER_TOKEN_TOKEN, token);
        int updatedRows = namedJdbcTemplate.update(SQL_USER_REGISTER_TOKEN_UPDATE, params);
        if (updatedRows == 1) {
            log.info("User registered token was set to 'true' value.");
            return true;
        }
        return false;
    }

    @Override
    public UserToken getUserToken(String token) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_USER_TOKEN_TOKEN, token);

        return namedJdbcTemplate.query(SQL_FIND_USER_TOKEN_BY_TOKEN, params, new UserTokenWithDetailExtractor(userDao));
    }

    private static final class UserTokenWithDetailExtractor implements ResultSetExtractor<UserToken> {

        private UserDao userDao;

        public UserTokenWithDetailExtractor(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        public UserToken extractData(ResultSet rs) throws SQLException, DataAccessException {
            UserToken userToken = null;
            while (rs.next()) {
                userToken = new UserToken();
                userToken.setId(rs.getLong(PARAM_USER_TOKEN_ID));
                userToken.setSendDate(rs.getTimestamp(PARAM_USER_TOKEN_DATE_SEND).toLocalDateTime());
                userToken.setToken(rs.getString(PARAM_USER_TOKEN_TOKEN));
                userToken.setUsed(rs.getBoolean(PARAM_USER_TOKEN_USED));

                User user = new UserProxy(userDao);
                user.setId(rs.getLong(PARAM_USER_TOKEN_USER_ID));
                userToken.setUser(user);
            }
            return userToken;
        }
    }
}
