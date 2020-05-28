// 이메일 형식 check를 위한 함수
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
		
		if($("#input_password").val() == ''){
			alert('비밀번호를 입력해 주세요!');
			$("#input_password").focus();
			return;
		}
		
		if($("#input_password_again").val() == $("#input_password").val()){
			alert('비밀번호 확인을 잘못 입력하였습니다!');
			$("#input_password_again").focus();
			return;
		}
		
		if($("#input_phone").val() == ''){
			alert('전화번호를 입력해 주세요!');
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
					}
					if (response.message == "failed for sending email") {
						alert("사용자에게 이메일을 보내지 못했습니다.");
					}
					return;
				}

				// alert("사용가능한 이메일입니다. 인증번호를 입력해주세요.");
				// jquery 실행
				if(response.data == true){
					$("#emailAuth_form").show();
				}
			},
			error : function(xhr, status, error) {
				alert("에러발생");
			}
		});
	});

	// 이메일 인증번호를 확인하는 경우
	$(document).on("click", "#emailAuthBtn", function() {
		var authCodeInput = $("#input_authCode").val();
		var random = $("#random").val();
		if (!authCode) {
			alert("인증 번호를 입력해주세요!");
			$("#input_authCode").focus();
			return;
		}

		// 이메일 인증 과정 거치기
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath }/user/checkAuth",
			async : true,
			data : "random=" + random + "&authCodeInput=" + authCodeInput,
			success : function(response) {
				if (response.result == "fail") {
					console.log(response);
					if (response.message == "authentication not matched") {
						alert("인증 번호가 다릅니다");
					}
					return;
				}

				alert("인증이 완료되었습니다!");
				// jquery 실행
				$("#emailAuth_form").hide();
				$("#emailAuth_confirmed").show();
			},
			error : function(xhr, status, error) {
				alert("에러발생");
			}
		});
	})
});