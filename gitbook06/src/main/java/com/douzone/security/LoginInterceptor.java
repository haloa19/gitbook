package com.douzone.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.UserVo;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String id =request.getParameter("id");
		String password =request.getParameter("password");
		
		UserVo vo = new UserVo(); 
		
		vo.setId(id);
		vo.setPassword(password);
		
		UserVo authUser =  userService.getUser(vo);
		System.out.println(authUser);
		
		
		if(authUser == null) {
			request.setAttribute("userVo", vo);
			request
			.getRequestDispatcher("/WEB-INF/views/user/index.jsp")
			.forward(request, response);
			
			return false;
		}
		System.out.println("----------------> authUser:"+authUser);

		
		// session 처리 
		HttpSession session = request.getSession(true);
		session.setAttribute("authUser", authUser);
		//response.sendRedirect(request.getContextPath());
		request.getRequestDispatcher("/WEB-INF/views/main/main.jsp").forward(request, response);
		
		return false;
	}

}
