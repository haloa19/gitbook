package com.douzone.gitbook.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.FriendRepository;

@Service
public class FriendService {

	@Autowired
	FriendRepository friendRepository;
	
	public void requestFriend(Map<String, Object> param) {
		friendRepository.insertRequest(param);
	}

	public void requestFriend2(Map<String, Object> param) {
		friendRepository.insertRequest2(param);
	}

	public void deleteFriendAll(Long no) {
		friendRepository.deleteFriendAll(no);
		
	}

}
