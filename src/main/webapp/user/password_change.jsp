<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>비밀번호 변경</title>
  	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/user.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
</head>
<body>
<%@ include file="/include/header.jsp" %>
<h1>비밀번호 변경</h1>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.flash}">
  <div class="toast toast-success">
    <c:out value="${sessionScope.flash}" />
  </div>
  <c:remove var="flash" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.flash_error}">
  <div class="toast toast-error">
    <c:out value="${sessionScope.flash_error}" />
  </div>
  <c:remove var="flash_error" scope="session" />
</c:if>

<c:if test="${not empty requestScope.msg}">
  <p style="color:green;">${requestScope.msg}</p>
</c:if>
<c:if test="${not empty requestScope.error}">
  <p style="color:red;">${requestScope.error}</p>
</c:if>

<form method="post" action="${ctx}/user/password_change">
  <label>현재 비밀번호 <input type="password" name="current_pw" required /></label><br/>
  <label>새 비밀번호 <input type="password" name="new_pw" required minlength="8" /></label><br/>
  <button type="submit">변경</button>
</form>
<%@ include file="/include/footer.jsp" %>
</body>
</html>