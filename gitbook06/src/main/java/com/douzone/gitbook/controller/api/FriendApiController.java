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
import com.douzone.gitbook.service.FriendService;
import com.douzone.gitbook.vo.UserVo;

@Controller("FriendApiController")
@RequestMapping("/friend")
public class FriendApiController {
	
	@Autowired
	FriendService friendService;
	
	@ResponseBody
	@RequestMapping(value="/request",method=RequestMethod.POST)
	public JsonResult reqFollow(@RequestBody Map<String, Object> param) {	// auth가 클릭한 userid받아오기
		
		System.out.println("chk 0608 :" + param.get("userno") + ":" + param.get("friendno"));
		friendService.requestFriend(param);
		//friendService.requestFriend2(param);
		
		return JsonResult.success(true);	
	}

}


