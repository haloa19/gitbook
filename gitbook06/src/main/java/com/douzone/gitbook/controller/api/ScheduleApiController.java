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

@Controller("ScheduleApiController")
@RequestMapping("/Schedule/{id:(?!assets).*}")
public class ScheduleApiController {

	@Autowired
	ScheduleService scheduleService;

	// 할일 리스트 출력
	@ResponseBody
	@RequestMapping(value = "/toDoList/{date}", method = RequestMethod.GET)
	public JsonResult getToDoList(@PathVariable String id, @PathVariable String date) {

		List<ScheduleVo> list = scheduleService.getToDoList(id, date);

		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/repoList/{date}", method = RequestMethod.GET)
	public JsonResult getRepoList(@PathVariable String id, @PathVariable String date) {
		
		List<ScheduleVo> list = scheduleService.getRepoList(id, date);
				
		return JsonResult.success(list);
	}

	// CheckDay 출력
	@ResponseBody
	@RequestMapping(value = "/notEmptyToDoList", method = RequestMethod.GET)
	public JsonResult ToDoList(@PathVariable String id) {

		List<ScheduleVo> list = scheduleService.getCheckedToDoDay(id);

		return JsonResult.success(list);
	}

	// CheckDay 출력
	@ResponseBody
	@RequestMapping(value = "/notEmptyCommitList", method = RequestMethod.GET)
	public JsonResult CommitList(@PathVariable String id) {

		List<ScheduleVo> list = scheduleService.getCheckedCommitDay(id);

		return JsonResult.success(list);
	}

	// 네비게이션 캘린더 list 가져오기
	@ResponseBody
	@RequestMapping(value = "/naviCommitList/{date}", method = RequestMethod.GET)
	public JsonResult NaviCommitList(@PathVariable String id, @PathVariable String date) {
		List<ScheduleVo> list = scheduleService.getNaviCommitDay(id, date);
		return JsonResult.success(list);
	}

	// 할일 추가
	@ResponseBody
	@RequestMapping(value = "/addToDo/{date}")
	public JsonResult addToDo(@RequestBody ScheduleVo vo, @PathVariable String id, @PathVariable String date) {
		scheduleService.insertToDo(vo);
		List<ScheduleVo> list = scheduleService.getToDoList(id, date);
		return JsonResult.success(list);
	}

	@ResponseBody
	@RequestMapping(value = "/delete/{date}", method = RequestMethod.POST)
	public JsonResult deleteToDo(@RequestBody ScheduleVo vo, @PathVariable String id, @PathVariable String date) {
		scheduleService.deleteToDo(vo);
		List<ScheduleVo> list = scheduleService.getToDoList(id, date);
		return JsonResult.success(list);
	}

}
