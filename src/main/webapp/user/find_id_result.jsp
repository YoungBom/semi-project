<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>아이디 찾기 결과</title>
<link href="${ctx}/resources/css/user.css" rel="stylesheet">
</head>
<body>
	<h1>아이디 찾기 결과</h1>

	<c:choose>
		<c:when test="${not empty requestScope.maskedUserId}">
			<p>
				회원님의 아이디는 <strong>${requestScope.maskedUserId}</strong> 입니다.
			</p>
		</c:when>
		<c:otherwise>
			<p class="error">일치하는 회원 정보를 찾을 수 없습니다.</p>
		</c:otherwise>
	</c:choose>

	<div class="actions">
		<a class="btn" href="${ctx}/user/login.jsp">로그인</a> <a class="btn"
			href="${ctx}/user/find_password.jsp">비밀번호 찾기</a>
	</div>
</body>
</html>
