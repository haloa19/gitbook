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

	// 달력 클릭 시 해당 날짜의 내용 출력
	public List<ScheduleVo> findToDoList(String id, String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("date", date);

		return sqlSession.selectList("schedule.findToDoList", map);
	}

	// 달력 클릭 시 해당 날짜의 내용 출력
	public List<ScheduleVo> findRepoList(String id, String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("date", date);

		return sqlSession.selectList("schedule.findRepoList", map);
	}

	public List<ScheduleVo> findNaviCommitDay(String id, String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("date", "%" + date + "%");

		return sqlSession.selectList("schedule.findNaviCommitList", map);
	}

	public List<ScheduleVo> findCheckedToDoDay(String id) {
		return sqlSession.selectList("schedule.findCheckedToDoDay", id);
	}

	public List<ScheduleVo> findCheckedCommitDay(String id) {
		return sqlSession.selectList("schedule.findCheckedCommitDay", id);
	}

	public void deleteList(ScheduleVo vo) {
		sqlSession.delete("schedule.deleteToDo", vo);
	}

	/////// group
	public List<ScheduleVo> findToDoList(Long groupNo, Long userNo, String date) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("groupNo", groupNo);
		map.put("userNo", userNo);
		map.put("date", date);

		return sqlSession.selectList("schedule.findGroupToDoList", map);
	}

	public List<ScheduleVo> findRepoList(Long groupNo, String date) {
		Map<String, Object> map = new HashMap<String, Object>();

		Long userNo = sqlSession.selectOne("schedule.findGroupMasterNo", groupNo);

		map.put("groupNo", groupNo);
		map.put("userNo", userNo);
		map.put("date", date);

		return sqlSession.selectList("schedule.findGroupRepoList", map);
	}

	public void insertGroupToDo(ScheduleVo vo, Long groupNo) {
		vo.setGroupNo(groupNo);
		sqlSession.insert("schedule.insertGroupToDo", vo);
	}

	public List<ScheduleVo> findCheckedToDoDay(Long groupNo) {
		return sqlSession.selectList("schedule.findCheckedGroupToDoDay", groupNo);
	}

	public List<ScheduleVo> findCheckedGroupCommitDay(Long groupNo) {
		return sqlSession.selectList("schedule.findCheckedGroupCommitDay", groupNo);
	}

	public List<ScheduleVo> findNaviCommitDay(Long groupNo, String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupNo", groupNo);
		map.put("date", "%" + date + "%");

		return sqlSession.selectList("schedule.findGroupNaviCommitList", map);
	}

	public void deleteGroupAll(Long no) {
		sqlSession.delete("schedule.deleteGroupAll", no);

	}

}
