<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인</title>
  	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/user.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
</head>
<body>
	<%@ include file="/include/header.jsp" %>
  <main class="auth-wrap">
    <h1 class="auth-title">🍔 로그인</h1>

    <c:if test="${not empty error}">
      <div class="alert error">${error}</div>
    </c:if>
    <c:if test="${not empty msg}">
      <div class="alert success">${msg}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login" class="auth-card" autocomplete="on">
      <label class="field">
        <span class="label">아이디</span>
        <input type="text" name="user_id" placeholder="아이디를 입력하세요" required maxlength="255" autocomplete="username">
      </label>

      <label class="field">
        <span class="label">비밀번호</span>
        <input type="password" name="user_pw" placeholder="비밀번호를 입력하세요" required minlength="8" maxlength="255" autocomplete="current-password">
      </label>

      

      <div class="actions">
        <button type="submit" class="btn primary">로그인</button>
      </div>

      
	  <div class="auth-links" style="text-align:center; margin-top:12px;">
  		<a href="${pageContext.request.contextPath}/user/find_id.jsp">아이디 찾기</a>
 			 &nbsp;|&nbsp;
  		<a href="${pageContext.request.contextPath}/user/find_password.jsp">비밀번호 찾기</a>
	  </div>

      <hr class="divider"/>

      
    </form>
  </main>
  	<%@ include file="/include/footer.jsp" %>
</body>
</html>
