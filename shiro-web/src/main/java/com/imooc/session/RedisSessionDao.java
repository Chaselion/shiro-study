package com.imooc.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import com.imooc.util.JedisUtil;
@Service
public class RedisSessionDao extends AbstractSessionDAO {
	
	@Resource
	private JedisUtil jedisUtil;
	private final String SHIRO_SESSION_PREFIX = "imooc-session";
	
	private byte[] getkey(String key) {
		return (SHIRO_SESSION_PREFIX + key).getBytes();
	}
	//保存session方法
	private void saveSession(Session session) {
		if(session != null && session.getId() != null) {
			byte[] key = getkey(session.getId().toString());
			byte[] value = SerializationUtils.serialize(session);
			jedisUtil.set(key,value);
			jedisUtil.expire(key,600);
		}
	}
	public void update(Session session) throws UnknownSessionException {
		saveSession(session);
	}

	public void delete(Session session) {
		if(session == null || session.getId() == null) {
			return;
		}
		byte[] key = getkey(session.getId().toString());
		jedisUtil.del(key);
	}

	public Collection<Session> getActiveSessions() {
		Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
		Set<Session> sessions = new HashSet<Session>();
		if(CollectionUtils.isEmpty(keys)) {
			return sessions;
		}
		for (byte[] key : keys) {
			Session session = (Session)SerializationUtils.deserialize(key);
			sessions.add(session);
		}
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		System.out.println("read session");
		if(sessionId == null) {
			return null;
		}
		byte[] key = getkey(sessionId.toString());
		byte[] value =jedisUtil.get(key);
		return (Session) SerializationUtils.deserialize(value);
	}

}
