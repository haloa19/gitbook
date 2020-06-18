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
import com.douzone.gitbook.service.AlarmService;
import com.douzone.gitbook.service.FileUploadService;
import com.douzone.gitbook.service.FriendService;
import com.douzone.gitbook.service.GitService;
import com.douzone.gitbook.service.GroupService;
import com.douzone.gitbook.service.MailService;
import com.douzone.gitbook.service.ScheduleService;
import com.douzone.gitbook.service.TimelineService;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.AlarmVo;
import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.GroupVo;
import com.douzone.gitbook.vo.UserVo;
import com.douzone.security.Auth;
import com.douzone.security.AuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller("UserApiController")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FriendService friendService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private TimelineService timelineService;
	
	@Autowired
	private GitService gitService;
	
	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	MailService mailService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	AlarmService alarmService;

	@Autowired
	private ObjectMapper jsonMapper;

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
	public JsonResult friendInfo(@RequestBody Map<String, Object> param) { // 클릭한 친구의 정보 가져오기

		System.out.println("친구 처리 : " + param.get("userno") + ":" + param.get("friendid"));
		FriendVo friendvo = userService.getUserFriend(param.get("friendid").toString());

		String friendno = userService.getFriendNo(param);
		System.out.println("num :" + friendno);
		param.put("friendno", friendno);

		String status = userService.getFriendStatus(param);
		System.out.println("처리 결과 : " + status);

		if (status == null) {
			if (param.get("userno").equals(param.get("friendno"))) {
				System.out.println("본인이야");
				friendvo.setStatus("본인");
			} else {
				System.out.println("기타");
				friendvo.setStatus("기타");
			}
		} else if (status.equals("친구")) {
			friendvo.setStatus("친구");
		} else if (status.equals("요청중")) {
			friendvo.setStatus("요청중");
		}

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
	@RequestMapping(value = "/friend/navilist", method = RequestMethod.POST)
	public JsonResult friendNaviList(@RequestBody Map<String, Object> param) { // auth가 클릭한 친구의 친구들 목록 가져오기

		List<UserVo> friendList = userService.getFriendNavi(param);
		return JsonResult.success(friendList);
	}

	@ResponseBody
	@RequestMapping(value = "/friend/add", method = RequestMethod.POST)
	public JsonResult friendAdd(@RequestBody Map<String, Object> param) { // auth가 클릭한 친구의 친구들 목록 가져오기

		userService.addFriend(param);
		userService.addFriend2(param);
		List<UserVo> friendList = userService.getFriend(param);

		// 알림 추가
		AlarmVo vo = new AlarmVo();

		vo.setUserNo(Integer.toUnsignedLong((Integer) param.get("friendno")));
		
		vo.setAlarmType("friend");

		UserVo userVo = alarmService.getUserNoAndNickname(vo.getUserNo());
		UserVo friend = alarmService.getUserNoAndNickname(Integer.parseInt((String) param.get("userno")));

		vo.setUserId(userVo.getId());
		vo.setAlarmContents(friend.getNickname() + " 님과 친구가 되었습니다.");

		alarmService.addAlarm(vo);

		AlarmVo recentAlarm = alarmService.getRecentAlarm(vo);
		System.out.println("recentAlarm : " + recentAlarm);

		try {
			String alarmJsonStr = jsonMapper.writeValueAsString(recentAlarm);
			alarmService.sendAlarm("alarm>>" + alarmJsonStr, userVo.getId());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		//

		return JsonResult.success(friendList);
	}

	@ResponseBody
	@RequestMapping(value = "/friend/delete", method = RequestMethod.POST)
	public JsonResult friendDelete(@RequestBody Map<String, Object> param) { // auth가 클릭한 친구의 친구들 목록 가져오기

		userService.deleteFriend(param);
		List<UserVo> friendList = userService.getFriend(param);
		return JsonResult.success(friendList);
 
	}

	@ResponseBody
	@RequestMapping(value = "/friend/search", method = RequestMethod.POST)
	public JsonResult search(@RequestBody Map<String, Object> param) {

		List<FriendVo> searchList = null;

		if (param.get("keyword") != "") {
			searchList = userService.getSearchList(param);
		}

		return JsonResult.success(searchList);
	}

	@ResponseBody
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
	public JsonResult checkEmail(@RequestParam(value = "email", required = true, defaultValue = "") String email,
			@RequestParam(value = "random", required = true, defaultValue = "0") String random,
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
			@RequestParam(value = "authCode", required = true, defaultValue = "0") String authCode,
			HttpSession session) {

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

		System.out.println("[getProfile] " + resultVo.getId() + " >>> profile no : " + resultVo.getProfileNo()
				+ "  //  nickname : " + resultVo.getNickname() + "  //  contents : " + resultVo.getProfileContents()
				+ "  //  image : " + resultVo.getImage());

		return JsonResult.success(resultVo);

	}

	@Auth
	@ResponseBody
	@RequestMapping(value = "/profile/update/{id}", method = RequestMethod.POST)
	public JsonResult updateProfile(@PathVariable("id") String id, @RequestBody UserVo vo, HttpServletRequest request) {
		vo.setId(id);
		System.out.println("[updateProfile] " + vo.getId() + " >>> profile no : " + vo.getProfileNo()
				+ "  //  nickname : " + vo.getNickname() + "  //  contents : " + vo.getProfileContents()
				+ "  //  image : " + vo.getImage());

		// 프로파일 업데이트 진행
		Boolean result = userService.updateProfile(vo);
		if (!result) {
			return JsonResult.fail("failed updating profile");
		}

		// 세션 업데이트
		HttpSession session = request.getSession(false);
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		authUser.setNickname(vo.getNickname());
		authUser.setProfileContents(vo.getProfileContents());
		authUser.setImage(vo.getImage());
		session.setAttribute("authUser", authUser);

		return JsonResult.success(true);
	}

	@Auth
	@ResponseBody
	@RequestMapping(value = "/profile/uploadImage/{id}", method = RequestMethod.POST)
	public JsonResult uploadImage(@RequestParam("newImage") MultipartFile newImage) {
		if (newImage == null) {
			return JsonResult.fail("No file transfered...");
		}
		String newImageName = fileUploadService.restore(newImage);
		return JsonResult.success(newImageName);
	}

	@Auth
	@ResponseBody
	@RequestMapping(value = "/account/checkUser", method = RequestMethod.POST)
	public JsonResult checkUser(@RequestBody UserVo vo, @AuthUser UserVo authUser) {
		System.out.println("[checkUser(vo)] id : " + vo.getId() + "  //  password : " + vo.getPassword());
		System.out.println("[authUser] id : " + authUser.getId());
		if ("".equals(authUser.getId()) || vo.getId().equals(authUser.getId()) == false) {
			return JsonResult.fail("Not matched with session information");
		}

		UserVo userData = userService.getUser(vo);
		if (userData == null) {
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
	public JsonResult updateUser(@RequestBody Map<String, Object> input, @AuthUser UserVo authUser,
			HttpServletRequest request) {
		if (authUser == null) {
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
		if (!result) {
			return JsonResult.fail("failed for update");
		}

		// 세션 업데이트
		HttpSession session = request.getSession(false);
		UserVo authUserOriginal = (UserVo) session.getAttribute("authUser");
		authUserOriginal.setName(vo.getName());
		authUserOriginal.setPhone(vo.getPhone());
		authUserOriginal.setBirthday(vo.getBirthday());
		authUserOriginal.setGender(vo.getGender());
		session.setAttribute("authUser", authUserOriginal);

		return JsonResult.success(true);
	}
	
	@Auth
	@ResponseBody
	@RequestMapping(value = "/out/{no}", method = RequestMethod.POST)
	public JsonResult out(@PathVariable("no") Long no, HttpServletRequest request) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo = (UserVo) httpSession.getAttribute("authUser");

		System.out.println("탈퇴 테테스트" + no);
		
		// 1. 계정 비활성화
		userService.updateUserStatus(no);
		
		// 2. 친구 관계 삭제
		friendService.deleteFriendAll(no);
		
		// 3. 그룹에서 탈퇴
		// 3-0. 유저 권한 조회
		List<GroupVo> list = groupService.getGrantAll(no);
		for(GroupVo vo : list) {
			System.out.println("grant : " + vo.getGrant() + ":" + vo.getNo());
			
		
			if(vo.getGrant().equals("admin")) {		// 3-1. 그룹장인 경우 그룹 관련 모든 그룹 데이터 삭제(스케쥴, 타임라인, 레포) => 요청 그룹, 참여중인 그룹 쿼리에 조건 추가 필요
				// 타임라인 삭제
				timelineService.deleteGroupAll(vo.getNo());
				
				// 깃 삭제
				gitService.deleteGroupAll(vo.getNo());
				
				// 스케쥴 삭제
				scheduleService.deleteGroupAll(vo.getNo());
				
				// 그룹 리스트 삭제
				groupService.deleteGroupListAllAdmin(vo.getNo());
				
				// 그룹 삭제
				groupService.deleteGroupAll(vo.getNo());
				
				
			} else {	// 3-2. 일반 유저 또는 요청중인 경우 group_list에서만 삭제
				// 그룹 리스트 삭제
				groupService.deleteGroupListAll(vo.getNo(), uservo.getNo());
			}
		}
	
		
		// 5. 참여중인 채팅방에서 나가기
		// 6. 채팅방장인 경우 채팅방 자체를 삭제
		
		return JsonResult.success(true);
	}

}
