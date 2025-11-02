<%@ page contentType="text/html; charset=UTF-8"%>
<%
model.User me = (model.User) request.getAttribute("me");
String ctx = request.getContextPath();
if (me == null) {
	response.sendRedirect(ctx + "/user/login.jsp");
	return;
}
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet" href="<%=ctx%>/resources/css/user.css">
</head>
<body>
	<h1>마이페이지</h1>

	<div class="card">
		<p>
			<strong>아이디</strong> :
			<%=me.getUser_id()%></p>
		<p>
			<strong>이름</strong> :
			<%=me.getName()%></p>
		<p>
			<strong>닉네임</strong> :
			<%=me.getNickname()%></p>
		<p>
			<strong>이메일</strong> :
			<%=me.getEmail()%></p>
		<p>
			<strong>휴대폰</strong> :
			<%=me.getPhone() == null ? "" : me.getPhone()%></p>
		<p>
			<strong>생년월일</strong> :
			<%=me.getBirth() == null ? "" : me.getBirth()%></p>
		<p>
			<strong>성별</strong> :
			<%=me.getGender() == null ? "" : me.getGender()%></p>
		<p>
			<strong>주소</strong> :
			<%=me.getAddress() == null ? "" : me.getAddress()%></p>
	</div>

	<p class="actions">
		<a href="<%=ctx%>/user/edit">정보 수정</a> | <a
			href="<%=ctx%>/password/change">비밀번호 변경</a>
	</p>

	<form method="post" action="<%=ctx%>/logout">
		<button type="submit">로그아웃</button>
	</form>
</body>
</html>
