package com.imooc.cache;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager {
	@Resource
	private RedisCache redisCache;
	public <K, V> Cache<K, V> getCache(String s) throws CacheException {
		// TODO Auto-generated method stub
		return redisCache;
	}

}
