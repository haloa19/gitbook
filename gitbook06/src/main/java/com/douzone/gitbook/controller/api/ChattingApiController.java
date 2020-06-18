package com.douzone.gitbook.controller.api;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.ChattingService;
import com.douzone.gitbook.vo.ChattingMsgVo;
import com.douzone.gitbook.vo.ChattingRoomVo;
import com.douzone.gitbook.vo.UserVo;
import com.douzone.security.AuthUser;


@Controller("ChattingApiController")
@RequestMapping("/chatting/api")
public class ChattingApiController {
	
	@Autowired
	private ChattingService chattingService;
	@Autowired
	private SimpMessagingTemplate webSocket;
	

	
	@ResponseBody
	@RequestMapping(value="/addchatRoom/{userNo}")
	public JsonResult addchatRoom(
			@PathVariable Long userNo,
			@RequestParam(value="newChatName", required= true) String newChatName
			,
			@RequestParam(value="inviteList", required= true)ArrayList<Long> inviteList
			) {
		
		ChattingRoomVo vo= new ChattingRoomVo();
		vo.setUserNo(userNo);
		vo.setTitle(newChatName);
		vo.setGrant("admin");
		chattingService.addchatRoom(vo);
		chattingService.addChatRoomuser(vo);
		
		for(Long no :inviteList){
			ChattingRoomVo addVo=new ChattingRoomVo();
			addVo.setUserNo(no);
			addVo.setNo(vo.getNo());
			addVo.setGrant("user");
			chattingService.addChatRoomuser(addVo);
		}
		
		//채팅방 생성 시 첫 메시지 등록 
		ChattingMsgVo msgVo= new ChattingMsgVo();
		msgVo.setChattingNo(vo.getNo());
		msgVo.setContents(newChatName+"(가)이 생성 되었습니다");
		chattingService.addAdminMsg(msgVo);
		
		for(Long no :inviteList){
			List<ChattingRoomVo> list= chattingService.chatRoomList(no);
			webSocket.convertAndSend("/topics/chatting/resetChatRoom/"+no, list);
		}

		List<ChattingRoomVo> list= chattingService.chatRoomList(userNo);
		return JsonResult.success(list);
	}
	
//	@MessageMapping("/chatting/socketCreateCahtRoom") 
//	public void send(ChattingMsgVo msg) {
//		
//		webSocket.convertAndSend("/topics/chatting/3", msg); //react로 메세지 전송
//	}
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value="/chatRoomList/{userNo}")
	public JsonResult chatRoomList(
			@PathVariable Long userNo
		
			) {
	
		List<ChattingRoomVo> list= chattingService.chatRoomList(userNo);
		
		return JsonResult.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/chatRoomListItmeInfo/{chatRoonNo}")
	public JsonResult chatRoomListItmeInfo(
			@PathVariable Long chatRoonNo,
			@AuthUser UserVo userVo
		
			) {
		Map<String,Object> map = new HashMap<>();
		if(chattingService.getAdminImage(chatRoonNo)==null) {
			map.put("image",
					"/gitbook/assets/img/users/default.jpg");
		}else {
		map.put("image",
				chattingService.getAdminImage(chatRoonNo).getImage());
		}
		map.put("lastMsg",chattingService.getLastMsg(chatRoonNo));
		map.put("alarmCount", chattingService.getAlarmList(chatRoonNo,userVo.getNo()));
	
		return JsonResult.success(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/chatRoomInfo/{chatRoonNo}")
	public JsonResult chatRoomInfo(
			@PathVariable Long chatRoonNo,
			@AuthUser UserVo userVo
			) {
		
		Map<String,Object> map = new HashMap<>();
		map.put("inviteList",chattingService.inviteList(chatRoonNo));
		map.put("msgList",chattingService.msgList(chatRoonNo));
		chattingService.updateResetAlarm(userVo.getNo(),chatRoonNo);
		webSocket.convertAndSend("/topics/chatting/alarm/"+chatRoonNo+"/"+userVo.getNo(),
				chattingService.getAlarmList(chatRoonNo,userVo.getNo()));
		return JsonResult.success(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/sendMsg/{userNo}/{chatRoonNo}")
	public JsonResult chatRoomInfo(
			@PathVariable Long userNo,
			@PathVariable Long chatRoonNo,
			@RequestParam(value="contents", required= true) String contents
			,
			@RequestParam(value="inviteList", required= true)ArrayList<Long> inviteList
		
			) {
	
		ChattingMsgVo msgVo= new ChattingMsgVo();
		msgVo.setChattingNo(chatRoonNo);
		msgVo.setContents(contents);
		msgVo.setUserNo(userNo);
		msgVo.setReadMsg("yes");
		chattingService.addUserMsg(msgVo);
		msgVo.setSendDate(chattingService.getSendDate(msgVo.getNo()));
		chattingService.addCheckMsg(msgVo);//메시지알람테이블에 등록 
		webSocket.convertAndSend("/topics/chatting/lastMsg/"+chatRoonNo, 
				msgVo);
		
		webSocket.convertAndSend("/topics/chatting/test"+"/"+chatRoonNo, 
				chattingService.msgList(chatRoonNo));// 메시지 리스트를 새로 뿌려줌
	
		for(Long no :inviteList){
			if(no != userNo) {
				ChattingMsgVo alarmVo=  new ChattingMsgVo();
				alarmVo.setUserNo(no);
				alarmVo.setNo(msgVo.getNo());
				alarmVo.setReadMsg("no");
				chattingService.addCheckMsg(alarmVo);//메시지알람테이블에 등록
				webSocket.convertAndSend("/topics/chatting/alarm/"+chatRoonNo+"/"+no,
						chattingService.getAlarmList(chatRoonNo,no));
				
			}
			List<ChattingRoomVo> list= chattingService.chatRoomList(no);
			webSocket.convertAndSend("/topics/chatting/resetChatRoom/"+no, list);

		}
	
		
	
		return JsonResult.success(chattingService.msgList(chatRoonNo));
	}
	
	@ResponseBody
	@RequestMapping(value="/resetAlarm/{userNo}/{chatRoonNo}")
	public void resetAlarm(
			
			@PathVariable Long userNo,
			@PathVariable Long chatRoonNo
			) {
	
		chattingService.updateResetAlarm(userNo,chatRoonNo);
		webSocket.convertAndSend("/topics/chatting/alarm/"+chatRoonNo+"/"+userNo,
				chattingService.getAlarmList(chatRoonNo,userNo));
	}
	
	
	@ResponseBody
	@RequestMapping(value="/deleteChatRoom/{userNo}/{chatRoonNo}")
	public void deleteChatRoom(
			@PathVariable Long userNo,
			@PathVariable Long chatRoonNo,
			@RequestParam(value="nickName", required= true) String nickName
			,
			@RequestParam(value="inviteList", required= true)ArrayList<Long> inviteList
			) {
				
		System.out.println(userNo+":::::"+chatRoonNo+nickName+inviteList);
		Map<String,Long> map =new HashMap<String,Long>();
		map.put("userNo", userNo);
		map.put("chatRoonNo", chatRoonNo);
		chattingService.deleteChatRoom(map);
		
		ChattingMsgVo msgVo= new ChattingMsgVo();
		msgVo.setChattingNo(chatRoonNo);
		msgVo.setContents(nickName+"(가)이 퇴장 하였습니다");
		chattingService.addAdminMsg(msgVo);
		webSocket.convertAndSend("/topics/chatting/test"+"/"+chatRoonNo, 
				chattingService.msgList(chatRoonNo));// 메시지 리스트를 새로 뿌려줌
		for(Long no :inviteList){
			List<ChattingRoomVo> list= chattingService.chatRoomList(no);
			webSocket.convertAndSend("/topics/chatting/resetChatRoom/"+no, list);
			if(chattingService.getAdminImage(chatRoonNo)==null) {
				if(userNo != no) {
					webSocket.convertAndSend("/topics/chatting/imgagReset/"+chatRoonNo+"/"+no, 
							"/gitbook/assets/img/users/default.jpg");
					}
			}
		
		}
		webSocket.convertAndSend("/topics/chatting/test"+"/"+chatRoonNo, 
				chattingService.msgList(chatRoonNo));// 메시지 리스트를 새로 뿌려줌
		webSocket.convertAndSend("/topics/chatting/lastMsg/"+chatRoonNo, 
				chattingService.getLastMsg(chatRoonNo));
		webSocket.convertAndSend("/topics/chatting/changeInviteList/"+chatRoonNo, 
				chattingService.inviteList(chatRoonNo));
		
			
	}
	
	@ResponseBody
	@RequestMapping(value="/getInviteList/{chatRoonNo}")
	public JsonResult getInviteList(
			@PathVariable Long chatRoonNo
			) {
		List<UserVo> list = chattingService.getInviteList(chatRoonNo);
		
		return JsonResult.success(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/addinviteUser/{userNo}/{chatRoonNo}")
	public void addinviteUser(
			@PathVariable Long userNo,
			@PathVariable Long chatRoonNo,
			@RequestParam(value="inviteNicknameList", required= true) ArrayList<String> inviteNicknameList
			,
			@RequestParam(value="inviteList", required= true)ArrayList<Long> inviteList
			) {
		
		for(Long no :inviteList){
			
			ChattingRoomVo addVo=new ChattingRoomVo();
			addVo.setUserNo(no);
			addVo.setNo(chatRoonNo);
			addVo.setGrant("user");
			chattingService.addChatRoomuser(addVo);
			List<ChattingRoomVo> list= chattingService.chatRoomList(no);
			webSocket.convertAndSend("/topics/chatting/resetChatRoom/"+no, list);
			}
		for(String nickName :inviteNicknameList){
			ChattingMsgVo msgVo= new ChattingMsgVo();
			msgVo.setChattingNo(chatRoonNo);
			msgVo.setContents(nickName+"(가)이 초대 되었습니다");
			chattingService.addAdminMsg(msgVo);
			}
		
		webSocket.convertAndSend("/topics/chatting/test"+"/"+chatRoonNo, 
				chattingService.msgList(chatRoonNo));// 메시지 리스트를 새로 뿌려줌
		webSocket.convertAndSend("/topics/chatting/lastMsg/"+chatRoonNo, 
				chattingService.getLastMsg(chatRoonNo));
		webSocket.convertAndSend("/topics/chatting/changeInviteList/"+chatRoonNo, 
				chattingService.inviteList(chatRoonNo));
		
		
	}
	
	
}