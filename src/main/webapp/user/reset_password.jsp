<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ok" value="${sessionScope.RESET_OK}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>비밀번호 재설정</title>
<link href="${ctx}/resources/css/user.css" rel="stylesheet">
</head>
<body>
	<h1>비밀번호 재설정</h1>

	<c:choose>
		<c:when test="${ok}">
			<form method="post" action="${ctx}/user/reset_password">
				<label>새 비밀번호 <input type="password" name="new_pw" required
					minlength="8" maxlength="255" placeholder="새 비밀번호">
				</label> <label>새 비밀번호 확인 <input type="password" name="new_pw2"
					required minlength="8" maxlength="255" placeholder="새 비밀번호 확인">
				</label>
				<div class="actions">
					<button type="submit">변경</button>
					<a class="btn-link" href="${ctx}/user/login.jsp">로그인으로</a>
				</div>
			</form>
			<c:if test="${not empty requestScope.error}">
				<p class="error">${requestScope.error}</p>
			</c:if>
			<c:if test="${not empty requestScope.msg}">
				<p class="msg">${requestScope.msg}</p>
			</c:if>
		</c:when>
		<c:otherwise>
			<p class="error">비밀번호 재설정 권한이 없습니다. 처음부터 다시 진행해주세요.</p>
			<div class="actions">
				<a class="btn" href="${ctx}/user/find_password.jsp">비밀번호 찾기</a>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>
