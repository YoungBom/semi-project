<%@ page contentType="text/html; charset=UTF-8"%>
<%
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>비밀번호 변경</title>
<link rel="stylesheet" href="<%=ctx%>/resources/css/user.css">
</head>
<body>
	<h1>비밀번호 변경</h1>

	<%
	if (request.getAttribute("error") != null) {
	%>
	<div class="error"><%=request.getAttribute("error")%></div>
	<%
	}
	%>

	<form method="post" action="<%=ctx%>/password/change">
		<label>현재 비밀번호 <input type="password" name="current_pw"
			required></label><br> <label>새 비밀번호 <input
			type="password" name="new_pw" required></label><br> <label>새
			비밀번호 확인 <input type="password" name="new_pw2" required>
		</label><br>
		<button type="submit">변경</button>
		<a href="<%=ctx%>/user/mypage">취소</a>
	</form>
</body>
</html>
