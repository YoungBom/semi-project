<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user.css">
</head>
<body>
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

      <label class="checkline">
        <input type="checkbox" name="remember_me" value="1">
        <span>로그인 상태 유지(선택)</span>
      </label>

      <div class="actions">
        <button type="submit" class="btn primary">로그인</button>
      </div>

      <div class="subline">
        <span class="muted">아이디가 없습니까?</span>
        <a class="link" href="${pageContext.request.contextPath}/register">회원가입</a>
      </div>

      <hr class="divider"/>

      <div class="assist">
        <a class="link" href="${pageContext.request.contextPath}/id/lookup">아이디 찾기</a>
      </div>
    </form>
  </main>
</body>
</html>
