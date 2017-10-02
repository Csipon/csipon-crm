package com.csipon.crm.dao.impl;

import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.dao.GroupDao;
import com.csipon.crm.dao.RegionDao;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.dao.RegionGroupsDao;
import com.csipon.crm.domain.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.csipon.crm.dao.impl.sql.RegionGroupsSqlQuery.*;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 28.04.2017
 */

@Repository
public class RegionGroupsDaoImpl implements RegionGroupsDao {
    private static final Logger log = LoggerFactory.getLogger(RegionGroupsDao.class);

    private RegionDao regionDao;
    private GroupDao groupDao;
    private DiscountDao discountDao;

    private SimpleJdbcInsert simpleInsert;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private GroupDaoImpl.GroupExtractor groupExtractor;
    private RegionDaoImpl.RegionExtractor regionExtractor;

    @Autowired
    public RegionGroupsDaoImpl(RegionDao regionDao, GroupDao groupDao, DiscountDao discountDao) {
        this.regionDao = regionDao;
        this.groupDao = groupDao;
        this.discountDao = discountDao;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(PARAM_RG_TABLE)
                .usingGeneratedKeyColumns(PARAM_RG_ID);
        groupExtractor = new GroupDaoImpl.GroupExtractor(discountDao);
        regionExtractor = new RegionDaoImpl.RegionExtractor();
    }

    @Override
    public Long create(Long idRegion, Long idGroup) {
        if (idRegion == null || idGroup == null) {
            return null;
        }
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_RG_GROUP_ID, idGroup)
                .addValue(PARAM_RG_REGION_ID, idRegion);
        Long idRow = simpleInsert.executeAndReturnKey(params).longValue();
        log.info("Group with id " + idGroup + " added for region with id " + idRegion);
        return idRow;
    }

    @Override
    public Long create(Region region, Group group) {
        return create(getRegionId(region), getGroupId(group));
    }

    @Override
    public Long delete(Region region, Group group) {
        Long regionId = region.getId();
        Long groupId = group.getId();
        if (regionId == null || groupId == null) {
            return null;
        } else {
            return delete(regionId, groupId);
        }
    }

    @Override
    public Long delete(Long regionId, Long groupId) {
        if (regionId < 1 || groupId < 1) {
            return null;
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_RG_REGION_ID, regionId)
                .addValue(PARAM_RG_GROUP_ID, groupId);
        long deletedRows = namedJdbcTemplate.update(SQL_DELETE_RG, params);
        if (deletedRows == 0) {
            log.error("Row has not been deleted");
            return null;
        } else {
            log.info("Group with id " + groupId + " was deleted from Region " + regionId);
            return deletedRows;
        }
    }

    @Override
    public List<Group> findGroupsByRegionId(Long regionId) {
        log.debug("Start finding groups by region");
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_RG_REGION_ID, regionId);
        List<Group> groups = namedJdbcTemplate.query(SQL_FIND_GROUPS_BY_REGION, params, groupExtractor);
        log.debug("End finding groups by region");
        return groups;
    }

    @Override
    public List<Group> findGroupsByRegion(Region region) {
        return findGroupsByRegionId(region.getId());
    }

    @Override
    public List<Region> findRegionsByGroup(Group group) {
        log.debug("Start finding regions by group");
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_RG_GROUP_ID, group.getId());
        List<Region> regions = namedJdbcTemplate.query(SQL_FIND_REGIONS_BY_GROUP, params, regionExtractor);
        log.debug("End finding regions by group");
        return regions;
    }

    private Long getRegionId(Region region) {
        if (region != null) {
            return region.getId();
        }
        return null;
    }

    private Long getGroupId(Group group) {
        if (group != null) {
            return group.getId();
        }
        return null;
    }
}