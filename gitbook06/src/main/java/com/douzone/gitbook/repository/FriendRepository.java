package com.douzone.gitbook.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.UserVo;

@Repository
public class FriendRepository {

	@Autowired
	private SqlSession sqlSession;

	public void insertRequest(Map<String, Object> param) {
		sqlSession.insert("friend.reqFriend", param);
	}

	public void insertRequest2(Map<String, Object> param) {
		sqlSession.insert("friend.reqFriend2", param);
	}

	public void deleteFriendAll(Long no) {
		sqlSession.delete("friend.deleteAll", no);
	}

	public List<UserVo> selectSendList(Map<String, Object> param) {
		return sqlSession.selectList("friend.sendFollowList", param);
		
	}

}
