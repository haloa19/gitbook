package com.douzone.gitbook.repository;


import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.douzone.gitbook.vo.UserVo;

@Repository
public class UserRepository {


	@Autowired
	private SqlSession sqlSession;
	public UserVo findByIdAndPassword(UserVo vo) {
		
		
		return sqlSession.selectOne("user.findByIdAndPassword", vo);
	}
	

}
