package com.douzone.gitbook.repository;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.UserVo;

@Repository
public class UserRepository {


	@Autowired
	private SqlSession sqlSession;
	public UserVo findByIdAndPassword(UserVo vo) {
		
		
		return sqlSession.selectOne("user.findByIdAndPassword", vo);
	}
	
	public List<UserVo> friendList(UserVo vo) {
		
		return sqlSession.selectList("user.friendList", vo);
	}

	public UserVo friendInfo(String userId) {

		return sqlSession.selectOne("user.friendInfo", userId);
	}
	

}
