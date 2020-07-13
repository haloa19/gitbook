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

	// pushProcess 에서 hook이 들어왔을 경우에 처리한다
	// input : "push" 관련된 모든 정보들 map
	public Boolean addPushInfo(Map<String, Object> push) {
		// 1. 현재 사용자의 userNo 불러와서 추가하기
		push.put("userNo", sqlSession.selectOne("git.findUserNo", push));
		System.out.println("유저 넘버 >> " + (Long) push.get("userNo"));
		
		// 2. repository 의 gitNo 불러와서 추가하기
		push.put("gitNo", sqlSession.selectOne("git.findGitNoByUserInfo", push));

		// 3. repository의 정보를 조회하기 ( [ gitNo ] >>> [ no, userNo, groupNo, description, visible, gitName, regDate, userId ] )
		GitVo gitInfo = sqlSession.selectOne("git.findGitInfoByNo", (Long) push.get("gitNo"));
		
		// 4. repository 정보 중에서...
		if ("public".equals(gitInfo.getVisible())) {
			// 4.1 "public" 으로 개방되었을 경우	-->	push 안에 [ visible = "public" ] 추가
			push.put("visible", "public");
		} else {
			// 4.1 "private" 으로 닫혀있을 때	-->	push 안에 [ visible = "public" ] 추가
			push.put("visible", "private");
		}
		
		// 결과 값들을 정의하기
		Boolean result_schedule = false;
		Boolean result_alarm = false;
		Boolean result_timeline = false;
		
		// 5.1 해당 repository가 "private" 으로 닫혀있을 경우 --> 해당 repository의 관리자 no (gitInfo.userNo)와 일치할 경우에만 alarm 넣기
		// 5.2 해당 repository가 "public" 으로 개방되었을 경우 --> alarm 넣기
		if (gitInfo.getUserNo() == (long) push.get("userNo") || ("public".equals(gitInfo.getVisible()))) {
			result_schedule = (sqlSession.insert("git.insertSchedule", push) == 1);
			result_alarm = (sqlSession.insert("git.insertAlarm", push) == 1);
		}
		
		// 6. timeline 넣기 (push한 사람의 명의로 넣기)
		if (((String) push.get("id")).equals((String) push.get("whoPushed"))) {
			result_timeline = (sqlSession.insert("git.insertTimeline", push) == 1);
		}
		

		// 7. 결과 보내기
		return true;
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
