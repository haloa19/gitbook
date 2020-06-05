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
import com.douzone.gitbook.service.MailService;
import com.douzone.gitbook.service.UserService;
import com.douzone.gitbook.util.LinuxServer;
import com.douzone.gitbook.vo.UserVo;

@Controller("UserController")
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	MailService mailService;


	@RequestMapping("/join")
	public String join(Model model) {
		int random = new Random().nextInt(900000) + 100000;
		model.addAttribute("random", random);
		return "user/join";
	}

	@RequestMapping(value = "/joinProcess", method = RequestMethod.POST)
	public String joinProcess(HttpServletRequest request, HttpSession session) {
		UserVo vo = new UserVo();
		vo.setId(request.getParameter("email"));
		vo.setPassword(request.getParameter("password"));
		vo.setPhone(request.getParameter("phone"));
		vo.setName(request.getParameter("name"));
		vo.setBirthday(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day") + " 00:00:00");
		vo.setGender(request.getParameter("gender"));

		System.out.println(vo);

		if (userService.addUser(vo) == false) {
			System.out.println("[ERROR] DB에 업로드 실패...");
			return "user/join";
		}

		// 경로 생성
		if ("ERROR".equalsIgnoreCase(LinuxServer.getResult("sudo mkdir /var/www/git/gitbook/" + vo.getId()))) {
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
	public String findID(Model model) {
		return "user/findID";
	}
	
	@RequestMapping(value = "/findIDProcess", method = RequestMethod.POST)
	public String findIDProcess(HttpServletRequest request, Model model) {
		UserVo vo = new UserVo();
		vo.setPhone(request.getParameter("phone"));
		vo.setName(request.getParameter("name"));
		vo.setBirthday(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day") + " 00:00:00");
		System.out.println("INPUT >> " + vo.getPhone() + " / " + vo.getName() + " / " + vo.getBirthday());
		
		String email = userService.getEmail(vo);
		System.out.println("OUTPUT >> " + email);
		
		if(email == null || "".equals(email)) {
			model.addAttribute("vo", vo);
			return "user/findID";
		} else {
			model.addAttribute("email", email);
			return "user/findIDSuccess";
		}
		
	}

	@RequestMapping("/findPW")
	public String findPW(Model model) {
		int random = new Random().nextInt(900000) + 100000;
		model.addAttribute("random", random);
		return "user/findPW";
	}

	@RequestMapping(value = "/findPWAuth", method = RequestMethod.POST)
	public String findPWAuth(
			@RequestParam(value = "email", required = true, defaultValue = "") String email, 
			@RequestParam(value = "random", required = true, defaultValue = "") String random, 
			HttpServletRequest req,
			Model model) {
		System.out.println("INPUT >> email: " + email + "  //  random : " + random);
		// 파라비터가 제대로 안 올 경우
		if("".equals(email) || "".equals(random)) {
			model.addAttribute("random", random);
			model.addAttribute("vo", new UserVo());
			return "user/findPW";
		}
		
		// 존재하지 않는 이메일인 경우
		if(userService.getEmailExistance(email) == false) {
			model.addAttribute("random", random);
			model.addAttribute("vo", new UserVo());
			return "user/findPW";
		}
		
		// authCode을 새로 만들어 random과 같이 session에 넣어주기
		HttpSession session = req.getSession(true);
		int ran = new Random().nextInt(900000) + 100000;
		String authCode = String.valueOf(ran);
		
		session.setAttribute("authCode", authCode);
		session.setAttribute("random", random);
		
				
		// 이메일을 보내기
		String returnedLink = "http://192.168.1.15:8080" + req.getContextPath() + "/user/findPWChange?email=" + email + "&random=" + random + "&authCode=" + authCode;
		String subject = "비밀번호 변경을 위한 인증 링크 입니다.";
		StringBuilder sb = new StringBuilder();
		sb.append("귀하의 인증 링크: " + returnedLink);
		
		System.out.println("Sending email");
		mailService.sendAsyncMethodCall(subject, sb.toString(), "bigbossdc200@gmail.com", email, null);
		
		// 최종적으로 email 링크와 사이트 링크를 보내기
		model.addAttribute("email", email);
		model.addAttribute("webpage", "http://www." + email.split("@")[1]);
		
		// 모두 충족하면 목표 페이지를 띄우기
		return "user/findPWAuth";
	}

	@RequestMapping(value = "/findPWChange", method = RequestMethod.GET)
	public String findPWChange(
			@RequestParam(value = "email", required = true, defaultValue = "") String email,
			@RequestParam(value = "random", required = true, defaultValue = "") String random,
			@RequestParam(value = "authCode", required = true, defaultValue = "") String authCode, HttpSession session, Model model) {
		// (1) session이 없거나, (2) @RequestParam 값들이 없을 경우? ==> reject하기 !!
		if(session == null || "".equals(authCode) || "".equals(random) || "".equals(email)) {
			return "redirect:/";
		}

		// HttpSession 객체로부터 "authCode"와 "random" 불러오기
		String originalAuthCode = (String) session.getAttribute("authCode");
		String originalRandom = (String) session.getAttribute("random");
		
		if(originalAuthCode == null || originalRandom == null || "".equals(originalAuthCode) || "".equals(originalRandom)) {
			return "redirect:/";
		}

		System.out.println("SESSION >> originalAuthCode: " + originalAuthCode + "  //  originalRandom: " + originalRandom);
		System.out.println("PARAMETER >> authCode: " + authCode + "  //  random: " + random);

		// session에 있는 2개 값들과 @RequestParam 에 있는 2개 값들과 비교하기
		if (originalAuthCode.equals(authCode) == false || originalRandom.equals(random) == false) {
			return "redirect:/";
		}
		
		// 모두 충족하면 목표 페이지를 띄우기
		model.addAttribute("email", email);
		return "user/findPWChange";
	}
	

	@RequestMapping(value = "/PWChangeProcess", method = RequestMethod.POST)
	public String PWChangeProcess(
			@RequestParam(value = "id", required = true, defaultValue = "") String email,
			@RequestParam(value = "password", required = true, defaultValue = "") String password,
			HttpSession session, Model model) {
		
		System.out.println("email : " + email + "  //  password : " + password);
		
		// 비밀번호를 sql에서 업데이트
		UserVo vo = new UserVo();
		vo.setId(email);
		vo.setPassword(password);
		Boolean isChanged = userService.updatePassword(vo);
		if(!isChanged) {
			model.addAttribute("vo", vo);
			model.addAttribute("email", email);
			return "user/findPWChange"; 
		}
		
		// 마지막으로 session을 정리한다
		session.removeAttribute("random");
		session.removeAttribute("authCode");
		session.invalidate();
		
		return "redirect:/";
	}


}
