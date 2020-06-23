<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<link rel="icon" href="%PUBLIC_URL%/favicon.ico" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="theme-color" content="#000000" />
<meta name="description"
	content="Web site created using create-react-app" />
<link rel="apple-touch-icon" href="%PUBLIC_URL%/logo192.png" />

<link rel="manifest" href="%PUBLIC_URL%/manifest.json" />


<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<meta property="og:title" content="" />
<meta property="og:url" content="" />
<meta property="og:description" content="" />

<link rel="icon"
	href="${pageContext.request.contextPath}/assets/img/logo.jpg" />
<link rel="apple-touch-icon" href="img/favicons/apple-touch-icon.png" />
<link rel="apple-touch-icon" sizes="72x72"
	href="img/favicons/apple-touch-icon-72x72.png" />
<link rel="apple-touch-icon" sizes="114x114"
	href="img/favicons/apple-touch-icon-114x114.png"></link>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous"></link>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">


<link type="text/css"
	href="${pageContext.request.contextPath}/assets/css/demos/photo.css"
	rel="stylesheet" />
<link type="text/css"
	href="${pageContext.request.contextPath}/assets/css/demos/join.css"
	rel="stylesheet" />




<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<!--script src="assets/js/modernizr-custom.js"></script-->


<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/base.js"></script>
<script src="https://kit.fontawesome.com/81c2c05f29.js" crossorigin="anonymous"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<!-- 따로 작성한 코드 -->
<script type="text/javascript">
//이메일 형식 check를 위한 함수
function checkEmail(str) {
	var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
	if (!reg_email.test(str)) {
		return false;
	} else {
		return true;
	}
}

// jQuery 구문 영역
$(function() {
	$("#join-form").submit(function(event) {
		event.preventDefault();
		
		if($("#input_email").val() == ''){
			alert('이메일을 입력 후 인증해 주세요!');
			$("#input_email").focus();
			return;
		}
		
		if($("#emailAuth_confirmed").is(":hidden")){
			alert('이메일이 인증되지 않았습니다!');
			return;
		}
		
		if($("#input_password").val() == '' || $("#input_password").val().length < 8){
			alert('비밀번호를 8자 이상 입력해 주세요!');
			$("#input_password").focus();
			return;
		}
		
		if($("#input_password_again").val() != $("#input_password").val()){
			alert('비밀번호 확인을 잘못 입력하였습니다!');
			$("#input_password_again").focus();
			return;
		}
		
		if($("#input_phone").val() == '' || $.isNumeric($("#input_phone").val()) == false || $("#input_phone").val().length < 10){
			alert('전화번호를 10자 이상 숫자로만 입력해 주세요!');
			$("#input_phone").focus();
			return;
		}
		
		if($("#input_username").val() == ''){
			alert('이름을 입력해 주세요!');
			$("#input_username").focus();
			return;
		}
		
		this.submit();
	});

	// 이메일을 입력하는 순간부터...
	$("#input_email").change(function() {
		$("#input_authCode").val('');
		$("#emailAuth_confirmed").hide();
		$("#emailAuth_form").hide();
	});

	// 이메일 확인 버튼을 눌렀을 경우
	$(document).on("click", "#emailBtn", function() {
		var email = $("#input_email").val();
		var random = $("#random").val();
		if (!random) {
			alert("error with auth configuration...(random is null)");
			return;
		}
		if (!email) {
			alert("이메일 주소를 입력해주세요!");
			$("#input_email").focus();
			return;
		}
		if (!checkEmail(email)) {
			alert("이메일 형식에 맞추어 다시 입력해주세요!");
			$("#input_email").focus();
			return;
		}
		
		$("#emailAuth_form").show();
		alert("이메일 인증을 시작합니다. 1분 이내로 인증메일을 못 받은 경우 다른 이메일로 시도해주세요.");

		// 이메일 인증을 위한 임시 번호 생성 및 이메일 전송
		$.ajax({
			type : "post",
			url : '${pageContext.request.contextPath}/user/checkEmail',
			async : true,
			data : "email=" + email + "&random=" + random,
			success : function(response) {
				if (response.result == "fail") {
					console.log(response);
					if (response.message == "not available email") {
						alert("사용할 수 없는 이메일입니다.");
						$("#emailAuth_confirmed").hide();
						$("#emailAuth_form").hide();
						$("#input_email").focus();
					}
					if (response.message == "failed for sending email") {
						alert("사용자에게 이메일을 보내지 못했습니다.");
						$("#emailAuth_confirmed").hide();
						$("#emailAuth_form").hide();
						$("#input_email").focus();

					}
					return;
				}
			},
			error : function(xhr, status, error) {
				alert("에러발생");
				$("#emailAuth_confirmed").hide();
				$("#emailAuth_form").hide();
				$("#input_email").focus();
			}
		});
	});

	// 이메일 인증번호를 확인하는 경우
	$(document).on("click", "#emailAuthBtn", function() {
		var authCodeInput = $("#input_authCode").val();
		var random = $("#random").val();
		if (!authCodeInput) {
			alert("인증 번호를 입력해주세요!");
			$("#input_authCode").focus();
			return;
		}

		// 이메일 인증 과정 거치기
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/user/checkAuth",
			async : true,
			data : "random=" + random + "&authCode=" + authCodeInput,
			success : function(response) {
				if (response.result == "fail") {
					console.log(response);
					if (response.message == "authentication not matched") {
						alert("인증 번호가 다릅니다");
						$("#input_authCode").focus();
					}
				}
				else if(response.result == "success"){
					alert("인증이 완료되었습니다!");
					// jquery 실행
					$("#emailAuth_form").hide();
					$("#emailAuth_confirmed").show();
				}
				
			},
			error : function(xhr, status, error) {
				alert("에러발생");
				$("#input_authCode").focus();
			}
		});
	});
	
});
</script>


<title>GitBook</title>
</head>
<body>
	<noscript>You need to enable JavaScript to run this app.</noscript>
	<div id="root">
		<div class="login">
			<div class="container">
				<div class="banner-content">
					<h1>
						<a href="${pageContext.request.contextPath}/">GitBook</a>
					</h1>

					<div>
						<form method="post" class="form-signin-gitbook" id="join-form" action="${pageContext.request.contextPath }/user/joinProcess">
							<div class="form-group-join">
								<input name="email" type="text" class="form-control-join-email"
									id="input_email" placeholder="이메일" />
								<button type="button" class="kafe-btn kafe-btn-mint form-group-join-btn"
									id="emailBtn" value="인증">인증</button>
								<input type="hidden" id="random" value="${random }" />
							</div>
							
							<!-- 인증 완료시 $('#emailAuth_confirmed') 띄우기-->
							<div class="form-group-join" id="emailAuth_form" style="display: none">
								<input name="confirm" type="text"
									class="form-control-join-email" id="input_authCode"
									placeholder="인증번호" />
								<input type="button" class="kafe-btn kafe-btn-mint form-group-join-btn"
									id="emailAuthBtn" value="확인" />
							</div>
							<div class="form-group-join" id="emailAuth_confirmed" style="display: none">
								<label style="color: white">인증이 완료되었습니다!</label>
							</div>
							
							<div class="form-group-join">
								<input name="password" type="password" class="form-control-join" id="input_password"
									placeholder="비밀번호" />
							</div>
							<div class="form-group-join">
								<input name="password_confirm" type="password" class="form-control-join" id="input_password_again"
									placeholder="비밀번호 확인" />
							</div>
							<div class="form-group-join">
								<input name="phone" type="tel" class="form-control-join" id="input_phone"
									placeholder="휴대폰번호" />
							</div>
							<div class="form-group-join">
								<input name="name" type="text" class="form-control-join" id="input_username"
									placeholder="이름" />
							</div>
							<div class="form-group-join">

								<select class="birth_info" name="year" type="checkbox" style="width: 100px">
									<c:forEach begin="${0}" end="${120}" var='k'>
										<option value="${2020-k}">${2020-k}</option>'
                    				</c:forEach>
								</select>
								<label class="birth_label" for="year">년</label>
								
								<select class="birth_info" name="month" type="checkbox">
									<c:forEach begin="${1}" end="${12}" var='k'>
										<option value="${k}">${k}</option>'
                    				</c:forEach>
								</select>
								<label class="birth_label" for="month">월</label>
								
								<select class="birth_info" name="day" type="checkbox">
									<c:forEach begin="${1}" end="${31}" var='k'>
										<option value="${k}">${k}</option>'
                    				</c:forEach>
								</select>
								<label class="birth_label" for="day">일</label>

							</div>
							<div class="form-group-join">
								<div class="chk_info">
									<input type="radio" name="gender" value="male" checked="checked" />
									<label for="gender">남자</label>
									<input type="radio" name="gender" value="female" />
									<label for="gender">여자</label>
								</div>
							</div>


							<a>
								<button class="kafe-btn kafe-btn-mint btn-block" type="submit" name="subm">가입하기</button>
							</a>
							<br />
							<a
								href="${pageContext.request.contextPath}/" class="btn btn-dark "
								role="button" style="margin-top: 10px">이미 GitBook회원이신가요? 지금
								로그인 하기</a>
							<br />

						</form>
					</div>


				</div>
			</div>
		</div>
	</div>

</body>
</html>
