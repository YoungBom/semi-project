<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>보안질문 설정</title>

<link rel="stylesheet" href="${ctx}/resources/css/user.css">
<link rel="stylesheet" href="${ctx}/resources/css/header.css">
<link rel="stylesheet" href="${ctx}/resources/css/recover.css">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
	rel="stylesheet">
</head>
<body>
	<!-- 런타임 include: 변수 충돌 방지 -->
	<jsp:include page="/include/header.jsp" />

	<!-- 아이디 찾기랑 같은 레이아웃 -->
	<main class="auth-wrap find-id security-qa">
		<section class="auth-card">
			<h1>질의응답(보안질문) 설정</h1>

			<c:if test="${not empty msg}">
				<p class="auth-success">${msg}</p>
			</c:if>
			<c:if test="${not empty error}">
				<p class="auth-error">${error}</p>
			</c:if>

			<form method="post" action="${ctx}/user/security_setup"
				autocomplete="off" class="auth-form">

				<!-- 질문 선택 -->
				<div class="field">
					<span class="label">질문 선택</span>
					<div class="qa-mode-group">
						<label> <input type="radio" name="mode" value="preset"
							checked> <span>사전 질문 사용</span>
						</label> <label> <input type="radio" name="mode" value="custom">
							<span>직접 질문 입력</span>
						</label>
					</div>
				</div>

				<!-- 사전 질문 드롭다운 -->
				<div class="field">
					<span class="label">사전 질문</span> <select name="qid">
						<!-- 맨 위 안내 문구 -->
						<option value="">질문을 선택하세요</option>

						<!-- DB에서 넘어온 질문 리스트 -->
						<c:forEach var="q" items="${questions}">
							<option value="${q.id}"
								<c:if test="${current.qid == q.id}">selected</c:if>>
								${q.text}</option>
						</c:forEach>
					</select>
				</div>

				<!-- 직접 질문 -->
				<div class="field">
					<span class="label">직접 질문 입력</span> <input type="text"
						name="question_tx" placeholder="예) 내가 처음 산 자동차 모델은?"
						value="${current.qtext}" maxlength="200" />
				</div>

				<!-- preset 미사용 시 폴백 qid -->
				<input type="hidden" name="qid_custom_fallback"
					value="${empty current.qid ? 1 : current.qid}">

				<!-- 답변 -->
				<div class="field">
					<span class="label">답변</span> <input type="text" name="answer"
						placeholder="답변 (저장 시 해시 처리)" required minlength="1"
						maxlength="255" />
				</div>

				<!-- 버튼 -->
				<div class="auth-actions">
					<button type="submit" class="auth-btn">저장</button>
					<a class="auth-btn secondary" href="${ctx}/user/mypage">마이페이지로</a>
				</div>

				<!-- 현재 설정 -->
				<div class="current-qa">
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
