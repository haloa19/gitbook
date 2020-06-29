package com.douzone.gitbook.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.AlarmService;
import com.douzone.gitbook.service.FriendService;
import com.douzone.gitbook.vo.AlarmVo;
import com.douzone.gitbook.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller("FriendApiController")
@RequestMapping("/friend")
public class FriendApiController {

	@Autowired
	FriendService friendService;

	@Autowired
	private AlarmService alarmService;

	@Autowired
	private ObjectMapper jsonMapper;

	@ResponseBody
	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public JsonResult reqFollow(@RequestBody Map<String, Object> param) { // auth가 클릭한 userid받아오기
		friendService.requestFriend(param);
		// friendService.requestFriend2(param);

		// 소켓 매핑
		AlarmVo vo = new AlarmVo();

		vo.setUserNo(Integer.toUnsignedLong((Integer) param.get("friendno")));
		vo.setAlarmType("friend");
		vo.setUserId((String) param.get("friendId"));
		vo.setAlarmContents(param.get("userNickName") + "님의 친구요청이 있습니다.");

		alarmService.addAlarm(vo);

		AlarmVo recentAlarm = alarmService.getRecentAlarm(vo);

		try {
			String alarmJsonStr = jsonMapper.writeValueAsString(recentAlarm);
			alarmService.sendAlarm("alarm>>" + alarmJsonStr, ((String) param.get("friendId")));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return JsonResult.success(true);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/send/follow", method = RequestMethod.POST)
	public JsonResult sendFollow(@RequestBody Map<String, Object> param) {
		System.out.println("me request " + param.get("userno"));
		
		List<UserVo> sendFollowList = friendService.getSendList(param);
		
		if(sendFollowList.size() > 0) {
			System.out.println("me test " + sendFollowList.get(0));
		}

		return JsonResult.success(sendFollowList);
	}
}
