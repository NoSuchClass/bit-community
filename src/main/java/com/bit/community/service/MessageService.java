package com.bit.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.community.dao.MessageDao;
import com.bit.community.model.Message;

@Service
public class MessageService {
	@Autowired
	MessageDao messageDao;
	@Autowired
	WordFilterService wordFilterService;

	public int addMessage(Message message) {
		message.setContent(wordFilterService.filter(message.getContent()));
		return messageDao.insertMessage(message) > 0 ? message.getId() : 0;
	}

	public List<Message> getConverstionDetail(String conversationId, int offset, int limit) {
		return messageDao.getMessageDetail(conversationId, offset, limit);
	}
	
	public List<Message> getConversationList(int userId, int offset, int limit){
		return messageDao.getMessageByUserId(userId, offset, limit);
	}
	
	public int getConversationCount(int userId, String conversationId) {
		return messageDao.getMessageUnreadCount(userId, conversationId);
	}
	
	public int changeReadStutes(String conversationId) {
		return messageDao.changeReadStatus(conversationId);
	}
}
