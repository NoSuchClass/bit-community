package com.bit.community.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit.community.async.EventModel;
import com.bit.community.async.EventProducer;
import com.bit.community.async.EventType;
import com.bit.community.service.UserService;

@Controller
public class LoginController {
	@Autowired
	UserService userService;

	@Autowired
	EventProducer eventProducer;

	@RequestMapping(value = { "/reg" }, method = RequestMethod.POST)
	public String reg(Model model, @RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "next", required = false) String next, HttpServletResponse response) {
		Map<String, String> resMap = userService.register(username, password);
		if (resMap.containsKey("ticket")) {
			Cookie cookie = new Cookie("ticket", resMap.get("ticket"));
			cookie.setPath("/");
			response.addCookie(cookie);
			if (!StringUtils.isEmpty(next)) {
				return "redirect:" + next;
			}
			return "redirect:/";
		} else {
			model.addAttribute("msg", resMap.get("msg"));
			return "login";
		}
	}

	@RequestMapping(path = { "/reglogin" }, method = { RequestMethod.GET })
	public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
		model.addAttribute("next", next);
		return "login";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public String login(Model model, @RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "next", required = false) String next,
			@RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
			HttpServletResponse response) {
		Map<String, String> resMap = userService.login(username, password);
		if (resMap.containsKey("ticket")) {
			model.addAttribute("username", username);
			Cookie cookie = new Cookie("ticket", resMap.get("ticket"));
			cookie.setPath("/");
			if (rememberme) {
				cookie.setMaxAge(3600*24*5);
			}
			response.addCookie(cookie);
			
			eventProducer.fireEvent(new EventModel(EventType.LOGIN).setExt("username", username).setExt("email", "343788619@qq.com").setActorId(333));

			if (!StringUtils.isEmpty(next)) {
				return "redirect:" + next;
			}
			return "redirect:/index";
		} else {
			model.addAttribute("msg", resMap.get("msg"));
			return "login";
		}
	}

	@RequestMapping("/toLogin")
	public String login(Model model, HttpServletRequest request) {
		return "login";
	}

	@RequestMapping("/logout")
	public String logout(@CookieValue("ticket") String ticket) {
		userService.changeStatus(ticket);
		return "login";
	}
}
