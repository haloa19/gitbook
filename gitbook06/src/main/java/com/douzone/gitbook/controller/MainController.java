package com.douzone.gitbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping({"/","/user"})
	public String index() {
		
		return "user/index";
	}
}
