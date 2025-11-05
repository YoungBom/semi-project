<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>회원가입</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/user.css">
</head>
<body>


	<h1>회원가입</h1>

	<div role="alert" class="msg error">
		<%=request.getAttribute("error") == null ? "" : request.getAttribute("error")%>
	</div>
	<div role="status" class="msg">
		<%=request.getAttribute("msg") == null ? "" : request.getAttribute("msg")%>
	</div>

	<form method="post" action="<%=request.getContextPath()%>/register"
		autocomplete="off">
		<label> 아이디(*) <input type="text" name="user_id" required
			maxlength="255" placeholder="로그인에 쓸 아이디" autocomplete="username">
		</label> <label> 비밀번호(*) <input type="password" name="user_pw"
			required minlength="8" maxlength="255" placeholder="8~20자 권장"
			autocomplete="new-password">
		</label> <label> 비밀번호 확인(*) <input type="password"
			name="user_pw_confirm" required minlength="8" maxlength="255"
			placeholder="비밀번호 재입력" autocomplete="new-password">
		</label> <label> 이메일(*) <input type="email" name="email" required
			maxlength="255" placeholder="example@domain.com" autocomplete="email">
		</label>

		<div class="row">
			<label> 이름(*) <input type="text" name="name" required
				maxlength="255" autocomplete="name">
			</label> <label> 성별(*) <select name="gender" required>
					<option value="">선택</option>
					<option value="남">남</option>
					<option value="여">여</option>
			</select>
			</label>
		</div>

		<div class="row">
			<label> 생년월일(*) <input type="date" name="birth" required>
			</label> <label> 휴대폰(*) <input type="text" name="phone"
				maxlength="20" placeholder="01000000000"
				pattern="^[0-9\-+ ]{9,20}$" autocomplete="tel">
			</label>
		</div>

		<label> 닉네임(*) <input type="text" name="nickname"
			maxlength="255" required autocomplete="nickname">
		</label> <label> 주소(선택) <input type="text" name="address"
			maxlength="255" autocomplete="street-address">
		</label>

		<div class="actions">
			<button type="submit">가입하기</button>
		</div>
	</form>

	<p class="sub" style="margin-top: 12px;">
		이미 계정이 있나요? <a href="<%=request.getContextPath()%>/user/login.jsp">로그인</a>
	</p>
</body>
</html>
