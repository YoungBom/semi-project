<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="checkIdUrl" value="/user/check-id" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/user.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/main.css"
	rel="stylesheet">
</head>
<body data-check-id-url="${checkIdUrl}">
	<%@ include file="/include/header.jsp"%>
	<main class="auth-wrap">
		<h1 class="auth-title">
			<span class="title-icon" aria-hidden="true">🍔</span> 회원가입
		</h1>

		<form class="auth-card" method="post"
			action="${pageContext.request.contextPath}/register"
			autocomplete="off">

			<!-- 아이디 + 중복확인 -->
			<div class="field">
				<label class="label" for="user_id">아이디(*)</label>
				<div style="display: flex; gap: 10px; align-items: center;">
					<input class="input" type="text" id="user_id" name="user_id"
						maxlength="30" required placeholder="로그인에 쓸 아이디"
						autocomplete="username" autocapitalize="off">
					<button type="button" class="btn btn-secondary" id="btnCheckId"
						style="white-space: nowrap; width: auto; min-width: 90px;">중복확인</button>
				</div>
				<small id="idStatus" class="hint">중복확인을 눌러주세요.</small> <input
					type="hidden" id="idChecked" value="false">
			</div>

			<!-- 비밀번호 & 확인 -->
			<div class="field">
				<label class="label" for="user_pw">비밀번호(*)</label> <input
					class="input" type="password" id="user_pw" name="user_pw"
					minlength="8" maxlength="20" required placeholder="소문자+숫자 8~20자"
					pattern="[a-z0-9]{8,20}" inputmode="text"
					autocomplete="new-password" autocapitalize="off"> <small
					class="hint">소문자와 숫자만 사용(8~20자)</small>
			</div>

			<div class="field">
				<label class="label" for="user_pw2">비밀번호 확인(*)</label> <input
					class="input" type="password" id="user_pw2" name="user_pw2"
					minlength="8" maxlength="20" required placeholder="비밀번호 다시 입력"
					pattern="[a-z0-9]{8,20}" inputmode="text"
					autocomplete="new-password" autocapitalize="off"> <small
					id="pwStatus" class="hint"></small>
			</div>

			<!-- 이메일: 로컬 + @ + (도메인 select) / 직접입력 선택시 select 숨기고 입력칸 표시 -->
			<div class="field">
				<label class="label" for="emailLocal">이메일(*)</label>
				<div
					style="display: flex; gap: 10px; align-items: center; width: 100%;">
					<input class="input" id="emailLocal" type="text"
						placeholder="example" required style="flex: 1 1 0;"
						autocapitalize="off"> <span aria-hidden="true">@</span>

					<!-- 도메인 선택 -->
					<select class="input" id="emailDomainSel" style="width: 220px;">
						<option value="gmail.com">gmail.com</option>
						<option value="naver.com">naver.com</option>
						<option value="daum.net">daum.net</option>
						<option value="kakao.com">kakao.com</option>
						<option value="nate.com">nate.com</option>
					</select>
				</div>
				<!-- 서버로 실제 전송될 이메일 -->
				<input type="hidden" id="email" name="email"> <small
					id="emailStatus" class="hint"></small>
			</div>

			<!-- 이름 -->
			<div class="field">
				<label class="label" for="name">이름(*)</label> <input class="input"
					id="name" type="text" name="name" maxlength="50" required>
			</div>

			<!-- 성별 -->
			<div class="field">
				<label class="label" for="gender">성별(*)</label> <select
					class="input" id="gender" name="gender" required>
					<option value="" selected disabled>선택</option>
					<option value="남">남성</option>
					<option value="여">여성</option>
					<option value="기타">기타/응답하지 않음</option>
				</select>
			</div>

			<!-- 생년월일(캘린더) -->
			<div class="field">
				<label class="label" for="birth">생년월일(*)</label> <input
					class="input" id="birth" type="date" name="birth"
					max="<%=new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())%>"
					onkeydown="return false;" onpaste="return false;" required>
			</div>

			<!-- 휴대폰 -->
			<div class="field">
				<label class="label" for="phone">휴대폰(*)</label> <input class="input"
					id="phone" type="tel" name="phone" required
					placeholder="01012345678" maxlength="11" inputmode="numeric"
					pattern="01[0-9]{8,9}"> <small id="phoneStatus"
					class="hint"></small>
			</div>

			<!-- 닉네임 -->
			<div class="field">
				<label class="label" for="nickname">닉네임(*)</label> <input
					class="input" id="nickname" type="text" name="nickname"
					maxlength="30" required>
			</div>

			<!-- 주소(선택) -->
			<div class="field">
				<label class="label" for="address">주소(선택)</label> <input
					class="input" id="address" type="text" name="address"
					maxlength="255" placeholder="">
			</div>

			<!-- 제출 -->
			<div class="actions">
				<button type="submit" class="btn primary" id="btnSubmit">가입하기</button>
			</div>

			<!-- 하단: 로그인 이동 -->
			<div class="subline">
				<span class="muted">이미 계정이 있나요?</span> <a class="link"
					href="${pageContext.request.contextPath}/login">로그인</a>
			</div>





			<!-- 역할 값: 기본 USER -->
			<input type="hidden" name="role" id="role" value="USER" />

			<div class="actions center">

				<button type="button" id="btnMakeAdmin" class="btn-outline">관리자
					계정 만들기</button>
			</div>
		</form>
	</main>

	<!-- 아이디 중복확인 URL -->
	<script
		src="${pageContext.request.contextPath}/resources/js/register.js"></script>
	<%@ include file="/include/footer.jsp"%>







	<script>
  (function () {
    const form = document.querySelector('form');
    const role = document.getElementById('role');
    const btnAdmin = document.getElementById('btnMakeAdmin');

    btnAdmin.addEventListener('click', () => {
      // 최소 방어장치(추후에 초대코드/첫 관리자 한정 등으로 강화 권장)
      if (!confirm('관리자 계정을 생성하시겠습니까? 일반 사용자에게 노출되면 안 됩니다.')){
    	  return;
      }

      role.value = 'ADMIN';   // 서버로 role=ADMIN 전송
      form.submit();          // 폼 제출
    });
  })();
</script>



</body>
</html>