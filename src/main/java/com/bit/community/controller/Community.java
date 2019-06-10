package com.bit.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Community {
	@RequestMapping("/666")
	public String test() {
		throw new RuntimeException("fuc");
	}
}
