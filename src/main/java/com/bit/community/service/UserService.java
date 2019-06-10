package com.bit.community.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bit.community.dao.LoginTicketDao;
import com.bit.community.dao.UserDao;
import com.bit.community.model.LoginTicket;
import com.bit.community.model.User;
import com.bit.community.utils.CommunityUtils;

@Service
public class UserService {
	@Autowired
	UserDao userDao;
	@Autowired
	LoginTicketDao loginTicketDao;

	public User getUserById(int id) {
		return userDao.selectById(id);
	}

	public Map<String, String> register(String username, String password) {
		Map<String, String> map = new HashMap<>();
		Random random = new Random();
		if (StringUtils.isEmpty(username)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if (StringUtils.isEmpty(password)) {
			map.put("msg", "密码不能为空");
		}

		User user = userDao.selectByName(username);
		if (user != null) {
			map.put("msg", "用户名已经被注册");
		} else {
			user = new User();
			user.setName(username);
			user.setSalt(UUID.randomUUID().toString().substring(0, 5));
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setPassword(CommunityUtils.MD5(password + user.getSalt()));
			userDao.addUser(user);
			map.put("ticket", addTicket(user.getId()));
		}
		return map;
	}

	public Map<String, String> login(String username, String password) {
		Map<String, String> map = new HashMap<>();
		User user = userDao.selectByName(username);
		if (user == null) {
			map.put("msg", "用户不存在，请注册");
			return map;
		}
		if (!user.getPassword().equals(CommunityUtils.MD5(password + user.getSalt()))) {
			map.put("msg", "密码错误");
		} else {
			map.put("ticket", addTicket(user.getId()));
		}
		return map;
	}
	
	public User getUserByName(String name) {
		return userDao.selectByName(name);
	}

	public String addTicket(int userId) {
		LoginTicket ticket = new LoginTicket();
		ticket.setUserId(userId);
		ticket.setStatus(0);
		Date now = new Date();
		now.setTime(24 * 60 * 60 * 7 + now.getTime());
		ticket.setExpired(now);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		loginTicketDao.addTicket(ticket);
		return ticket.getTicket();
	}

	public void changeStatus(String ticket) {
		loginTicketDao.updateStatus(ticket, 1);
	}
}
