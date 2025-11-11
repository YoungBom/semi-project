<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>아이디 찾기</title>
<link href="${ctx}/resources/css/user.css" rel="stylesheet">
<link href="${ctx}/resources/css/main.css" rel="stylesheet">
</head>
<body>
	<%@ include file="/include/header.jsp"%>
	<h1>아이디 찾기</h1>

	<form method="post" action="${ctx}/user/find_id">
		<label>이메일 <input type="email" name="email" required
			maxlength="255" placeholder="가입 시 사용한 이메일">
		</label> <label>닉네임 <input type="text" name="nickname" required
			maxlength="100" placeholder="닉네임">
		</label>

		<div class="actions">
			<button type="submit">아이디 찾기</button>
			<a class="btn-link" href="${ctx}/user/login.jsp">로그인으로</a>
		</div>
	</form>

	<c:if test="${not empty requestScope.error}">
		<p class="error">${requestScope.error}</p>
	</c:if>
</body>
</html>
