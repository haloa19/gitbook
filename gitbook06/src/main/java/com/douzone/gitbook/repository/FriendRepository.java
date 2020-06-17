package com.douzone.gitbook.repository;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

}
