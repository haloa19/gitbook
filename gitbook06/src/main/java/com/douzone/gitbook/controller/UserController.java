package com.douzone.gitbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping(value="/auth",method= RequestMethod.POST)
	public void auth() {
		
	}
	
	@RequestMapping("/findID")
	public String findID() {
		
		return "user/findID";
	}
	
	@RequestMapping("/findPW")
	public String findPW() {
		
		return "user/findPW";
	}
	
	@RequestMapping("/join")
	public String join() {
		
		return "user/join";
	}
	
	@RequestMapping("/findIDSuccess")
	public String findIDSuccess() {
		
		return "user/findIDSuccess";
	}
	@RequestMapping("/findPWAuth")
	public String findPWAuth() {
		
		return "user/findPWAuth";
	}
	
	@RequestMapping("/findPWChange")
	public String findPWChange() {
		
		return "user/findPWChange";
	}
	
	
}
