<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>비밀번호 찾기</title>
  <link href="${ctx}/resources/css/user.css" rel="stylesheet">
</head>
<body>
  <h1>비밀번호 찾기</h1>

  <form method="post" action="${ctx}/user/find_password">
    <label>아이디
      <input type="text" name="user_id" required maxlength="255" placeholder="가입 아이디">
    </label>
    <label>이메일
      <input type="email" name="email" required maxlength="255" placeholder="가입 이메일">
    </label>

    <div class="actions">
      <button type="submit">다음</button>
      <a class="btn-link" href="${ctx}/user/login.jsp">로그인으로</a>
    </div>
  </form>

  <c:if test="${not empty requestScope.error}">
    <p class="error">${requestScope.error}</p>
  </c:if>
</body>
</html>
