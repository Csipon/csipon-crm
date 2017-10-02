package com.csipon.crm.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.csipon.crm.dao.impl.sql.PersistentTokenSqlQuery.*;

/**
 * Created by Pasha on 23.04.2017.
 */
@Repository
public class PersistentTokenDaoImpl implements PersistentTokenRepository {
    private static final Logger log = LoggerFactory.getLogger(PersistentTokenDaoImpl.class);

    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private SimpleJdbcInsert insert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARAM_TOKEN_TABLE);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void createNewToken(PersistentRememberMeToken token) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_TOKEN_USERNAME, token.getUsername())
                .addValue(PARAM_TOKEN_SERIES, token.getSeries())
                .addValue(PARAM_TOKEN, token.getTokenValue())
                .addValue(PARAM_TOKEN_LAST_USED, token.getDate());
        insert.execute(params);
    }

    public void updateToken(String series, String tokenValue, Date lastUsed) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_TOKEN_SERIES, series)
                .addValue(PARAM_TOKEN, tokenValue)
                .addValue(PARAM_TOKEN_LAST_USED, lastUsed);
        namedJdbcTemplate.update(UPDATE_TOKEN, params);
    }

    public void removeUserTokens(String username) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_TOKEN_USERNAME, username);
        namedJdbcTemplate.update(REMOVE_USER_TOKEN, params);
    }

    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue(PARAM_TOKEN_SERIES, seriesId);
            return namedJdbcTemplate.query(TOKEN_BY_SERIES, params, new PersistentRememberMeTokenExtractor());
        } catch (EmptyResultDataAccessException emptyRs) {
            if (log.isDebugEnabled()) {
                log.debug("Querying token for series \'" + seriesId + "\' returned no results.", emptyRs);
            }
        } catch (IncorrectResultSizeDataAccessException incorrectRsSize) {
            log.error("Querying token for series \'" + seriesId + "\' returned more than one value. Series should be unique");
        } catch (DataAccessException dataAccess) {
            log.error("Failed to load token for series " + seriesId, dataAccess);
        }
        return null;
    }

    private static final class PersistentRememberMeTokenExtractor implements ResultSetExtractor<PersistentRememberMeToken> {
        @Override
        public PersistentRememberMeToken extractData(ResultSet rs) throws SQLException, DataAccessException {
            PersistentRememberMeToken rememberMeToken = null;
            while (rs.next()) {
                rememberMeToken = new PersistentRememberMeToken(
                        rs.getString(PARAM_TOKEN_USERNAME),
                        rs.getString(PARAM_TOKEN_SERIES),
                        rs.getString(PARAM_TOKEN),
                        rs.getTimestamp(PARAM_TOKEN_LAST_USED));
            }
            return rememberMeToken;
        }
    }
}
