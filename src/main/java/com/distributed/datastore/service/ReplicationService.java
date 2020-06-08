package com.distributed.datastore.service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReplicationService {
	@Autowired
    private DiscoveryClient discoveryClient;
	
	RestTemplate restTemplate = new RestTemplate();
	
	Logger logger = LoggerFactory.getLogger(ReplicationService.class);
	
	 @Async("threadPoolTaskExecutor")
	public void updateKeyValuePair(String key, String value,
			String currentlyRunningURL, Boolean replication) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		if(!Boolean.TRUE.equals(replication)) {
			for (URI baseUrl : serviceUrls()) {
				if (!baseUrl.toString().equals(currentlyRunningURL)) {
					String url = prepareUrlForReplication(baseUrl, key);
					ResponseEntity<String> response = restTemplate.postForEntity(url, value, String.class);
					logger.info("Response from call => " + response);
				}
			}
		}
	}
	 
	 public List<URI> serviceUrls() {
	        return discoveryClient.getInstances("distributed-datastore")
	          .stream()
	          .map(si -> si.getUri())
	          .collect(Collectors.toList());
	    }

	private String prepareUrlForReplication(URI baseUrl, String key) {
		StringBuilder builder = new StringBuilder(baseUrl.toString());
		builder.append("/set/");
		builder.append(key);
		builder.append("?replication=true");
		return builder.toString();
	}

	 @Bean("threadPoolTaskExecutor")
	    public TaskExecutor getAsyncExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(20);
	        executor.setMaxPoolSize(1000);
	        executor.setWaitForTasksToCompleteOnShutdown(true);
	        executor.setThreadNamePrefix("Async-");
	        return executor;
	    }
}
