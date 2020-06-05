package com.douzone.gitbook.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.util.LinuxServer;
import com.douzone.gitbook.vo.UserVo;


@Controller("UserController")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;


	@RequestMapping("/join")
	public String join(Model model) {
		int random = new Random().nextInt(900000) + 100000;
		model.addAttribute("random", random);
		return "user/join";
	}

	
	
	@RequestMapping(value="/joinProcess", method = RequestMethod.POST)
	public String joinProcess(HttpServletRequest request, HttpSession session) {
		UserVo vo = new UserVo();
		vo.setId(request.getParameter("email"));
		vo.setPassword(request.getParameter("password"));
		vo.setPhone(request.getParameter("phone"));
		vo.setName(request.getParameter("name"));
		vo.setBirthday(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day") + " 00:00:00");
		vo.setGender(request.getParameter("gender"));
		
		System.out.println(vo);
		
		if(userService.addUser(vo) == false) {
			System.out.println("[ERROR] DB에 업로드 실패...");
			return "user/join";
		}
		
		// 경로 생성
		if("ERROR".equalsIgnoreCase(LinuxServer.getResult("sudo mkdir /var/www/git/gitbook/" + vo.getId()))) {
			System.out.println("[ERROR] 리눅스 서버에 repository 경로 생성 실패...");
			return "user/join";
		}
		
		// 여기부터는 성공시 수행
		// HttpSession 객체 내의 "authCode"와 "random" 제거하기
		session.removeAttribute("authCode");
		session.removeAttribute("random");
		session.invalidate();
		
		return "redirect:/";
	}

	@RequestMapping("/findID")
	public String findID() {

		return "user/findID";
	}

	@RequestMapping("/findPW")
	public String findPW() {

		return "user/findPW";
	}

	@RequestMapping("/findIDSuccess")
	public String findIDSuccess() {

		return "user/findIDSuccess";
	}

	@RequestMapping("/findPWAuth")
	public String findPWAuth() {

		return "user/findPWAuth";
	}

	@RequestMapping("/findPWChange")
	public String findPWChange() {

		return "user/findPWChange";
	}
	
	

}
