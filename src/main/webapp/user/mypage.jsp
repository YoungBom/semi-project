<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/user.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

</head>
<body class="mt-3">
	<%@ include file="/include/header.jsp" %>
	<main class="profile-wrap">
		<h1 class="profile-title">🍔 마이페이지</h1>

		<c:if test="${not empty error}">
			<div class="alert error">${error}</div>
		</c:if>

		<section class="profile-card">
			<div class="profile-row">
				<span class="k">아이디</span><span class="v">${user.userId}</span>
			</div>
			<div class="profile-row">
				<span class="k">이름</span><span class="v">${user.name}</span>
			</div>
			<div class="profile-row">
				<span class="k">이메일</span><span class="v">${user.email}</span>
			</div>
			<div class="profile-row">
				<span class="k">닉네임</span><span class="v">${user.nickname}</span>
			</div>
			<div class="profile-row">
				<span class="k">권한</span><span class="v">${user.role}</span>
			</div>
			<div class="profile-row">
				<span class="k">연락처</span><span class="v">${user.phone}</span>
			</div>
			<div class="profile-row">
				<span class="k">생년월일</span><span class="v">${user.birth}</span>
			</div>
			<div class="profile-row">
				<span class="k">성별</span><span class="v">${user.gender}</span>
			</div>
			<div class="profile-row">
				<span class="k">주소</span><span class="v">${user.address}</span>
			</div>
		</section>

		<div class="profile-actions">
			<a class="btn primary"
				href="${pageContext.request.contextPath}/user/edit">정보 수정</a> <a
				class="btn secondary"
				href="${pageContext.request.contextPath}/logout">로그아웃</a>
		</div>
	</main>
	
		<%@ include file="/include/footer.jsp" %>
</body>
</html>
