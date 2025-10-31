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

	<p><%=request.getAttribute("error") != null ? request.getAttribute("error") : ""%></p>
	<p><%=request.getAttribute("msg") != null ? request.getAttribute("msg") : ""%></p>

	<form method="post" action="<%=request.getContextPath()%>/login">
		<label>아이디 <input type="text" name="user_id" required
			maxlength="255" placeholder="아이디를 입력하세요">
		</label> <label>비밀번호 <input type="password" name="user_pw" required
			minlength="8" maxlength="255" placeholder="비밀번호를 입력하세요">
		</label>

		<div class="actions">
			<button type="submit">로그인</button>
		</div>
	</form>

	<p style="margin-top: 12px;">
		계정이 없으신가요? <a href="<%=request.getContextPath()%>/user/register.jsp">회원가입</a>
	</p>
</body>
</html>
