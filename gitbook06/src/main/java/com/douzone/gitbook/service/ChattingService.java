package com.douzone.gitbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.ChattingRepository;
import com.douzone.gitbook.vo.ChattingMsgVo;
import com.douzone.gitbook.vo.ChattingRoomVo;

@Service
public class ChattingService {


	@Autowired
	private ChattingRepository chattingRepository;

	public void addchatRoom(ChattingRoomVo vo) {
		chattingRepository.addchatRoom(vo);
		
	}

	public void addChatRoomuser(ChattingRoomVo vo) {
		chattingRepository.addChatRoomuser(vo);
		
	}

	public void addAdminMsg(ChattingMsgVo msgVo) {
		chattingRepository.addAdminMsg(msgVo);
		
	}

	public List<ChattingRoomVo> chatRoomList(Long userNo) {
		return chattingRepository.chatRoomList(userNo);
	}

	public ChattingMsgVo getAdminImage(Long chatRoonNo) {
		
		return chattingRepository.getAdminImage(chatRoonNo);
	}

	public ChattingMsgVo getLastMsg(Long chatRoonNo) {
		
		return chattingRepository.getLastMsg(chatRoonNo);
	}
}
