<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>비밀번호 변경</title>
<link rel="stylesheet" href="${ctx}/resources/css/user.css">
</head>
<body>
<h1>비밀번호 변경</h1>

<c:if test="${not empty requestScope.msg}">
  <p style="color:green;">${requestScope.msg}</p>
</c:if>
<c:if test="${not empty requestScope.error}">
  <p style="color:red;">${requestScope.error}</p>
</c:if>

<form method="post" action="${ctx}/user/password-change">
  <label>현재 비밀번호 <input type="password" name="current_pw" required /></label><br/>
  <label>새 비밀번호 <input type="password" name="new_pw" required minlength="8" /></label><br/>
  <button type="submit">변경</button>
</form>
</body>
</html>