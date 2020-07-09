package com.douzone.gitbook.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.ScheduleService;
import com.douzone.gitbook.vo.ScheduleVo;

@Controller("ScheduleGroupApiController")
@RequestMapping("/group/Schedule/{groupNo:(?!assets).*}/{userNo:(?!assets).*}")
public class ScheduleGroupApiController {

	@Autowired
	ScheduleService scheduleService;

	// 할일 리스트 출력
	@ResponseBody
	@RequestMapping(value = "/toDoList/{date}", method = RequestMethod.GET)
	public JsonResult getToDoList(@PathVariable Long groupNo, @PathVariable Long userNo, @PathVariable String date) {
		List<ScheduleVo> list = scheduleService.getToDoList(groupNo, userNo, date);
		
		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/repoList/{date}", method = RequestMethod.GET)
	public JsonResult getRepoList(@PathVariable Long groupNo, @PathVariable Long userNo, @PathVariable String date) {
		List<ScheduleVo> list = scheduleService.getRepoList(groupNo, date);
		
		return JsonResult.success(list);
	}

	// 할일 추가
	@ResponseBody
	@RequestMapping(value = "/addToDo/{date}")
	public JsonResult addToDo(@RequestBody ScheduleVo vo, @PathVariable Long groupNo, @PathVariable Long userNo,
			@PathVariable String date) {
		scheduleService.insertGroupToDo(vo, groupNo);
		List<ScheduleVo> list = scheduleService.getToDoList(groupNo, userNo, date);
		return JsonResult.success(list);
	}

	// CheckDay 출력
	@ResponseBody
	@RequestMapping(value = "/notEmptyGroupToDoList", method = RequestMethod.GET)
	public JsonResult ToDoList(@PathVariable Long groupNo) {
	
		List<ScheduleVo> list = scheduleService.getCheckedToDoDay(groupNo);

		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/notEmptyGroupCommitList", method = RequestMethod.GET)
	public JsonResult CommitList(@PathVariable Long groupNo) {

		List<ScheduleVo> list = scheduleService.getCheckedCommitDay(groupNo);
		System.out.println("ok");
		return JsonResult.success(list);
	}

	// 네비게이션 캘린더 list 가져오기
	@ResponseBody
	@RequestMapping(value = "/naviGroupCommitList/{date}", method = RequestMethod.GET)
	public JsonResult NaviCommitList(@PathVariable Long groupNo, @PathVariable String date) {
		List<ScheduleVo> list = scheduleService.getNaviCommitDay(groupNo, date);

		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/delete/{date}", method = RequestMethod.POST)
	public JsonResult deleteToDo(@RequestBody ScheduleVo vo, @PathVariable Long groupNo, @PathVariable Long userNo,
			@PathVariable String date) {
		scheduleService.deleteToDo(vo);
		List<ScheduleVo> list = scheduleService.getToDoList(groupNo, userNo, date);
		return JsonResult.success(list);

		// test
	}
}
