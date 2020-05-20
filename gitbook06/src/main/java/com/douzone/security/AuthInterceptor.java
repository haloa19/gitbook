package com.douzone.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.douzone.gitbook.vo.UserVo;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 1. handler 종류 확인
		if (handler instanceof HandlerMethod == false) {
			// asset 접근 defaultServletHandler 가 처리하는 경우 ( 보통. assets 의 정적 자원 접근)
			return true;
		}
		// 2. casting
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		
		// 3. Method의 @Auth 받아오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		
		
		// 4. Method에 @Auth가 없으면 Type 에 붙어있는지 확인해야한다.(과제)
		
		if (auth == null) {
			auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
		}

		// 4-2 Type 이나 메소드에 둘 다 @Auth 가 적용이 안되어 있는 경우
		if (auth == null) {

			return true;
		}

		// 5. @Auth가
		HttpSession session = request.getSession();
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}

		UserVo authUser = (UserVo) session.getAttribute("authUser");

		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}

		// 6 권한(Authorization) 체크를 위해서 @Auth role 가져오기
		String role = auth.value();
		System.out.println("role:" + role);

		if ("USER".equals(role)) {// 7. @Auth의 role 이 "USER" 인 경우에는 authUser의 role "USER" 이든 "ADMIN" 이든 상관이 없음
			return true;
		} 
		
		
		
		// @Auth의 role => "ADMIN"
		// authUser의 권한이 
		// 인증확인 되었으므로 핸들러 메소드 실행
		return true;
	}

}
