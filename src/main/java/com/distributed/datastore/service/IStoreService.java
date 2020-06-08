package com.distributed.datastore.service;

public interface IStoreService {
	/**
	   * Put a single entry to storage
	   *
	   * @param key   the name of entry to put
	   * @param value the value of entry
	   * @param <T>   type of value of entry
	   *
	   * @return true if entry added successfully, otherwise false
	   */
	  boolean put(String key, String value);

	  /**
	   * Get single entry from storage
	   *
	   * @param key the name of entry to get
	   * @param <T> type of value of entry
	   *
	   * @return the object related to given key
	   */
	  String get(String key);
}
