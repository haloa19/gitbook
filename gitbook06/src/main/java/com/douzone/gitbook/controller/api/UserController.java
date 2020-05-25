package com.douzone.gitbook.controller.api;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.GitService;
import com.douzone.gitbook.vo.UserVo;
import com.douzone.security.AuthUser;

@Controller("UserApiController")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private GitService userService;
	
	@ResponseBody
	@RequestMapping(value="/auth",method=RequestMethod.GET)
	public JsonResult checkEmail(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo =(UserVo)httpSession.getAttribute("authUser");
		
		System.out.println("확인:"+uservo);
		if(uservo==null) {
			return JsonResult.success(null);
		}
		
		return JsonResult.success(uservo);
	}
	
	
}
