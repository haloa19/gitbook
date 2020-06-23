<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding= "UTF-8" %>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <link rel="icon" href="%PUBLIC_URL%/favicon.ico" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="theme-color" content="#000000" />
    <meta
      name="description"
      content="Web site created using create-react-app"
    />
    <link rel="apple-touch-icon" href="%PUBLIC_URL%/logo192.png" />
 
    <link rel="manifest" href="%PUBLIC_URL%/manifest.json" />
   
    
      <meta charset="utf-8"/>
      <meta http-equiv="X-UA-Compatible" content="IE=edge"/>  
      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
      <meta name="description" content="" />
      <meta name="keywords" content="" />
      <meta property="og:title" content="" />
      <meta property="og:url" content="" />
      <meta property="og:description" content="" />

      <link rel="icon" href="${pageContext.request.contextPath}/assets/img/logo.jpg"/>
      <link rel="apple-touch-icon" href="img/favicons/apple-touch-icon.png"/>
      <link rel="apple-touch-icon" sizes="72x72" href="img/favicons/apple-touch-icon-72x72.png"/>
      <link rel="apple-touch-icon" sizes="114x114" href="img/favicons/apple-touch-icon-114x114.png"></link>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"></link>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
     
     
      <link type="text/css" href="${pageContext.request.contextPath}/assets/css/demos/photo.css" rel="stylesheet" />
      <link type="text/css" href="${pageContext.request.contextPath}/assets/css/demos/join.css" rel="stylesheet" />
      
      
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
      <!--script src="assets/js/modernizr-custom.js"></script-->
    
    
      <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
      <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
      <script src="${pageContext.request.contextPath}/assets/js/base.js"></script>
      <script src="https://kit.fontawesome.com/81c2c05f29.js" crossorigin="anonymous"></script>
      <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
      

        
    <title>GitBook</title>
  </head>
  <body>
    <noscript>You need to enable JavaScript to run this app.</noscript>
    <div id="root">
      <div class="login">
            <div class="container" >    
                <div class="banner-content">	   
                    <h1><a href="${pageContext.request.contextPath}">GitBook</a></h1>
     
     
      <form method="post" class="form-signin-gitbook" action="${ pageContext.request.contextPath }/main">
		   <h3 class="form-signin-heading"></h3>
		   <div class="form-group" >
		    <input name="id" type="text" class="form-control" placeholder="Email"></input>
		   </div>
		   <div class="form-group">
		    <input type="password" class="form-control" name="password" placeholder="Password"></input>
		   </div>
		   <button class="kafe-btn kafe-btn-mint btn-block" type="submit" name="subm">Sign in</button>
		   <br/>
		   <c:if test="${not empty userVo}">
				<p style="color:red">로그인이 실패 했습니다.</p>

			</c:if>
			
		   <a href="${pageContext.request.contextPath}/user/join"  class="btn btn-dark " role="button" style="margin-top:10px">아직 gitbook의 회원이 아니십니까? click!!</a>
		   <a href="${pageContext.request.contextPath}/user/findID" class="btn btn-dark " role="button">아이디 찾기</a>
          
           <a href="${pageContext.request.contextPath}/user/findPW" class="btn btn-dark " role="button">비밀번호 찾기</a>
		  </form>
    </div>
    
      </div>
            </div>
        </div> 

  </body>
</html>
