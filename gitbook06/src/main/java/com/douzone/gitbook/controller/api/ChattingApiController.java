package com.douzone.gitbook.controller.api;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
import com.douzone.gitbook.service.ChattingService;
import com.douzone.gitbook.vo.ChattingMsgVo;
import com.douzone.gitbook.vo.ChattingRoomVo;


@Controller("ChattingApiController")
@RequestMapping("/chatting/api")
public class ChattingApiController {
	
	@Autowired
	private ChattingService chattingService;
	

	
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
		ChattingMsgVo msgVo= new ChattingMsgVo();
		msgVo.setChattingNo(vo.getNo());
		msgVo.setContents(newChatName+"(가)이 생성 되었습니다");
		System.out.println("msgvo"+msgVo);
		chattingService.addAdminMsg(msgVo);
		
		List<ChattingRoomVo> list= chattingService.chatRoomList(userNo);
		
		
		return JsonResult.success(list);
	}
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
			@PathVariable Long chatRoonNo
		
			) {
		Map<String,Object> map = new HashMap<>();
		map.put("image",chattingService.getAdminImage(chatRoonNo).getImage());
		map.put("lastMsg",chattingService.getLastMsg(chatRoonNo));
	
	
		return JsonResult.success(map);
	}

}
