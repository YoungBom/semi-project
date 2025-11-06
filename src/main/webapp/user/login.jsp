<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인</title>
  <link rel="stylesheet"
      href="${pageContext.request.contextPath}/resources/css/user.css?v=20251106_1">
</head>
<body class= "login-page">

  <!-- 상단 타이틀/레이아웃은 프로젝트 스타일 유지 -->
  <!-- 뷰포트 중앙 정렬 래퍼 -->
<main class="auth-viewport">
  <!-- 기존 폼 그대로 -->
  <form method="post"
        action="${pageContext.request.contextPath}/login"
        autocomplete="on"
        class="login-form">

    <label>아이디
      <input type="text" name="user_id" required maxlength="255" autocomplete="username">
    </label>

    <label>비밀번호
      <input type="password" name="user_pw" required minlength="8" maxlength="255" autocomplete="current-password">
    </label>

    <input type="hidden" name="next" value="${param.next != null ? param.next : next}" />

    <div class="actions center">
      <button type="submit" class="btn-primary" id="loginBtn">로그인</button>
    </div>

    <c:url var="registerUrl" value="/user/register.jsp">
      <c:param name="next" value="${not empty param.next ? param.next : next}" />
    </c:url>

    <p class="auth-switch tight-center">
      계정이 없으신가요?
      <a class="link-accent" href="${registerUrl}">회원가입하기</a>
    </p>
  </form>
</main>
  <!-- 에러 모달 -->
  <div class="modal" id="errorModal" aria-hidden="true" role="dialog" aria-modal="true" aria-labelledby="errTitle">
    <div class="modal-backdrop" data-close></div>
    <div class="modal-panel" role="document">
      <div class="modal-header">
        <h3 id="errTitle">로그인에 실패했습니다</h3>
        <button class="modal-close" type="button" title="닫기" aria-label="닫기" data-close>&times;</button>
      </div>
      <div class="modal-body" id="errMsg"></div>
      <div class="modal-footer">
        <button class="btn-primary" type="button" data-close>확인</button>
      </div>
    </div>
  </div>

  <!-- 서버 에러를 JS가 읽도록 숨김 출력 -->
  <c:if test="${not empty error}">
    <span id="serverError" data-msg="${fn:escapeXml(error)}" hidden></span>
  </c:if>
  <c:if test="${not empty param.error}">
    <span id="serverErrorParam" data-msg="${fn:escapeXml(param.error)}" hidden></span>
  </c:if>

  <script src="${pageContext.request.contextPath}/resources/js/login.js"></script>
</body>
</html>
