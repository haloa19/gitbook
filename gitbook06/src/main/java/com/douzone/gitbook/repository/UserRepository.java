package com.douzone.gitbook.repository;

import java.util.List;
import java.util.Map;

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

	
	public List<UserVo> friendList(Map<String, Object> param) {
		
		return sqlSession.selectList("user.friendList", param);
	}

	public List<UserVo> friendListReq(Map<String, Object> param) {
		
		return sqlSession.selectList("user.friendListReq", param);
	}

	
	public UserVo friendInfo(String userId) {
		return sqlSession.selectOne("user.friendInfo", userId);
	}


	public boolean addFriend(Map<String, Object> param) {
		int count = sqlSession.update("user.friendAdd", param);
		return count == 1;
		
	}

	public boolean addFriend2(Map<String, Object> param) {
		int count = sqlSession.insert("user.friendAdd2", param);
		return count == 1;
	}

	public boolean deleteFriend(Map<String, Object> param) {
		int count = sqlSession.delete("user.friendDelete", param);
		return count == 1;
	}

	public List<FriendVo> searchList(Map<String, Object> param) {
		
		return sqlSession.selectList("user.searchList", param);
  }

	public Boolean findEmailAvailable(String email) {
		return (Integer) sqlSession.selectOne("user.countEmail", email) == 0;
	}

	public Boolean addUser(UserVo vo) {
		return sqlSession.update("user.addUser", vo) == 1 && sqlSession.update("user.addProfile", vo) == 1;

	}
  
}
