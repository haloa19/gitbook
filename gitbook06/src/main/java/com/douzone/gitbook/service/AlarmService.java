package com.douzone.gitbook.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.douzone.gitbook.repository.AlarmRepository;
import com.douzone.gitbook.vo.AlarmVo;

@Service
public class AlarmService {
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	public void sendAlarm(String message, String id) {
		// "/topic/alarm/유저아이디" 로 해당 알림 메시지를 날린다.
		webSocket.convertAndSend("/topics/alarm/" + id, message);
	}

	public List<AlarmVo> getAlarmList(String id) {
		return alarmRepository.findList(id);
	}

	public Boolean markAsRead(Map<String, Object> input) {
		return alarmRepository.markRead(input);
	}

}
