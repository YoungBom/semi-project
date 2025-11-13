<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>보안질문 설정</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/user.css">
	<link href="${ctx}/resources/css/header.css" rel="stylesheet">
	<link href="${ctx}/resources/css/recover.css" rel="stylesheet">
</head>
<body>
	<!-- 런타임 include: 변수 충돌 방지 -->
	<jsp:include page="/include/header.jsp" />

	<main class="auth-wrap">
		<section class="auth-card">
			<h1>질의응답(보안질문) 설정</h1>

			<c:if test="${not empty msg}">
				<div class="alert success">${msg}</div>
			</c:if>
			<c:if test="${not empty error}">
				<div class="alert error">${error}</div>
			</c:if>

			<form method="post"
				action="${pageContext.request.contextPath}/user/security_setup"
				autocomplete="off">
				<fieldset>
					<legend>질문 선택</legend>

					<label class="inline"> <input type="radio" name="mode"
						value="preset" checked> 사전 질문 사용
					</label> <select name="qid">
						<c:forEach var="q" items="${questions}">
							<option value="${q.id}"
								<c:if test="${current.qid == q.id}">selected</c:if>>
								${q.text}</option>
						</c:forEach>
					</select>

					<div style="height: 8px"></div>

					<label class="inline"> <input type="radio" name="mode"
						value="custom"> 직접 질문 입력
					</label>
					<!-- EL은 null이면 자동으로 빈 문자열 출력되므로 그대로 사용 -->
					<input type="text" name="question_tx"
						placeholder="예) 내가 처음 산 자동차 모델은?" value="${current.qtext}"
						maxlength="200" />

					<!-- preset 미사용 시 폴백 qid -->
					<input type="hidden" name="qid_custom_fallback"
						value="${empty current.qid ? 1 : current.qid}">
				</fieldset>

				<fieldset>
					<legend>답변</legend>
					<input type="text" name="answer" placeholder="답변 (저장 시 해시 처리)"
						required minlength="1" maxlength="255">
				</fieldset>

				<div class="actions">
					<button type="submit" class="btn-primary">저장</button>
					<a class="btn-outline"
						href="${pageContext.request.contextPath}/user/mypage">마이페이지로</a>
				</div>

				<div class="muted" style="margin-top: 12px">
					현재 설정:
					<c:choose>
						<c:when test="${not empty current}">
							<strong> <c:out
									value="${empty current.qtext ? current.qtext_resolved : current.qtext}" />
							</strong>
						</c:when>
						<c:otherwise>없음</c:otherwise>
					</c:choose>
				</div>
			</form>
		</section>
	</main>
</body>
</html>
