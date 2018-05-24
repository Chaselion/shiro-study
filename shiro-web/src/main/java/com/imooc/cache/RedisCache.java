package com.imooc.cache;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.imooc.util.JedisUtil;
@Component
public class RedisCache<K,V> implements Cache<K, V> {
	@Resource
	private JedisUtil jedisUtil;
	private final String CACHE_PREFIX = "imooc-cache";
	
	private byte[] getKey(K k) {
		if(k instanceof String) {
			return (CACHE_PREFIX + k).getBytes();
		}
		return SerializationUtils.serialize(k);
	}
	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public V get(K k) throws CacheException {
		System.out.println("从redis获取数据");
		byte[] value = jedisUtil.get(getKey(k));
		if(value != null) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}

	public Set<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	public V put(K k, V v) throws CacheException {
		byte[] key = getKey(k);
		byte[] value = SerializationUtils.serialize(v);
		jedisUtil.set(key, value);
		jedisUtil.expire(key, 600);
		return null;
	}

	public V remove(K k) throws CacheException {
		byte[] key = getKey(k);
		byte[] value = jedisUtil.get(key);
		jedisUtil.del(key);
		if(value != null) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
