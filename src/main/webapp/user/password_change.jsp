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
	<jsp:include page="/include/header.jsp" />

	<h1>비밀번호 변경</h1>
	<div class="msg error"><%=request.getAttribute("error") == null ? "" : request.getAttribute("error")%></div>

	<form method="post" action="<%=ctx%>/password/change"
		autocomplete="off">
		<label>현재 비밀번호 <input type="password" name="current_password"
			required minlength="8" maxlength="255"
			autocomplete="current-password">
		</label> <label>새 비밀번호 <input type="password" name="new_password"
			required minlength="8" maxlength="255" autocomplete="new-password">
		</label> <label>새 비밀번호 확인 <input type="password"
			name="new_password_confirm" required minlength="8" maxlength="255"
			autocomplete="new-password">
		</label>
		<div class="actions">
			<button type="submit">변경</button>
			<a href="<%=ctx%>/mypage">취소</a>
		</div>
	</form>
</body>
</html>
