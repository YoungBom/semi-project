<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>비밀번호 찾기</title>
  <link href="${ctx}/resources/css/user.css" rel="stylesheet" />
  <link href="${ctx}/resources/css/recover.css" rel="stylesheet" />
  <link href="${ctx}/resources/css/header.css" rel="stylesheet">
  <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
  <%@ include file="/include/header.jsp" %>

  <main class="auth-wrap">
    <section class="auth-card">
      <h1>비밀번호 찾기</h1>

      <form method="post" action="${ctx}/user/find_password" class="auth-form">
        <c:choose>
          <%-- 컨트롤러가 step='question' 으로 포워딩하면 질문/답변 단계 --%>
          <c:when test="${step eq 'question'}">
            <input type="hidden" name="step" value="question" />
            <input type="hidden" name="uid" value="${uid}" />

            <label class="field">
              <span class="label">보안 질문</span>
              <input type="text" value="${fn:escapeXml(question_text)}" readonly />
            </label>

            <label class="field">
              <span class="label">답변</span>
              <input type="text" name="answer" autocomplete="off" required />
            </label>

            <div class="auth-actions">
              <button type="submit" class="auth-btn">확인</button>
              <a class="auth-btn secondary" href="${ctx}/user/login.jsp">로그인으로</a>
            </div>
          </c:when>

          <%-- 기본 단계: 아이디/휴대폰으로 사용자 찾기 --%>
          <c:otherwise>
            <label class="field">
              <span class="label">아이디</span>
              <input type="text" name="user_id" required maxlength="255" autocomplete="username" />
            </label>

            <label class="field">
              <span class="label">휴대폰 번호</span>
              <input type="tel" name="phone" placeholder="예: 01012345678" required />
            </label>

            <div class="auth-actions">
              <button type="submit" class="auth-btn">다음</button>
              <a class="auth-btn secondary" href="${ctx}/user/find_id.jsp">아이디 찾기</a>
            </div>
          </c:otherwise>
        </c:choose>

        <c:if test="${not empty error}">
          <p class="auth-error">${error}</p>
        </c:if>
      </form>
    </section>
  </main>
</body>
</html>
