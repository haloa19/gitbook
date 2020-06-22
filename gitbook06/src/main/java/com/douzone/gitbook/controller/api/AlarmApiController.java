package com.douzone.gitbook.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.AlarmService;
import com.douzone.gitbook.service.GitService;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.vo.AlarmVo;
import com.douzone.gitbook.vo.GitVo;
import com.douzone.gitbook.vo.UserVo;
import com.douzone.security.Auth;
import com.douzone.security.AuthUser;

@Controller("AlarmApiController")
@RequestMapping("/alarm")
public class AlarmApiController {

	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private GitService gitService;
	
	@Auth
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public JsonResult getAlarmList(@AuthUser UserVo authUser) {
	
		List<AlarmVo> alarmList = alarmService.getAlarmList(authUser.getId());
		
		for (AlarmVo vo : alarmList) {
			if("commit".equals(vo.getAlarmType())) {
				GitVo gitInfo = gitService.getGitInfoByNo(vo.getAlarmRefNo());
	
				Long groupNo = gitInfo.getGroupNo();
				vo.setUserNo(gitInfo.getUserNo());
				vo.setGroupNo(groupNo);
			}
		}
				
		return JsonResult.success(alarmList);
	}

	@Auth
	@ResponseBody
	@RequestMapping(value = "/mark", method = RequestMethod.POST)
	public JsonResult markAsRead(@RequestBody Map<String, Object> input, @AuthUser UserVo authUser) {
		// input : { no : ??? , id : ??? }
		if (authUser.getId().equals((String) input.get("id")) == false) {
			return JsonResult.fail("not matched user ID");
		}

		Boolean isMarked = alarmService.markAsRead(input);
		if (!isMarked) {
			return JsonResult.fail("failed for marking read");
		}

		return JsonResult.success(true);
	}
	
	@MessageMapping("/alarm/send") //react > spring 수신
	public void send(AlarmVo msg) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 가즈아!!!!!!!!!!!!!!!!!!!!!!!!!!!" + msg.getAlarmContents());
		
	
		// webSocket.convertAndSend("/topics/alarm/test", message); //react로 메세지 전송
	}

}
