package com.douzone.gitbook.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.GroupVo;
import com.douzone.gitbook.vo.UserVo;

@Repository
public class GroupRepository {
	
	@Autowired
	private SqlSession sqlSession;

	public List<GroupVo> groupList(UserVo uservo) {
		
		return sqlSession.selectList("group.groupList", uservo);
	}

	public void groupRegist(GroupVo groupVo) {
		
		sqlSession.insert("group.groupRegist", groupVo);
	}

	public void groupGrant(Map<String, String> map) {
		
		sqlSession.insert("group.groupGrant", map);
	}

	public GroupVo groupInfo(Map<String, Object> param) {
		
		return sqlSession.selectOne("group.groupInfo", param);
	}

	public List<FriendVo> reqGroupList(Map<String, Object> param) {
		
		return sqlSession.selectList("group.reqGroupList", param);
	}
	
	public List<FriendVo> joinGroupList(Map<String, Object> param) {
		
		return sqlSession.selectList("group.joinGroupList", param);
	}

	public void addMember(Map<String, Object> param) {
		sqlSession.insert("group.addMember", param);		
	}

	public List<GroupVo> myreqList(UserVo uservo) {
		return sqlSession.selectList("group.myreqList", uservo);
		
	}

	public void addGroup(Map<String, Object> param) {
		sqlSession.update("group.addGroup", param);
	}

	public void rejectGroup(Map<String, Object> param) {
		sqlSession.delete("group.rejectGroup", param);
	}

	public void update(Map<String, Object> param) {
		sqlSession.update("group.update", param);
		
	}

	public List<GroupVo> getGrantAll(Long no) {
		
		return sqlSession.selectList("group.grantAll", no);
	}
	
	public void deleteGroupListAllAdmin(Long no) {
		sqlSession.delete("group.deleteGroupListAllAdmin", no);
		
	}

	public void deleteGroupListAll(Long groupno, Long userno) {
		Map<String, Long> map = new HashMap<String,Long>(); 
		map.put("groupno", groupno);
		map.put("userno", userno);
		
		sqlSession.delete("group.deleteGroupListAll", map);
		
	}

	public void deleteGroupAll(Long no) {
		sqlSession.delete("group.deleteGroupAll", no);
		
	}

	public String getGroupTitle(Long groupNo) {
		return sqlSession.selectOne("group.findGroupTitle", groupNo);
	}
	
}
