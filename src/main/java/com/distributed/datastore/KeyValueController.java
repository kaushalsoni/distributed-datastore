package com.distributed.datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.distributed.datastore.service.KeyValueStoreService;
import com.distributed.datastore.service.ReplicationService;

@RestController
@EnableAsync
public class KeyValueController {
	
	@Autowired
	private KeyValueStoreService service;
	
	@Autowired
	private ReplicationService replicationClient;
	
	Logger logger = LoggerFactory.getLogger(KeyValueController.class);
	
	@PostMapping("/set/{key}")
	public void update(@PathVariable String key, @RequestParam(required = false) Boolean replication, @RequestBody String value) {
		service.put(key, value);
		//perform below replication asynchronously
		replicationClient.updateKeyValuePair(key, value, geCurrentlyRunningtInstanceURL(), replication);			
		logger.info("Instances up for this services are");
	}
	
	@GetMapping("/get/{key}")
	public String get(@PathVariable String key) {
		return service.get(key);
	}
	
	public String geCurrentlyRunningtInstanceURL() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	}
	
}
