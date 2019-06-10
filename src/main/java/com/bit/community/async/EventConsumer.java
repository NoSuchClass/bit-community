package com.bit.community.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bit.community.utils.JedisAdapter;
import com.bit.community.utils.RedisKeyUtil;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
	private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);
	@Autowired
	JedisAdapter jedisAdapter;
	private Map<EventType, List<EventHandler>> config = new HashMap<>();
	private ApplicationContext application;

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, EventHandler> beans = application.getBeansOfType(EventHandler.class);
		if (beans != null) {
			for(Map.Entry<String, EventHandler> entry : beans.entrySet()) {
				List<EventType> eventTypes= entry.getValue().getSupportEventTypes();
				for(EventType type : eventTypes) {
					if (!config.containsKey(type)) {
						config.put(type, new ArrayList<EventHandler>());
					}
					config.get(type).add(entry.getValue());
				}
			}
		}
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					String key = RedisKeyUtil.getEventKey();
					List<String> events = jedisAdapter.brpop(0, key);
					if (events != null) {
						for(String message : events) {
							if (message.equals(key)) {
								continue;
							}
							EventModel eventModel = JSON.parseObject(message, EventModel.class);
							if (!config.containsKey(eventModel.getType())) {
								log.error("can not handle this event");
								continue;
							}
							
							for(EventHandler handler : config.get(eventModel.getType())) {
								log.info("do handle "+ eventModel.getType());
								handler.doHandle(eventModel);
							}
						}
					}
				}
				
			}
		});
		thread.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.application = applicationContext;
	}
}
