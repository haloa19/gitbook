package com.douzone.gitbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.GitRepository;
import com.douzone.gitbook.vo.GitVo;

@Service
public class GitService {
	@Autowired
	private GitRepository gitRepository;
	
	public List<GitVo> getRepositoryList(String id) {
		return gitRepository.findList(id);
	}
	public void insertGit(GitVo vo) {
		
		gitRepository.insertGit(vo);
	}
	public void updateVisible(GitVo vo) {
		
		gitRepository.updateVisible(vo);
	}
	
}
