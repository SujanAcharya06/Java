
package com.example.cachingdemo.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CacheInspectionService {

	@Autowired
	private CacheManager cacheManger;

	public String printCacheContent(String cacheName) {
		Cache cache = cacheManger.getCache(cacheName);
		if (cache != null) {
			log.info("Cache Contents: {}", Objects.requireNonNull(cache.getNativeCache()).toString());
			return Objects.requireNonNull(cache.getNativeCache()).toString();
		} else {
			return "No such cache " + cacheName;
		}
	}

}
