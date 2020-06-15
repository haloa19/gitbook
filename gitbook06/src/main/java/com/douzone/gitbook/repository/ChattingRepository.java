package com.douzone.gitbook.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.gitbook.vo.ChattingMsgVo;
import com.douzone.gitbook.vo.ChattingRoomVo;

@Repository
public class ChattingRepository {

	@Autowired
	private SqlSession sqlSession;

	public void addchatRoom(ChattingRoomVo vo) {
		sqlSession.insert("chatting.addchatRoom",vo);	
	}

	public void addChatRoomuser(ChattingRoomVo vo) {
		sqlSession.insert("chatting.addChatRoomuser",vo);	
		
	}

	public void addAdminMsg(ChattingMsgVo msgVo) {
		sqlSession.insert("chatting.addAdminMsg",msgVo);
		
	}

	public List<ChattingRoomVo> chatRoomList(Long userNo) {
		
		return sqlSession.selectList("chatting.chatRoomList", userNo);
	}

	public ChattingMsgVo getAdminImage(Long chatRoonNo) {
	
		return sqlSession.selectOne("chatting.getAdminImage", chatRoonNo);
	}

	public ChattingMsgVo getLastMsg(Long chatRoonNo) {
	
		return sqlSession.selectOne("chatting.getLastMsg", chatRoonNo);
	}

	public List<ChattingMsgVo> inviteList(Long chatRoonNo) {
		
		return sqlSession.selectList("chatting.inviteList", chatRoonNo);
	}

	public List<ChattingMsgVo> msgList(Long chatRoonNo) {
		
		return sqlSession.selectList("chatting.msgList",chatRoonNo);
	}

	public void addUserMsg(ChattingMsgVo msgVo) {
		sqlSession.insert("chatting.addUserMsg",msgVo);
	
	}

	public void addCheckMsg(ChattingMsgVo msgVo) {
		sqlSession.insert("chatting.addCheckMsg",msgVo);
		
	}

	public String getSendDate(Long no) {
		
		return sqlSession.selectOne("chatting.getSendDate", no);
	}


}
