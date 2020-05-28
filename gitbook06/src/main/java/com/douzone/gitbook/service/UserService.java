package com.douzone.gitbook.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.UserRepository;
import com.douzone.gitbook.vo.FriendVo;
import com.douzone.gitbook.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public UserVo getUser(UserVo vo) {
		
		
		return userRepository.findByIdAndPassword(vo);
	}
	
	public List<UserVo> getFriend(Map<String, Object> param) {
		
		
		return userRepository.friendList(param);
	}

	public UserVo getUserFriend(String userId) {
		
		return userRepository.friendInfo(userId);
	}

	public boolean addFriend(Map<String, Object> param) {
		
		return userRepository.addFriend(param);		
	}

	public boolean deleteFriend(Map<String, Object> param) {
		return userRepository.deleteFriend(param);
		
	}

	public List<FriendVo> getSearchList(Map<String, Object> param) {
		
		return userRepository.searchList(param);
	}

	public Boolean getEmailStatus(String email) {
		return userRepository.findEmailAvailable(email);
	}

	public Boolean addUser(UserVo vo) {
		return userRepository.addUser(vo);
		
	}
	
}
