package com.bit.community.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bit.community.async.EventHandler;
import com.bit.community.async.EventModel;
import com.bit.community.async.EventType;
import com.bit.community.model.EntityType;
import com.bit.community.model.Message;
import com.bit.community.model.User;
import com.bit.community.service.MessageService;
import com.bit.community.service.UserService;
import com.bit.community.utils.CommunityUtils;

@Component
public class FollowHandle implements EventHandler{
	@Autowired
	MessageService messageService;
	@Autowired
	UserService userService;
	@Override
	public void doHandle(EventModel model) {
		Message message = new Message();
		message.setFromId(CommunityUtils.SYSTEM_USERID);
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		User user = userService.getUserById(model.getActorId());
		if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
			message.setContent("用户" + user.getName()+"关注了你的问题，http://127.0.0.1:8080/question/"
					+ model.getEntityId());
		}else if (model.getEntityType() == EntityType.ENTITY_USER) {
			message.setContent("用户" + user.getName()+"关注了你，http://127.0.0.1:8080/user/"
					+ model.getActorId());
		}
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.FOLLOW);
	}
	
	
}
