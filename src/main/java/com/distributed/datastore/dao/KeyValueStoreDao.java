package com.distributed.datastore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.distributed.datastore.entity.KeyValueStore;

@Repository
@Transactional
public class KeyValueStoreDao {

	private final static String insertSql = "INSERT INTO key_value_store (store_key, store_value, created_at, updated_at) VALUES (?, ?, ?, ?)";

	private final static String updateQuery = "update key_value_store set store_value = ?, updated_at = ? where store_key = ?";
	
	private final static String getQuery = "select * from key_value_store where store_key=?";
	
	Logger logger = LoggerFactory.getLogger(KeyValueStoreDao.class);
	

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void put(String key, String value) {
		KeyValueStore keyValueStore = get(key);
		if (keyValueStore == null) {
			Date created_at = new Date();
			Object[] params = new Object[] { key, value, created_at, created_at };

			int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP };
			logger.info("Inserting rows into key_value_store table");
			jdbcTemplate.update(insertSql, params, types);
		} else {
			logger.info("Updating key_value_store table");
			jdbcTemplate.update(updateQuery, value, new Date(), key);
		}

	}

	public KeyValueStore get(String key) {
		List<KeyValueStore> keyValueStoreList = jdbcTemplate.query(getQuery, new Object[] { key },
				new KeyValueStoreRowMapper());
		return keyValueStoreList.size() > 0 ? keyValueStoreList.get(0) : null;
	}
	
	class KeyValueStoreRowMapper implements RowMapper< KeyValueStore > {
	    @Override
	    public KeyValueStore mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	KeyValueStore keyValueStore = new KeyValueStore();
	        keyValueStore.setKey(rs.getString("store_key"));
	        keyValueStore.setValue(rs.getString("store_value"));
	        keyValueStore.setCreated_at(rs.getDate("created_at"));
	        keyValueStore.setCreated_at(rs.getDate("updated_at"));
	        return keyValueStore;
	    }
	}

}
