<%@ page contentType="text/html; charset=UTF-8"%>
<%
model.User me = (model.User) request.getAttribute("me");
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
<link rel="stylesheet" href="<%=ctx%>/resources/css/user.css">
</head>
<body>
	<h1>회원정보 수정</h1>
	<form method="post" action="<%=ctx%>/user/edit">
		<label>이메일 <input type="email" name="email"
			value="<%=me.getEmail()%>" required></label><br> <label>닉네임
			<input name="nickname"
			value="<%=me.getNickname() == null ? "" : me.getNickname()%>" required>
		</label><br> <label>휴대폰 <input name="phone"
			value="<%=me.getPhone() == null ? "" : me.getPhone()%>"></label><br> <label>생년월일
			<input type="date" name="birth"
			value="<%=me.getBirth() == null ? "" : me.getBirth()%>">
		</label><br> <label>성별 <select name="gender">
				<option value="" disabled>선택</option>
				<option value="남" <%="남".equals(me.getGender()) ? "selected" : ""%>>남성</option>
				<option value="여" <%="여".equals(me.getGender()) ? "selected" : ""%>>여성</option>
		</select>
		</label><br> <label>주소 <input name="address"
			value="<%=me.getAddress() == null ? "" : me.getAddress()%>"></label><br>
		<div class="actions">
			<button type="submit">저장</button>
			<a href="<%=ctx%>/user/mypage">취소</a>
		</div>
	</form>
</body>
</html>
