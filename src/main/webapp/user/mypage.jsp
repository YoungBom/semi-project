<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>마이페이지</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user.css">
</head>
<body>
  <main class="profile-wrap">
    <h1 class="profile-title">마이페이지</h1>

    <c:if test="${not empty error}">
      <div class="alert error">${error}</div>
    </c:if>
    <c:if test="${not empty msg}">
      <div class="alert success">${msg}</div>
    </c:if>

    <!-- user 속성은 서블릿에서 채워줌 -->
    <section class="profile-card">
      <div class="profile-row">
        <span class="k">아이디</span>
        <span class="v">${user.userId}</span>
      </div>
      <div class="profile-row">
        <span class="k">이름</span>
        <span class="v">${user.name}</span>
      </div>
      <div class="profile-row">
        <span class="k">이메일</span>
        <span class="v">${user.email}</span>
      </div>
      <div class="profile-row">
        <span class="k">닉네임</span>
        <span class="v">${user.nickname}</span>
      </div>
      <div class="profile-row">
        <span class="k">역할</span>
        <span class="v">${user.role}</span>
      </div>
      <div class="profile-row">
        <span class="k">전화번호</span>
        <span class="v">${user.phone}</span>
      </div>
      <div class="profile-row">
        <span class="k">생년월일</span>
        <span class="v">${user.birth}</span>
      </div>
      <div class="profile-row">
        <span class="k">성별</span>
        <span class="v">${user.gender}</span>
      </div>
      <div class="profile-row">
        <span class="k">주소</span>
        <span class="v">${user.address}</span>
      </div>
    </section>

    <div class="profile-actions">
      <a class="btn primary" href="${pageContext.request.contextPath}/user/edit.jsp">정보 수정</a>
      <a class="btn secondary" href="${pageContext.request.contextPath}/logout">로그아웃</a>
    </div>
  </main>
</body>
</html>
