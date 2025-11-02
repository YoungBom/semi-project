<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/user.css">
</head>
<body>
	<h1>로그인</h1>

	<div role="alert" class="msg error">
		<%=request.getAttribute("error") == null ? "" : request.getAttribute("error")%>
	</div>
	<div role="status" class="msg">
		<%=request.getAttribute("msg") == null ? "" : request.getAttribute("msg")%>
	</div>

	<form method="post" action="<%=request.getContextPath()%>/login"
		autocomplete="on">
		<label> 아이디 <input type="text" name="user_id" required
			maxlength="255" placeholder="아이디를 입력하세요" autocomplete="username">
		</label> <label> 비밀번호 <input type="password" name="user_pw" required
			minlength="8" maxlength="255" placeholder="비밀번호를 입력하세요"
			autocomplete="current-password">
		</label> <label class="inline"> <input type="checkbox"
			name="remember_me" value="1"> 로그인 상태 유지(선택)
		</label>

		<div class="actions">
			<button type="submit">로그인</button>
		</div>
	</form>

	<p class="sub">
		<a href="<%=request.getContextPath()%>/user/register.jsp">회원가입</a> · <a
			href="<%=request.getContextPath()%>/password/forgot">아이디/비밀번호 찾기</a>
		<!-- 나중에 연결 -->
	</p>
</body>
</html>
