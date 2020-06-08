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
	
	public List<UserVo> getFriendReq(Map<String, Object> param) {
		
		return userRepository.friendListReq(param);

	}

	public FriendVo getUserFriend(String userId) {
		return userRepository.friendInfo(userId);
	}

	public boolean addFriend(Map<String, Object> param) {
		return userRepository.addFriend(param);
	}
	
	public boolean addFriend2(Map<String, Object> param) {
		
		return userRepository.addFriend2(param);		
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
  
	public String getEmail(UserVo vo) {
		return userRepository.findEmail(vo);
	}
  
	public Boolean getEmailExistance(String email) {
		return userRepository.findEmailExistance(email);
	}


	public boolean existUser(String password, String id) {
		
		return userRepository.findPassword(password,id) != null;
  }
  
	public Boolean updatePassword(UserVo vo) {
		return userRepository.changePasswordResult(vo);
	}

	public UserVo getProfile(String id) {
		return userRepository.getProfileInfo(id);

	}

	public Boolean updateProfile(UserVo vo) {
		return userRepository.updateProfileInfo(vo);
	}
  
	public Boolean updateUserInfo(UserVo vo) {
		return userRepository.updateUserInfo(vo);
	}

	public List<UserVo> getFriendNavi(Map<String, Object> param) {
		
		return userRepository.friendNaviList(param);
	}

	public String getFriendStatus(Map<String, Object> param) {
		
		return userRepository.friendStatus(param);
	}

	public String getFriendNo(Map<String, Object> param) {
		
		return userRepository.friendNo(param);
	}
  
}
