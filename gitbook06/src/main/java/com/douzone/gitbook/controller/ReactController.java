package com.douzone.gitbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactController {
	
	@RequestMapping({"/main/**", "/my/**", "/group/**", "/mygroup/**", "/myfriend/**"})
	public String react() {
		
		return "main/main";
	}
	
}
