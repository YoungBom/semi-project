<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>아이디 찾기</title></head>
  	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/user.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
<body>
<h1>아이디 찾기</h1>
	<%@ include file="/include/header.jsp" %>
<c:if test="${not empty msg}"><p>${msg}</p></c:if>
<c:if test="${not empty result}"><p>${result}</p></c:if>
	<%@ include file="/include/footer.jsp" %>
</body>
</html>