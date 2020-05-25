package com.douzone.gitbook.controller.api;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.UserVo;
import com.douzone.security.AuthUser;

@Controller("UserApiController")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
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
	
	@ResponseBody
	@RequestMapping(value="/friend",method=RequestMethod.GET)
	public JsonResult findFriend(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo =(UserVo)httpSession.getAttribute("authUser");
		
		if(uservo==null) {
			return JsonResult.success(null);
		}

		List<UserVo> friendList = userService.getFriend(uservo);
		return JsonResult.success(friendList);	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend",method=RequestMethod.POST)
	public JsonResult friendInfo(@RequestBody String userId) {

		UserVo friendvo = userService.getUserFriend(userId);
		return JsonResult.success(friendvo);
		
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/list",method=RequestMethod.POST)
	public JsonResult friendList(@RequestBody String userId) {
		UserVo uservo = new UserVo();
		uservo.setId(userId);
	
		List<UserVo> friendList = userService.getFriend(uservo);
		return JsonResult.success(friendList);	
	}
}
