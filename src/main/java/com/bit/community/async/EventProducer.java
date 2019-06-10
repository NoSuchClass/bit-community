package com.bit.community.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bit.community.utils.JedisAdapter;
import com.bit.community.utils.RedisKeyUtil;

@Service
public class EventProducer {
	@Autowired
	JedisAdapter jedisAdapter;
	public boolean fireEvent(EventModel eventModel) {
		try {
			String json = JSONObject.toJSONString(eventModel);
			String key = RedisKeyUtil.getEventKey();
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}