package com.bit.community.async.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.community.async.EventHandler;
import com.bit.community.async.EventModel;
import com.bit.community.async.EventType;
import com.bit.community.utils.MailSender;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class LoginExceptionHandler implements EventHandler{
	@Autowired
	MailSender mailSender;
	@Autowired
	Configuration configuration;
	@Override
	public void doHandle(EventModel model){
		Map<String, Object> map = new HashMap<>();
		map.put("username", model.getExt("username"));
		try {
			Template template = configuration.getTemplate("login_exception.html");
			mailSender.sendWithHTMLTemplate("343788619@qq.com", "登录IP异常",  template, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LOGIN);
	}
	
	
}
