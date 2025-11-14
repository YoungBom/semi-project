<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>아이디 찾기</title>
<!-- user.css 뒤에 recover.css (우선순위 중요) -->
<link href="${ctx}/resources/css/user.css" rel="stylesheet" />
<link href="${ctx}/resources/css/recover.css" rel="stylesheet" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
  <%@ include file="/include/header.jsp"%>
  
  <main class="auth-wrap">
    <section class="auth-card">
      <h1>아이디 찾기</h1>

      <form method="post" action="${ctx}/user/find_id" class="auth-form">
        <c:choose>
          <c:when test="${step eq 'answer'}">
            <input type="hidden" name="step" value="answer" />
            <input type="hidden" name="uid" value="${uid}" />

            <label class="field">
              <span class="label">질문</span>
              <input type="text" value="${fn:escapeXml(question_text)}" readonly />
            </label>

            <label class="field">
              <span class="label">답변</span>
              <input type="text" name="answer" autocomplete="off" required autofocus />
            </label>

            <div class="auth-actions">
              <button type="submit" class="auth-btn">확인</button>
              <a class="auth-btn secondary" href="${ctx}/user/login.jsp">로그인으로</a>
            </div>
          </c:when>

          <c:otherwise>
            <label class="field">
              <span class="label">휴대폰 번호</span>
              <input type="tel" name="phone"
                     placeholder="예: 01012345678"
                     inputmode="numeric" pattern="[0-9]{10,11}"
                     autocomplete="tel" required autofocus />
            </label>

            <div class="auth-actions">
              <button type="submit" class="auth-btn">아이디 찾기</button>
              <a class="auth-btn secondary" href="${ctx}/user/login.jsp">로그인으로</a>
            </div>
          </c:otherwise>
        </c:choose>

        <c:if test="${not empty error}">
          <p class="auth-error">${fn:escapeXml(error)}</p>
        </c:if>
      </form>
    </section>
  </main>
</body>
</html>
