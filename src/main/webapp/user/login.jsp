<%@ page contentType="text/html; charset=UTF-8"%>
<%
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" href="<%=ctx%>/resources/css/user.css">
</head>
<body>
	<h1>로그인</h1>

	<%
	if (request.getAttribute("error") != null) {
	%>
	<div class="error"><%=request.getAttribute("error")%></div>
	<%
	}
	%>

	<form method="post" action="<%=ctx%>/login" autocomplete="on">
		<label>아이디 <input type="text" name="user_id" required
			autocomplete="username"></label><br> <label>비밀번호 <input
			type="password" name="user_pw" required
			autocomplete="current-password"></label><br>
		<button type="submit" class ="btn-login">로그인</button>
	</form>
</body>
</html>
