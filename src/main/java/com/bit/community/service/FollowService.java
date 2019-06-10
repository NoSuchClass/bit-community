package com.bit.community.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.community.utils.JedisAdapter;
import com.bit.community.utils.RedisKeyUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@Service
public class FollowService {
	@Autowired
	JedisAdapter jedisAdapter;

	public boolean follow(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityId);
		Date date = new Date();
		Jedis jedis = jedisAdapter.getJedis();
		Transaction tx = jedisAdapter.multi(jedis);
		tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
		System.out.println(followerKey+ date.getTime()+ String.valueOf(userId));
		tx.zadd(followeeKey, date.getTime(), String.valueOf(userId));
		List<Object> ret = jedisAdapter.exec(tx, jedis);
		return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
	}

	public boolean unfollow(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityId);
		Jedis jedis = jedisAdapter.getJedis();
		Transaction tx = jedisAdapter.multi(jedis);
		tx.zrem(followerKey, String.valueOf(userId));
		tx.zrem(followeeKey, String.valueOf(userId));
		List<Object> ret = jedisAdapter.exec(tx, jedis);
		return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
	}

	private List<Integer> getIdsFromSet(Set<String> idset){
		List<Integer> ids = new ArrayList<>();
		for (String string : idset) {
			ids.add(Integer.parseInt(string));
		}
		return ids;
	}
	
	public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, count));
	}
	
	public List<Integer> getFollowees(int entityType, int entityId, int offset, int count) {
		String followerKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
		return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, count));
	}
	
	public long getFolloweeCount(int entityType, int entityId) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
		return jedisAdapter.zcard(followeeKey);
	}
	
	public long getFollowerCount(int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisAdapter.zcard(followerKey);
	}
	
	public boolean isFollower(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
		return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
	}
}


