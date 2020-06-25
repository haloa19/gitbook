package com.douzone.gitbook.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.ChattingRepository;
import com.douzone.gitbook.vo.ChattingMsgVo;
import com.douzone.gitbook.vo.ChattingRoomVo;
import com.douzone.gitbook.vo.UserVo;

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

	public List<ChattingMsgVo> inviteList(Long chatRoonNo) {
		return chattingRepository.inviteList(chatRoonNo);
	}

	public List<ChattingMsgVo> msgList(Long chatRoonNo) {
		return chattingRepository.msgList(chatRoonNo);
	}

	public void addUserMsg(ChattingMsgVo msgVo) {
		chattingRepository.addUserMsg(msgVo);
	}

	public void addCheckMsg(ChattingMsgVo msgVo) {
		chattingRepository.addCheckMsg(msgVo);
	}

	public String getSendDate(Long no) {
		return chattingRepository.getSendDate(no);
	}

	public Long getAlarmList(Long chatRoonNo, Long userNo) {
		return chattingRepository.getAlarmList(chatRoonNo, userNo);
	}

	public void updateResetAlarm(Long no, Long chatRoonNo) {
		chattingRepository.updateResetAlarm(no, chatRoonNo);
	}

	public void deleteChatRoom(Map<String, Long> map) {
		chattingRepository.deleteChatRoom(map);
	}

	public List<UserVo> getInviteList(Long chatRoonNo) {
		return chattingRepository.getInviteList(chatRoonNo);
	}

	public String getChatName(Long chatRoonNo) {
		return chattingRepository.getChatName(chatRoonNo);
	}
}
