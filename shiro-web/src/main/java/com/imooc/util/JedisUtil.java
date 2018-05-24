package com.imooc.util;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {
	@Resource
	private JedisPool jedisPool;
	
	private Jedis getResources() {
		return jedisPool.getResource();
	}
	public byte[] set(byte[] key, byte[] value) {
		Jedis jedis = getResources();
		try {
			jedis.set(key, value);
			return value;
		} finally {
			jedis.close();
		}
	}

	public void expire(byte[] key, int i) {
		Jedis jedis = getResources();
		try {
			jedis.expire(key, i);
		} finally {
			jedis.close();
		}
	}
	public byte[] get(byte[] key) {
		Jedis jedis = getResources();
		try {
			return jedis.get(key);
		} finally {
			jedis.close();
		}
	}
	public void del(byte[] key) {
		Jedis jedis = getResources();
		try {
			jedis.del(key);
		} finally {
			jedis.close();
		}
		
	}
	public Set<byte[]> keys(String sHIRO_SESSION_PREFIX) {
		Jedis jedis = getResources();
		try {
			return jedis.keys((sHIRO_SESSION_PREFIX + "*").getBytes());
		} finally {
			jedis.close();
		}
	}
}
