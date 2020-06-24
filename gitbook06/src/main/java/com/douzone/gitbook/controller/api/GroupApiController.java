package com.douzone.gitbook.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.AlarmService;
import com.douzone.gitbook.service.GroupService;
import com.douzone.gitbook.vo.AlarmVo;
import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.GroupVo;
import com.douzone.gitbook.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller("GroupApiController")
@RequestMapping("/group")
public class GroupApiController {

	@Autowired
	GroupService groupService;

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private ObjectMapper jsonMapper;

	// 내가 참여중인 그룹 리스트
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult reqFollow(HttpServletRequest request) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo = (UserVo) httpSession.getAttribute("authUser");

		List<GroupVo> groupList = groupService.getList(uservo);

		return JsonResult.success(groupList);
	}

	// 이미지 프리뷰 시 업로드
	@ResponseBody
	@RequestMapping(value = "/imgupload", method = RequestMethod.POST)
	public JsonResult upload(@RequestParam("file") MultipartFile multipartFile) {
		String url = groupService.restore(multipartFile);

		return JsonResult.success(url);
	}

	// 그룹 생성
	@ResponseBody
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public JsonResult regist(HttpServletRequest request, @RequestParam("groupTitle") String groupTitle,
			@RequestParam("description") String description, @RequestParam("imgurl") String imgurl) {
		HttpSession httpSession = request.getSession(false);
		UserVo userVo = (UserVo) httpSession.getAttribute("authUser");

		GroupVo groupVo = new GroupVo();
		groupVo.setGroupTitle(groupTitle);
		groupVo.setGroupIntro(description);
		groupVo.setImage(imgurl);

		groupService.regist(groupVo);

		Map<String, String> map = new HashMap<String, String>();
		map.put("groupno", groupVo.getNo().toString());
		map.put("userno", userVo.getNo().toString());
		map.put("grant", "admin");

		groupService.grant(map);
		//List<GroupVo> groupList = groupService.getList(userVo);
		System.out.println("group change " + groupVo.getNo());
		return JsonResult.success(groupVo.getNo());
	}

	// 클릭한 그룹 정보
	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public JsonResult info(@RequestBody Map<String, Object> param) {
		System.out.println("navi chk :" + param.get("userno") + ":" + param.get("groupno"));
		GroupVo groupVo = groupService.getInfo(param);

		return JsonResult.success(groupVo);
	}

	// 그룹 초대 가능한 멤버 목록 (그룹장의 친구)
	@ResponseBody
	@RequestMapping(value = "/reqlist", method = RequestMethod.POST)
	public JsonResult reqlist(@RequestBody Map<String, Object> param) {

		List<FriendVo> reqGroupList = groupService.getReqGroupList(param);

		return JsonResult.success(reqGroupList);
	}

	// 그룹 참여중인 멤버 목록
	@ResponseBody
	@RequestMapping(value = "/joinlist", method = RequestMethod.POST)
	public JsonResult joinlist(@RequestBody Map<String, Object> param) {

		List<FriendVo> joinGroupList = groupService.getJoinGroupList(param);

		return JsonResult.success(joinGroupList);
	}

	// 그룹 초대
	@ResponseBody
	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public JsonResult request(HttpServletRequest request, @RequestBody Map<String, Object> param) { // auth가 클릭한
																									// userid받아오기
		HttpSession httpSession = request.getSession(false);
		UserVo userVo = (UserVo) httpSession.getAttribute("authUser");

		groupService.addMember(param);

		// 소캣 매핑
		AlarmVo vo = new AlarmVo();
		vo.setUserNo(Integer.toUnsignedLong((Integer) param.get("userno")));

		Map<String, Long> numberMap = new HashMap<>();
		numberMap.put("userNo", Integer.toUnsignedLong((Integer) param.get("userno")));
		numberMap.put("groupNo", Integer.toUnsignedLong((Integer) param.get("groupno")));

		UserVo getUserIdVo = alarmService.getUserIdAndGroupTitle(numberMap);
		System.out.println(getUserIdVo.getId() + ":" + getUserIdVo.getGroupTitle());

		vo.setAlarmType("group");
		vo.setAlarmContents(getUserIdVo.getGroupTitle() + " 그룹에 초대되었습니다.");

		vo.setUserId(getUserIdVo.getId());

		alarmService.addAlarm(vo);

		AlarmVo recentAlarm = alarmService.getRecentAlarm(vo);

		try {
			String alarmJsonStr = jsonMapper.writeValueAsString(recentAlarm);
			alarmService.sendAlarm("alarm>>" + alarmJsonStr, vo.getUserId());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		//

		param.put("userno", userVo.getNo().toString());
		param.put("groupno", param.get("groupno").toString());

		List<FriendVo> reqGroupList = groupService.getReqGroupList(param);

		return JsonResult.success(reqGroupList);
	}

	// 나한테 요청온 그룹 리스트
	@ResponseBody
	@RequestMapping(value = "/myreqlist", method = RequestMethod.GET)
	public JsonResult myrequest(HttpServletRequest request) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo = (UserVo) httpSession.getAttribute("authUser");

		List<GroupVo> groupList = groupService.getMyRequest(uservo);

		return JsonResult.success(groupList);
	}

	// 요청온 그룹 참여


	@ResponseBody
	@RequestMapping(value = "/addgroup", method = RequestMethod.POST)
	public JsonResult addGroup(HttpServletRequest request, @RequestBody Map<String, Object> param) { // auth가 클릭한
		// userid받아오기
		HttpSession httpSession = request.getSession(false);
		UserVo uservo = (UserVo) httpSession.getAttribute("authUser");
		List<Map<String, Object>> groupUserList = new ArrayList<>();
	
		//groupNo Casting
		int groupNo = ("java.lang.String".equals(param.get("groupno").getClass().getName())) ? Integer.parseInt((String) param.get("groupno")) : (Integer) param.get("groupno");
		groupUserList = alarmService.getGroupUserList(groupNo);
    		
		groupService.addGroup(param); 
    
		List<GroupVo> groupList = groupService.getList(uservo);

		

		for (Map<String, Object> line : groupUserList) {

			AlarmVo vo = new AlarmVo();

			vo.setUserNo((Long) line.get("no")); // 그룹원들의 no로 맞출 것
			vo.setAlarmType("group");
			vo.setAlarmContents(uservo.getNickname() + " 님이 " + line.get("groupTitle") + " 그룹에 가입했습니다.");
			vo.setUserId((String) line.get("id"));

			alarmService.addAlarm(vo);

			AlarmVo recentAlarm = alarmService.getRecentAlarm(vo);

			try {
				String alarmJsonStr = jsonMapper.writeValueAsString(recentAlarm);
				alarmService.sendAlarm("alarm>>" + alarmJsonStr, (String) line.get("id"));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return JsonResult.success(groupList);
	}


	// 요청온 그룹 거절
	@ResponseBody
	@RequestMapping(value = "/rejectgroup", method = RequestMethod.POST)
	public JsonResult rejectGroup(HttpServletRequest request, @RequestBody Map<String, Object> param) {
		HttpSession httpSession = request.getSession(false);
		UserVo uservo = (UserVo) httpSession.getAttribute("authUser");

		groupService.rejectGroup(param);

		List<GroupVo> groupList = groupService.getList(uservo);

		return JsonResult.success(groupList);
	}

	// 멤버 탈퇴 시키기
	@ResponseBody
	@RequestMapping(value = "/deleteMember", method = RequestMethod.POST)
	public JsonResult deleteMember(HttpServletRequest request, @RequestBody Map<String, Object> param) {

		groupService.rejectGroup(param);

		List<FriendVo> joinGroupList = groupService.getJoinGroupList(param);

		return JsonResult.success(joinGroupList);
	}

	// 그룹 정보 업데이트
	@ResponseBody
	@RequestMapping(value = "/infoupdate", method = RequestMethod.POST)
	public JsonResult infoupdate(@RequestBody Map<String, Object> param) {

		groupService.update(param);
		GroupVo groupVo = groupService.getInfo(param);

		return JsonResult.success(groupVo);
	}

}
