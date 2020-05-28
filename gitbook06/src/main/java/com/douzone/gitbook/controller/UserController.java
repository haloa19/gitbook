package com.douzone.gitbook.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douzone.gitbook.dto.JsonResult;
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

	@ResponseBody
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
	public JsonResult checkEmail(@RequestParam(value = "email", required = true, defaultValue = "") String email, @RequestParam(value = "random", required = true, defaultValue = "0") String random,
			HttpServletRequest req) {
		System.out.println("email: " + email + "  //  random: " + random);
		
		Boolean isPresent = userService.getEmailStatus(email);
		if (isPresent == false) {
			return JsonResult.fail("not available email");
		}

		// 사용자에게 이메일을 보내기
		int ran = new Random().nextInt(900000) + 100000;
		HttpSession session = req.getSession(true);

		// authCode & random(페이지) 두 값들 모두 session에 넣을것
		String authCode = String.valueOf(ran);
		session.setAttribute("authCode", authCode);
		session.setAttribute("random", Integer.parseInt(random));

		// 메일에 보낼 내용 정의
		String subject = "회원가입 인증 코드 발급 안내 입니다.";
		StringBuilder sb = new StringBuilder();
		sb.append("귀하의 인증 코드는 " + authCode + "입니다.");

		boolean isSent = mailService.send(subject, sb.toString(), "bigbossdc200@gmail.com", email, null);
		return (isSent ? JsonResult.success(true) : JsonResult.fail("failed for sending email"));
	}

	@ResponseBody
	@RequestMapping(value = "/checkAuth", method = RequestMethod.POST)
	public JsonResult checkAuth(@RequestParam(value = "random", required = true, defaultValue = "0") String random,
			@RequestParam(value = "authCode", required = true, defaultValue = "0") String authCode, HttpSession session) {
		
		// HttpSession 객체로부터 "authCode"와 "random" 불러오기
		String originalJoinCode = (String) session.getAttribute("authCode");
		String originalRandom = Integer.toString((int) session.getAttribute("random"));
		
		System.out.println("originalJoinCode: " + originalJoinCode + "  //  originalRandom: " + originalRandom);
		System.out.println("input >> authCode: " + authCode + "  //  random: " + random);
		
		if("0".equals(authCode) || "0".equals(random)) {
			return JsonResult.fail("authentication not matched");
		}
		if (originalJoinCode.equals(authCode) && originalRandom.equals(random)) {
			System.out.println("Authentication succeess!!!");
			return JsonResult.success(true);
		} else {
			return JsonResult.fail("authentication not matched");
		}
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
