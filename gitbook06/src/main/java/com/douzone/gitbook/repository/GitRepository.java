package com.douzone.gitbook.repository;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.GitVo;

@Repository
public class GitRepository {
	@Autowired
	private SqlSession sqlSession;
	
	public List<GitVo> findList(String id) {
		return sqlSession.selectList("git.findList",id);
	}

	public void insertGit(GitVo vo) {
	 sqlSession.insert("git.insertGit",vo);
	}

	public void updateVisible(GitVo vo) {
		 sqlSession.update("git.updateVisible",vo);
	}
	

}
