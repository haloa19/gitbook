package com.douzone.gitbook.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.FriendRepository;
import com.douzone.gitbook.vo.UserVo;

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

	public List<UserVo> getSendList(Map<String, Object> param) {
		
		return friendRepository.selectSendList(param);
		
	}

}
