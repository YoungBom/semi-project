<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/resources/css/user.css">
</head>
<body>

	
	<c:if test="${not empty requestScope.FLASH}">
		<div class="flash">
			<c:out value="${requestScope.FLASH}" />
		</div>
	</c:if>

	<h1>마이페이지</h1>

	<div class="card">
		<p>
			<strong>아이디</strong> :
			<c:out value="${me.user_id}" />
		</p>
		<p>
			<strong>이름</strong> :
			<c:out value="${me.name}" />
		</p>
		<p>
			<strong>닉네임</strong> :
			<c:out value="${me.nickname}" />
		</p>
		<p>
			<strong>이메일</strong> :
			<c:out value="${me.email}" />
		</p>
		<p>
			<strong>휴대폰</strong> :
			<c:out value="${me.phone}" />
		</p>

	
		<c:choose>
			<c:when test="${me.birth instanceof java.util.Date}">
				<p>
					<strong>생년월일</strong> :
					<fmt:formatDate value="${me.birth}" pattern="yyyy-MM-dd" />
				</p>
			</c:when>
			<c:otherwise>
				<p>
					<strong>생년월일</strong> :
					<c:out value="${me.birth}" />
				</p>
			</c:otherwise>
		</c:choose>

		<p>
			<strong>성별</strong> :
			<c:out value="${me.gender}" />
		</p>
		<p>
			<strong>주소</strong> :
			<c:out value="${me.address}" />
		</p>
	</div>

	<p class="actions">
		<a href="${ctx}/user/edit">정보 수정</a> | <a
			href="${ctx}/password/change">비밀번호 변경</a>
	</p>

	<form method="post" action="${ctx}/logout">
		<button type="submit">로그아웃</button>
	</form>
</body>
</html>
