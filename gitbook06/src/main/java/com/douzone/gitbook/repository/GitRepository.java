package com.douzone.gitbook.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.GitVo;

@Repository
public class GitRepository {
	@Autowired
	private SqlSession sqlSession;

	public List<GitVo> findList(String id) {
		return sqlSession.selectList("git.findList", id);
	}

	public void insertGit(GitVo vo) {
		sqlSession.insert("git.insertGit", vo);
	}

	public void updateVisible(GitVo vo) {
		sqlSession.update("git.updateVisible", vo);
	}

	public GitVo getGitItem(String id, String repoName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("repoName", repoName);

		return sqlSession.selectOne("git.getGitItem", map);
	}

	public void deleteRepository(GitVo vo) {
		sqlSession.delete("git.deleteRepository", vo);

	}

	public Boolean addPushInfo(Map<String, Object> push) {
	      Long userNo = sqlSession.selectOne("git.findUserNo", push);
	      push.put("userNo", userNo);
	      
	      Long gitNo = sqlSession.selectOne("git.findGitNoByUserInfo", push);
	      push.put("gitNo", gitNo);

	      GitVo gitInfo = sqlSession.selectOne("git.findGitInfoByNo", (long)push.get("gitNo"));
	      
	      // 관리자와 private
	      if("public".equals(gitInfo.getVisible()) || gitInfo.getUserNo() == (Long)push.get("userNo")) {
	    	  Integer result_alarm = sqlSession.insert("git.insertAlarm", push);
		      System.out.println("result_alarm >> " + (result_alarm == 1));
	      }
	      
	      Integer result_schedule = sqlSession.insert("git.insertSchedule", push);
	      System.out.println("result_schedule >> " + (result_schedule == 1));
	      
	      String visible = sqlSession.selectOne("git.findVisible", push);
	      if("public".equals(visible)) {
	         push.put("visible", "public");
	      } else {
	         push.put("visible", "private");
	      }
	      
	      if(push.get("whoPushed").equals(push.get("id"))) {
	    	  Integer result_timeline = sqlSession.insert("git.insertTimeline", push);
	    	  System.out.println("result_timeline >> " + (result_timeline == 1));
	      }
	     
	      
	      return result_schedule == 1;
	   }

	public List<GitVo> findListGroup(Map<String, String> map) {
		return sqlSession.selectList("git.findListGroup", map);
	}

	public List<GitVo> findMyList(String id) {

		return sqlSession.selectList("git.findMyList", id);
	}

	public String getUserNickName(String id) {
		String nickName = sqlSession.selectOne("alarm.findUserNicknameById", id);
		return nickName;
	}

	public void deleteGroupAll(Long no) {
		sqlSession.delete("git.deleteGroupAll", no);
		
	}

	public Object getGroupNo(String repoName, String id, long userNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("repoName", repoName);
		map.put("id", id);
		map.put("userNo", userNo);
		return sqlSession.selectOne("git.getGroupNo", map);
	}

	public Long findGroupNo(Map<String, Object> push) {
		Long userNo = sqlSession.selectOne("git.findUserNo", push);
		push.put("userNo", userNo);
		
		return sqlSession.selectOne("git.findGroupNo", push);
	}

	public List<String> findGroupMemberIdList(Long groupNo) {
		return sqlSession.selectList("git.findGroupMemberIdList", groupNo);
	}

	public GitVo findGitInfoByNo(Long alarmRefNo) {
		return sqlSession.selectOne("git.findGitInfoByNo", alarmRefNo);
	}

}
