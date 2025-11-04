<%@ page contentType="text/html; charset=UTF-8"%>
<%
model.User me = (model.User) request.getAttribute("me");
String ctx = request.getContextPath();
if (me == null) { 
	response.sendRedirect(ctx + "/edit");
	return;
}
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

	<div class="msg error"><%=request.getAttribute("error") == null ? "" : request.getAttribute("error")%></div>

	<!-- action 경로 수정 -->
	<form method="post" action="<%=ctx%>/edit" autocomplete="off">
		<label>이메일 <input type="email" name="email"
			value="<%=me.getEmail()%>" required maxlength="255">
		</label> <label>닉네임 <input type="text" name="nickname"
			value="<%=me.getNickname()%>" required maxlength="255">
		</label> <label>휴대폰 <input type="text" name="phone"
			value="<%=me.getPhone() == null ? "" : me.getPhone()%>" maxlength="20"
			pattern="^[0-9\\-+ ]{9,20}$">
		</label> <label>생년월일 <input type="text" name="birth"
			value="<%=me.getBirth() == null ? "" : me.getBirth()%>"
			placeholder="YYYY-MM-DD">
		</label> <label>성별 <select name="gender">
				<option value=""
					<%=(me.getGender() == null || me.getGender().isEmpty()) ? "selected" : ""%>>선택</option>
				<option value="남" <%="남".equals(me.getGender()) ? "selected" : ""%>>남</option>
				<option value="여" <%="여".equals(me.getGender()) ? "selected" : ""%>>여</option>
		</select>
		</label> <label>주소 <input type="text" name="address"
			value="<%=me.getAddress() == null ? "" : me.getAddress()%>" maxlength="255">
		</label>

		<div class="actions">
			<button type="submit">저장</button>
			<a href="<%=ctx%>/mypage">취소</a>
		</div>
	</form>
</body>
</html>
