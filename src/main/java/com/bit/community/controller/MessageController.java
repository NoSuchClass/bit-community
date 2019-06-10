package com.bit.community.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.community.model.HostHolder;
import com.bit.community.model.Message;
import com.bit.community.model.User;
import com.bit.community.model.ViewObject;
import com.bit.community.service.MessageService;
import com.bit.community.service.UserService;
import com.bit.community.utils.CommunityUtils;

@Controller
public class MessageController {
	@Autowired
	MessageService messageService;
	@Autowired
	HostHolder hostHolder;
	@Autowired
	UserService userService;
	Logger log = LoggerFactory.getLogger(MessageController.class);

	@RequestMapping(path = "/msg/addMessage", method = { RequestMethod.POST })
	@ResponseBody
	public String addMessage(@RequestParam("toName") String toName, @RequestParam("content") String content,
			Model model) {
		try {
			User fromUser = hostHolder.getUser();
			User toUser = userService.getUserByName(toName);
			if (fromUser == null)
				return CommunityUtils.getJSON(999, "未登录");
			if (toUser == null)
				return CommunityUtils.getJSON(1, "用户不存在");
			if (fromUser.getId() == toUser.getId())
				return CommunityUtils.getJSON(1, "不能给自己发送消息");

			Message message = new Message();
			message.setCreatedDate(new Date());
			message.setFromId(fromUser.getId());
			message.setToId(toUser.getId());
			message.setContent(content);
			messageService.addMessage(message);
			return CommunityUtils.getJSON(0);
		} catch (Exception e) {
			log.error("发送信息失败： " + e.getMessage());
			return CommunityUtils.getJSON(1, "发送信息失败");
		}
	}

	@RequestMapping(value = "/msg/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public String listMessage(Model model, @RequestParam("conversationId") String conversationId) {
		try {
			List<Message> messageList = messageService.getConverstionDetail(conversationId, 0, 10);
			List<ViewObject> messages = new ArrayList<ViewObject>();
			for (Message message : messageList) {
				ViewObject vObject = new ViewObject();
				vObject.set("message", message);
				vObject.set("user", userService.getUserById(message.getFromId()));
				messageService.changeReadStutes(message.getConversationId());
				messages.add(vObject);
			}
			model.addAttribute("messages", messages);
			return "letterDetail";
		} catch (Exception e) {
			log.error(e.getMessage());
			return "/";
		}
	}

	@RequestMapping(value = "/msg/list", method = { RequestMethod.GET })
	public String getConversationList(Model model) {
		User user = hostHolder.getUser();
		if (user == null) {
			return "login";
		}
		List<Message> messageList = messageService.getConversationList(hostHolder.getUser().getId(), 0, 10);
		List<ViewObject> conversations = new ArrayList<>();
		for (Message message : messageList) {
			ViewObject viewObject = new ViewObject();
			int targetId = message.getFromId() == user.getId() ? message.getToId() : message.getFromId();
			viewObject.set("message", message);
			viewObject.set("user", userService.getUserById(targetId));
			viewObject.set("count", messageService.getConversationCount(user.getId(), message.getConversationId()));
			conversations.add(viewObject);
		}
		model.addAttribute("conversations", conversations);
		return "letter";
	}
}
