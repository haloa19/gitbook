package com.douzone.gitbook.controller.api;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.MailService;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.FriendVo;

import com.douzone.gitbook.service.FileUploadService;

import com.douzone.gitbook.vo.UserVo;
import com.douzone.security.Auth;
import com.douzone.security.AuthUser;

@Controller("UserApiController")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	MailService mailService;
	
	@Autowired
	FileUploadService fileUploadService;

  
  
	@ResponseBody
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public JsonResult checkEmail(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo = (UserVo) httpSession.getAttribute("authUser");

		System.out.println("확인:" + uservo);
		if (uservo == null) {
			return JsonResult.success(null);
		}

		return JsonResult.success(uservo);
	}
  
	@ResponseBody
	@RequestMapping(value = "/friend", method = RequestMethod.POST)
	public JsonResult friendInfo(@RequestBody String userId) { // 클릭한 친구의 정보 가져오기

		UserVo friendvo = userService.getUserFriend(userId);
		return JsonResult.success(friendvo);

	}
  
	@ResponseBody
	@RequestMapping(value = "/friend/req", method = RequestMethod.POST)
	public JsonResult friendRequest(@RequestBody Map<String, Object> param) { // auth가 클릭한 친구의 친구들 목록 가져오기

		List<UserVo> friendList = userService.getFriendReq(param);
		return JsonResult.success(friendList);	
	}

	@ResponseBody
	@RequestMapping(value = "/friend/list", method = RequestMethod.POST)
	public JsonResult friendList(@RequestBody Map<String, Object> param) { // auth가 클릭한 친구의 친구들 목록 가져오기

		List<UserVo> friendList = userService.getFriend(param);
		return JsonResult.success(friendList);
	}

	@ResponseBody
	@RequestMapping(value = "/friend/add", method = RequestMethod.POST)
	public JsonResult friendAdd(@RequestBody Map<String, Object> param) { // auth가 클릭한 친구의 친구들 목록 가져오기
		System.out.println("추가확인 " + param.get("userno") + ":" + param.get("friendno") + ":" + param.get("id") + ":" + param.get("kind"));

		userService.addFriend(param);
		userService.addFriend2(param);
		List<UserVo> friendList = userService.getFriend(param);

		return JsonResult.success(friendList);
	}

	@ResponseBody
	@RequestMapping(value="/friend/delete",method=RequestMethod.POST)
	public JsonResult friendDelete(@RequestBody Map<String, Object> param) {	// auth가 클릭한 친구의 친구들 목록 가져오기
		System.out.println("추가확인 삭제" + param.get("userno") + ":" + param.get("friendno") + ":" + param.get("kind"));
		userService.deleteFriend(param);
		List<UserVo> friendList = userService.getFriend(param);
		System.out.println(friendList.get(0));
		return JsonResult.success(friendList);	

	}

	@ResponseBody
	@RequestMapping(value="/friend/search",method=RequestMethod.POST)
	public JsonResult search(@RequestBody Map<String, Object> param) {
		
		System.out.println("추가확인요청 " + param.get("userno") + ":" + param.get("userid") + ":" + param.get("keyword"));
		List<FriendVo> searchList = null;
		
		if(param.get("keyword") != "") {
	
			searchList = userService.getSearchList(param);
		}
		
		return JsonResult.success(searchList);
	}

	@ResponseBody
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
	public JsonResult checkEmail(@RequestParam(value = "email", required = true, defaultValue = "") String email, @RequestParam(value = "random", required = true, defaultValue = "0") String random,
			HttpServletRequest req) {
		System.out.println("email: " + email + "  //  random: " + random);

		Boolean isAvailable = userService.getEmailStatus(email);
		if (isAvailable == false) {
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

		if ("0".equals(authCode) || "0".equals(random)) {
			return JsonResult.fail("authentication not matched");
		}
		if (originalJoinCode.equals(authCode) && originalRandom.equals(random)) {
			System.out.println("Authentication succeess!!!");
			return JsonResult.success(true);
		} else {
			return JsonResult.fail("authentication not matched");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/profile/info/{id}", method = RequestMethod.POST)
	public JsonResult getProfile(@PathVariable("id") String id) {
		System.out.println(id);
		UserVo resultVo = userService.getProfile(id);
		resultVo.setId(id);
		
		System.out.println("[getProfile] " + resultVo.getId() + " >>> profile no : " + resultVo.getProfileNo() + "  //  nickname : " + resultVo.getNickname() + "  //  contents : " + resultVo.getProfileContents() + "  //  image : " + resultVo.getImage());
		
		return JsonResult.success(resultVo);
		
	}
	
	@Auth
	@ResponseBody
	@RequestMapping(value = "/profile/update/{id}", method = RequestMethod.POST)
	public JsonResult updateProfile(@PathVariable("id") String id, @RequestBody UserVo vo) {
		vo.setId(id);
		System.out.println("[updateProfile] " + vo.getId() + " >>> profile no : " + vo.getProfileNo() + "  //  nickname : " + vo.getNickname() + "  //  contents : " + vo.getProfileContents() + "  //  image : " + vo.getImage());
    
		// 프로파일 업데이트 진행
		Boolean result = userService.updateProfile(vo);
		if(!result) {
			return JsonResult.fail("failed updating profile");
		}
		return JsonResult.success(true);
	}
	
	@Auth
	@ResponseBody
	@RequestMapping(value = "/profile/uploadImage/{id}", method = RequestMethod.POST)
	public JsonResult uploadImage(@RequestParam("newImage") MultipartFile newImage) {
		if(newImage == null) {
			return JsonResult.fail("No file transfered...");
		}
		String newImageName = fileUploadService.restore(newImage);
		return JsonResult.success(newImageName);		
	}
	
	@Auth
	@ResponseBody
	@RequestMapping(value = "/account/checkUser", method = RequestMethod.POST)
	public JsonResult checkUser(@RequestBody UserVo vo) {
		System.out.println("[checkUser(vo)] id : " + vo.getId() + "  //  password : " + vo.getPassword());
		UserVo userData = userService.getUser(vo);
		if(userData == null) {
			return JsonResult.fail("no user found");
		}
		UserVo returned = new UserVo();
		returned.setId(userData.getId());
		returned.setPhone(userData.getPhone());
		returned.setName(userData.getName());
		returned.setBirthday(userData.getBirthday());
		returned.setGender(userData.getGender());
		return JsonResult.success(returned);
	}
	
	@Auth
	@ResponseBody
	@RequestMapping(value = "/account/updateUser", method = RequestMethod.POST)
	public JsonResult updateUser(@RequestBody Map<String, Object> input, @AuthUser UserVo authUser) {
		if(authUser == null) {
			return JsonResult.fail("cannot find user");
		}
		System.out.println("id >> " + authUser.getId());
		
		UserVo vo = new UserVo();
		vo.setId(authUser.getId());
		vo.setName((String) input.get("name"));
		vo.setPhone((String) input.get("phone"));
		vo.setBirthday((String) input.get("birthday"));
		vo.setGender((String) input.get("gender"));
		if (input.get("password") != null && "".equals((String) input.get("password")) == false) {
			vo.setPassword((String) input.get("password"));
		}
		System.out.println(vo);
		
		Boolean result = userService.updateUserInfo(vo);
		if(!result) {
			return JsonResult.fail("failed for update");
		}
		return JsonResult.success(true);
	}
	

}
