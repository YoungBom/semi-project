<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>아이디 찾기 결과</title>
<link href="${ctx}/resources/css/user.css" rel="stylesheet">
<link href="${ctx}/resources/css/recover.css" rel="stylesheet">
</head>
<body>
	<%@ include file="/include/header.jsp"%>

	<main class="auth-wrap">
		<section class="auth-card auth-result">
			<h1>아이디 찾기 결과</h1>

			<c:choose>
				<c:when test="${not empty maskedUserId}">
					<p>
						회원님의 아이디는 <strong>${fn:escapeXml(maskedUserId)}</strong> 입니다.
					</p>
				</c:when>
				<c:otherwise>
					<p class="auth-error">일치하는 회원 정보를 찾을 수 없습니다.</p>
				</c:otherwise>
			</c:choose>

			<div class="auth-actions">
				<a class="auth-btn" href="${ctx}/user/login.jsp">로그인</a> <a
					class="auth-btn secondary" href="${ctx}/user/find_password.jsp">비밀번호
					찾기</a>
			</div>

		</section>
	</main>
</body>
</html>
