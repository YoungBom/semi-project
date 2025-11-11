<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>보안질문 설정</title>
<link href="${ctx}/resources/css/user.css" rel="stylesheet" />
<link href="${ctx}/resources/css/recover.css" rel="stylesheet" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
	<%@ include file="/include/header.jsp"%>

	<main class="auth-wrap">
		<section class="auth-card">
			<h1>보안질문 설정</h1>

			<form method="post" action="${ctx}/user/security_setup"
				class="auth-form">
				<label class="field"> <span class="label">질문 선택</span> <select
					name="question_id" required>
						<option value="" disabled
							<c:if test="${empty selectedQid}">selected</c:if>>질문을
							선택하세요</option>
						<c:forEach var="q" items="${questions}">
							<option value="${q.id}"
								<c:if test="${q.id == selectedQid}">selected</c:if>>
								${fn:escapeXml(q.text)}</option>
						</c:forEach>
				</select>
				</label>

				<!-- 직접 입력(예: id=9) 선택 시 표시되는 커스텀 질문 입력칸 -->
				<label class="field" id="field-custom" style="display: none;">
					<span class="label">직접 입력 질문</span> <input type="text"
					name="question_tx" maxlength="200"
					value="${fn:escapeXml(question_tx)}" autocomplete="off" />
				</label> <label class="field"> <span class="label">답변</span> <input
					type="text" name="answer" value="${fn:escapeXml(answer)}"
					maxlength="255" required autocomplete="off" />
				</label>

				<div class="auth-actions">
					<button type="submit" class="auth-btn">저장</button>
					<a class="auth-btn secondary" href="${ctx}/user/mypage">마이페이지</a>
				</div>

				<c:if test="${not empty error}">
					<p class="auth-error">${error}</p>
				</c:if>
				<c:if test="${not empty msg}">
					<p class="auth-success">${msg}</p>
				</c:if>
			</form>
		</section>
	</main>

	<script>
		(function() {
			const sel = document.querySelector('select[name="question_id"]');
			const custom = document.getElementById('field-custom');
			function toggle() {
				if (!sel)
					return;
				// 기본 정책: question_id가 '9'면 사용자 정의 질문 입력 노출
				custom.style.display = (sel.value === '9') ? 'block' : 'none';
			}
			if (sel) {
				sel.addEventListener('change', toggle);
				// 서버가 selectedQid를 주입했을 때도 초기 상태 반영
				toggle();
			}
		})();
	</script>
</body>
</html>
