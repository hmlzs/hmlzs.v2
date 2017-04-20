package com.core.repository;

import com.core.cache.CacheClient;
import com.core.config.Config;
import com.core.repository.idGenerator.IdGenerator;
import com.core.repository.sqlBuilder.SQLBuilder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.core.repository.BaseRepository.idRowMapper;

/**
 * Created by sun
 */
@Repository
public class SecurityRepository extends BaseRepository {

    protected Log logger = LogFactory.getLog(BaseRepository.class);

}
