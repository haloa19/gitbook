package com.douzone.gitbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.ScheduleRepository;
import com.douzone.gitbook.vo.ScheduleVo;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	public void insertToDo(ScheduleVo vo) {
		scheduleRepository.insertToDo(vo);
	}

	public List<ScheduleVo> getToDoList(String id, String date) {
		return scheduleRepository.findToDoList(id, date);
	}
	
	public List<ScheduleVo> getRepoList(String id, String date) {
		return scheduleRepository.findRepoList(id, date);
	}
	
	public List<ScheduleVo> getCheckedToDoDay(String id) {
		return scheduleRepository.findCheckedToDoDay(id);
	}
	
	public List<ScheduleVo> getCheckedCommitDay(String id) {
		return scheduleRepository.findCheckedCommitDay(id);
	}
	
	public List<ScheduleVo> getNaviCommitDay(String id,String date) {
		return scheduleRepository.findNaviCommitDay(id,date);
	}

	public void deleteToDo(ScheduleVo vo) {
		System.out.println("서비스 vo" + vo);
		scheduleRepository.deleteList(vo);
	}

	
}
