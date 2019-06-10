package com.bit.community.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bit.community.async.EventHandler;
import com.bit.community.async.EventModel;
import com.bit.community.async.EventType;
import com.bit.community.model.Message;
import com.bit.community.model.User;
import com.bit.community.service.MessageService;
import com.bit.community.service.UserService;
import com.bit.community.utils.CommunityUtils;

@Component
public class LikeHandler implements EventHandler {
	@Autowired
	UserService userSerivce;
	@Autowired
	MessageService messageService;

	@Override
	public void doHandle(EventModel model) {
		Message message = new Message();
		message.setFromId(CommunityUtils.SYSTEM_USERID);
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		User user = userSerivce.getUserById(model.getActorId());
		message.setContent("用户" + user.getName()+"赞了你的评论，<b><a href = http://127.0.0.1:8080/question/"
				+ model.getExts().get("questionId")+">点击查看</a></b>");
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LIKE);
	}

}
