package com.douzone.gitbook.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.ScheduleVo;

@Repository
public class ScheduleRepository {

	@Autowired
	private SqlSession sqlSession;

	public void insertToDo(ScheduleVo vo) {
		sqlSession.insert("schedule.insertToDo", vo);
	}

	public List<ScheduleVo> findToDoList(String id, String date) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id",id);
		map.put("date",date);
		
		return sqlSession.selectList("schedule.findToDoList",map);
	}
	
	public List<ScheduleVo> findRepoList(String id, String date) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id",id);
		map.put("date",date);
		
		return sqlSession.selectList("schedule.findRepoList",map);
	}
	
	public List<ScheduleVo> findNaviCommitDay(String id, String date) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id",id);
		map.put("date","%"+date+"%");
		
		return sqlSession.selectList("schedule.findNaviCommitList",map);
	}
	
	
	public List<ScheduleVo> findCheckedToDoDay(String id) {
		return sqlSession.selectList("schedule.findCheckedToDoDay",id);
	}
	
	public List<ScheduleVo> findCheckedCommitDay(String id) {
		return sqlSession.selectList("schedule.findCheckedCommitDay",id);
	}

	public void deleteList(ScheduleVo vo) {
		sqlSession.delete("schedule.deleteToDo", vo);
	}

	
	
	
}
