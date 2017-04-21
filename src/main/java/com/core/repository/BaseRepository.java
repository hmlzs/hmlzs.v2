package com.core.repository;

import com.core.cache.CacheClient;
import com.core.config.Config;
import com.core.repository.idGenerator.IdGenerator;
import com.core.repository.sqlBuilder.SQLBuilder;
import com.core.util.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sun
 */
@Repository
public class BaseRepository {
    protected Log logger = LogFactory.getLog(BaseRepository.class);

    @Autowired
    protected CacheClient cacheClient;

    @Autowired
    protected Config config;

    @Autowired
    protected NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    protected IdGenerator idGenerator;

    @Autowired
    protected SQLBuilder sqlBuilder;

    public static RowMapper<Long> idRowMapper = new RowMapper<Long>() {
        @Override
        public Long mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getLong(1);
        }
    };

    /**
     *
     * @param object
     * @return
     * @throws Exception
     */
    public long create(Object object) throws Exception {

        long id = 0;

        String appName = config.getDbName();
        id = sqlBuilder.getIdFromObject(object);

        if (id == 0) {
            id = idGenerator.getNextId(object.getClass().getSimpleName().toLowerCase(), appName);
        }
        // 更新
        jdbcTemplate.update(sqlBuilder.getSQLCreate(object.getClass(), id), new BeanPropertySqlParameterSource(object));
        idGenerator.updateKeyGen(object.getClass().getSimpleName().toLowerCase(), id);
        return id;
    }

    /**
     * list
     * @param type
     * @param sql
     * @param param
     * @param <T>
     * @return
     */
    public <T> List<T> list(Class<T> type, String sql, Map<String, Object> param) {
        List<T> result = new ArrayList<T>();
        String sqlString = "";
        sqlString = sqlBuilder.getSQLList(type, sql);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        if (MapUtils.isNotEmpty(param)) {
            mapSqlParameterSource.addValues(param);
        }
        List<Long> idList = jdbcTemplate.query(sqlString, mapSqlParameterSource, idRowMapper);
        for (Long id : idList) {
            result.add(find(type, id));
        }
        return result;
    }

    public <T> T find(Class<T> type, Long id) {

        T obj = null;
        obj = findFromCache(id, type);
        if (obj == null) {
            obj = findFromDb(id, type);
            putToCache(obj, type, id);
        }
        return obj;
    }

    public int update(Object object) throws Exception {
        String sql = sqlBuilder.getSQLUpdate(object.getClass());
        deleteFromCache(sqlBuilder.getIdFromObject(object), object.getClass());

        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(object));
    }

    public <T> int delete(Class<T> type, long id) {
        String appName = config.getDbName();

        Map<String, String> params = new HashMap<String, String>();

        params.put("id", String.valueOf(id));

        cacheClient.delete(appName + "-" + type.getSimpleName() + "-" + id);

        return jdbcTemplate.update(sqlBuilder.getSQLDelete(type), params);
    }

    public <T> int count(Class<T> type, String sql, Map<String, Object> param) {
        return jdbcTemplate.queryForObject(sqlBuilder.getCountSQL(type, sql), param, Integer.class);
    }

    public <T> List<T> pageList(Class<T> type, String sql, Map<String, Object> param, int pageSize, int pageNum) {
        return list(type, sqlBuilder.getPageSQL(sql, pageSize, pageNum), param);
    }

    public <T> boolean deleteFromCache(long id, Class<T> type) {
        return cacheClient.delete(config.getDbName() + "-" + type.getSimpleName() + "-" + id);
    }

    public <T> T findFromCache(long id, Class<T> type) {
        String key = config.getDbName() + "-" + type.getSimpleName() + "-" + id;
        String v = (String) cacheClient.get(key);
        if (v != null) {
            return JsonUtil.fromJson(v, type);
        } else {
        }
        return null;
    }

    public <T> T findFromDb(long id, Class<T> type) {
        String sql = sqlBuilder.getSQLQuery(type);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(id));
        return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<T>(type));
    }

    public <T> void putToCache(T obj, Class<T> type, long id) {
        String key = config.getDbName() + "-" + type.getSimpleName() + "-" + id;
        cacheClient.set(key, JsonUtil.toJson(obj));
    }
}
