<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>비밀번호 변경</title>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/resources/css/user.css">
</head>
<body>
  <h1>비밀번호 변경</h1>

  <!-- 에러 메시지: requestScope.error가 있을 때만 표시 -->
  <c:if test="${not empty requestScope.error}">
    <div class="error">
      <c:out value="${requestScope.error}" />
    </div>
  </c:if>

  <form method="post" action="${ctx}/password/change" autocomplete="off">
    <label>현재 비밀번호
      <input type="password" name="current_pw" required autocomplete="current-password">
    </label><br>

    <label>새 비밀번호
      <input type="password" name="new_pw" required minlength="8" maxlength="255" autocomplete="new-password">
    </label><br>

    <label>새 비밀번호 확인
      <input type="password" name="new_pw2" required minlength="8" maxlength="255" autocomplete="new-password">
    </label><br>

    <button type="submit">변경</button>
    <a href="${ctx}/user/mypage">취소</a>
  </form>
</body>
</html>
