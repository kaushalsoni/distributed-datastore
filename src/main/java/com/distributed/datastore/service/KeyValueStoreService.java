package com.distributed.datastore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distributed.datastore.dao.KeyValueStoreDao;
import com.distributed.datastore.entity.KeyValueStore;
import com.distributed.datastore.util.KeyValueUtil;

@Service
public class KeyValueStoreService implements IStoreService {

	@Autowired
	private KeyValueStoreDao keyValueStoreDao;

	private Logger logger = LoggerFactory.getLogger(KeyValueStoreService.class);

	public boolean put(String key, String value) {
		KeyValueUtil.checkNull("Key", key);
		logger.info("KeyValue.put -> key: " + key + ", value: " + value);

		// If the value is null, delete it
		try {
			keyValueStoreDao.put(key, value);
			logger.info("KeyValue.put -> Stored successfully");
			return true;
		} catch (Exception e) {
			logger.info("KeyValue.put -> Store operation failed");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String get(String key) {
		try {
			KeyValueStore keyValueStore = keyValueStoreDao.get(key);
			logger.info("KeyValue.get -> got record successfully");
			return keyValueStore == null ? null : keyValueStore.getValue();
		}  catch (Exception e) {
			logger.info("KeyValue.get -> error performing get operation");
			e.printStackTrace();
		}
		return null;
	}

}
