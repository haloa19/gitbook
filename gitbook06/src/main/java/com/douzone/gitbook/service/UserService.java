package com.douzone.gitbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.UserRepository;
import com.douzone.gitbook.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public UserVo getUser(UserVo vo) {
		
		
		return userRepository.findByIdAndPassword(vo);
	}
}
