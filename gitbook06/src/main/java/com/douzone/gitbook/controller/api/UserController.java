package com.douzone.gitbook.controller.api;



import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.douzone.gitbook.service.MailService;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.UserVo;

@Controller("UserApiController")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
		///////////////////
	
	
	@Autowired
	MailService mailService;
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
	public JsonResult checkEmail(@RequestParam(value = "email", required = true, defaultValue = "") String email, @RequestParam(value = "random", required = true, defaultValue = "0") String random,
			HttpServletRequest req) {
		System.out.println("email: " + email + "  //  random: " + random);
		
		Boolean isPresent = userService.getEmailStatus(email);
		if (isPresent == false) {
			return JsonResult.fail("not available email");
		}

		// 사용자에게 이메일을 보내기
		int ran = new Random().nextInt(900000) + 100000;
		HttpSession session = req.getSession(true);

		// authCode & random(페이지) 두 값들 모두 session에 넣을것
		String authCode = String.valueOf(ran);
		session.setAttribute("authCode", authCode);
		session.setAttribute("random", Integer.parseInt(random));

		// 메일에 보낼 내용 정의
		String subject = "회원가입 인증 코드 발급 안내 입니다.";
		StringBuilder sb = new StringBuilder();
		sb.append("귀하의 인증 코드는 " + authCode + "입니다.");

		boolean isSent = mailService.send(subject, sb.toString(), "bigbossdc200@gmail.com", email, null);
		return (isSent ? JsonResult.success(true) : JsonResult.fail("failed for sending email"));
	}

	@ResponseBody
	@RequestMapping(value = "/checkAuth", method = RequestMethod.POST)
	public JsonResult checkAuth(@RequestParam(value = "random", required = true, defaultValue = "0") String random,
			@RequestParam(value = "authCode", required = true, defaultValue = "0") String authCode, HttpSession session) {
		
		// HttpSession 객체로부터 "authCode"와 "random" 불러오기
		String originalJoinCode = (String) session.getAttribute("authCode");
		String originalRandom = Integer.toString((int) session.getAttribute("random"));
		
		System.out.println("originalJoinCode: " + originalJoinCode + "  //  originalRandom: " + originalRandom);
		System.out.println("input >> authCode: " + authCode + "  //  random: " + random);
		
		if("0".equals(authCode) || "0".equals(random)) {
			return JsonResult.fail("authentication not matched");
		}
		if (originalJoinCode.equals(authCode) && originalRandom.equals(random)) {
			System.out.println("Authentication succeess!!!");
			return JsonResult.success(true);
		} else {
			return JsonResult.fail("authentication not matched");
		}
	}
	
	////////////////////
	
	
	
	
	
	
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
	public JsonResult friendInfo(@RequestBody String userId) {	// 클릭한 친구의 정보 가져오기

		UserVo friendvo = userService.getUserFriend(userId);
		return JsonResult.success(friendvo);
		
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/friend/req",method=RequestMethod.POST)
	public JsonResult friendRequest(@RequestBody Map<String, Object> param) {	// auth가 클릭한 친구의 친구들 목록 가져오기
	
		List<UserVo> friendList = userService.getFriend(param);
		return JsonResult.success(friendList);	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/list",method=RequestMethod.POST)
	public JsonResult friendList(@RequestBody Map<String, Object> param) {	// auth가 클릭한 친구의 친구들 목록 가져오기
	
		List<UserVo> friendList = userService.getFriend(param);
		return JsonResult.success(friendList);	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/add",method=RequestMethod.POST)
	public JsonResult friendAdd(@RequestBody Map<String, Object> param) {	// auth가 클릭한 친구의 친구들 목록 가져오기
		System.out.println("추가확인 " + param.get("userno") + ":" + param.get("friendno") + ":" + param.get("id") + ":" + param.get("kind"));
		userService.addFriend(param);
		List<UserVo> friendList = userService.getFriend(param);
		
		return JsonResult.success(friendList);	
	}
	
	@ResponseBody
	@RequestMapping(value="/friend/delete",method=RequestMethod.POST)
	public JsonResult friendDelete(@RequestBody Map<String, Object> param) {	// auth가 클릭한 친구의 친구들 목록 가져오기
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
