<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>비밀번호 재설정</title>
  <link href="${ctx}/resources/css/user.css" rel="stylesheet" />
  <link href="${ctx}/resources/css/recover.css" rel="stylesheet" />
  <link href="${ctx}/resources/css/header.css" rel="stylesheet">
  <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
  <%@ include file="/include/header.jsp" %>

  <main class="auth-wrap">
    <section class="auth-card">
      <h1>비밀번호 재설정</h1>

      <form method="post" action="${ctx}/user/reset_password" class="auth-form">
        <!-- 검증 후 컨트롤러가 제공하는 식별값 (uid 또는 token) -->
        <input type="hidden" name="uid" value="${uid}" />
        <input type="hidden" name="token" value="${token}" />

        <label class="field">
          <span class="label">새 비밀번호</span>
          <input type="password" name="new_pw" minlength="8" maxlength="255" required autocomplete="new-password" />
        </label>

        <label class="field">
          <span class="label">새 비밀번호 확인</span>
          <input type="password" name="new_pw_confirm" minlength="8" maxlength="255" required autocomplete="new-password" />
        </label>

        <div class="auth-actions">
          <button type="submit" class="auth-btn">변경</button>
          <a class="auth-btn secondary" href="${ctx}/user/login.jsp">로그인으로</a>
        </div>

        <c:if test="${not empty error}">
          <p class="auth-error">${error}</p>
        </c:if>
      </form>
    </section>
  </main>
</body>
</html>
