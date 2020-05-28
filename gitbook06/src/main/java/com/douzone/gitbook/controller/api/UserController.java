package com.douzone.gitbook.controller.api;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.UserVo;

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
	@RequestMapping(value="/friend",method=RequestMethod.POST)
	public JsonResult friendInfo(@RequestBody String userId) {

		UserVo friendvo = userService.getUserFriend(userId);
		return JsonResult.success(friendvo);
		
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/req",method=RequestMethod.POST)
	public JsonResult friendRequest(@RequestBody Map<String, Object> param) {	
	
		List<UserVo> friendList = userService.getFriend(param);
		return JsonResult.success(friendList);	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/list",method=RequestMethod.POST)
	public JsonResult friendList(@RequestBody Map<String, Object> param) {	
	
		List<UserVo> friendList = userService.getFriend(param);
		return JsonResult.success(friendList);	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/add",method=RequestMethod.POST)
	public JsonResult friendAdd(@RequestBody Map<String, Object> param) {
		System.out.println("추가확인 " + param.get("userno") + ":" + param.get("friendno") + ":" + param.get("id") + ":" + param.get("kind"));
		userService.addFriend(param);
		List<UserVo> friendList = userService.getFriend(param);
		
		return JsonResult.success(friendList);	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/delete",method=RequestMethod.POST)
	public JsonResult friendDelete(@RequestBody Map<String, Object> param) {	
		System.out.println("추가확인 " + param.get("userno") + ":" + param.get("friendno"));
		
		return JsonResult.success(userService.deleteFriend(param));	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/search",method=RequestMethod.POST)
	public JsonResult search(@RequestBody Map<String, Object> param) {
		
		System.out.println("추가확인 " + param.get("userid") + ":" + param.get("keyword"));
		
		List<FriendVo> searchList = userService.getSearchList(param);
		
		return JsonResult.success(searchList);
	}
	
}
