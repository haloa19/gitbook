package com.douzone.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.UserVo;

public class gitbookInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession httpSession = request.getSession(false);
		UserVo uservo =(UserVo)httpSession.getAttribute("authUser");
		if(uservo == null) {
			response.sendRedirect(request.getContextPath());
			return false ;
		}
	
		response.sendRedirect(request.getContextPath()+"/main");
	
		
		
		
		return false;
	}

}
