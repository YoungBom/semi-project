<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/user.css?v=reg4">
</head>
<body>
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
					<button type="button" class="btn ghost" id="btnCheckId">중복확인</button>
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
						<option value="yahoo.com">yahoo.com</option>
						<option value="_custom">직접입력</option>
					</select>

					<!-- 직접입력 (같은 자리에 토글, 기본 숨김) -->
					<input class="input" id="emailDomainCustom" type="text"
						placeholder="domain.com" style="width: 220px; display: none;"
						autocapitalize="off">
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
					<option value="">선택</option>
					<option value="M">남성</option>
					<option value="F">여성</option>
					<option value="O">기타/응답하지 않음</option>
				</select>
			</div>

			<!-- 생년월일(캘린더) -->
			<div class="field">
				<label class="label" for="birth">생년월일(*)</label> <input
					class="input" id="birth" type="date" name="birth" required>
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
		</form>
	</main>

	<!-- 아이디 중복확인 URL -->
	<c:url var="checkIdUrl" value="/user/check-id" />

	<script>
  (function(){
    const $ = (s,p=document)=>p.querySelector(s);

    // ===== 아이디 중복확인 =====
    const userId = $('#user_id');
    const btnCheck = $('#btnCheckId');
    const idChecked = $('#idChecked');
    const idStatus = $('#idStatus');

    btnCheck.addEventListener('click', async () => {
      const id = (userId.value||'').trim();
      if (!id) { idChecked.value='false'; idStatus.textContent='아이디를 입력해주세요.'; idStatus.className='hint bad'; return; }
      idChecked.value='false'; idStatus.textContent='확인 중...'; idStatus.className='hint';
      try {
        const res = await fetch('${checkIdUrl}?user_id=' + encodeURIComponent(id), {headers:{'Accept':'application/json'}});
        if(!res.ok) throw new Error();
        const data = await res.json(); // {available:true/false}
        if (data.available){ idChecked.value='true'; idStatus.textContent='사용 가능한 아이디입니다.'; idStatus.className='hint ok'; }
        else { idChecked.value='false'; idStatus.textContent='이미 사용중인 아이디입니다.'; idStatus.className='hint bad'; }
      } catch(e){ idChecked.value='false'; idStatus.textContent='확인 실패. 잠시 후 다시 시도해주세요.'; idStatus.className='hint bad'; }
    });
    userId.addEventListener('input', ()=>{ idChecked.value='false'; idStatus.textContent='중복확인을 눌러주세요.'; idStatus.className='hint'; });

    // ===== 비밀번호 규칙/일치 =====
    const rePw=/^(?=.*[a-z])(?=.*\d)[a-z0-9]{8,20}$/;
    const pw1=$('#user_pw'), pw2=$('#user_pw2'), pwStatus=$('#pwStatus');
    function validatePw(){
      if(pw1.value!==pw1.value.toLowerCase()) pw1.value=pw1.value.toLowerCase();
      if(pw2.value!==pw2.value.toLowerCase()) pw2.value=pw2.value.toLowerCase();
      if(!rePw.test(pw1.value)){ pwStatus.textContent='조건 불충족: 소문자+숫자 8~20자'; pwStatus.className='hint bad'; return false; }
      if(pw2.value && pw1.value!==pw2.value){ pwStatus.textContent='비밀번호가 일치하지 않습니다.'; pwStatus.className='hint bad'; return false; }
      if(pw1.value && pw2.value && pw1.value===pw2.value){ pwStatus.textContent='사용 가능한 비밀번호입니다.'; pwStatus.className='hint ok'; }
      else { pwStatus.textContent=''; pwStatus.className='hint'; }
      return true;
    }
    pw1.addEventListener('input', validatePw);
    pw2.addEventListener('input', validatePw);

    // ===== 이메일 (select ↔ custom 같은 칸 토글) =====
    const emailLocal = $('#emailLocal');
    const sel = $('#emailDomainSel');
    const custom = $('#emailDomainCustom');
    const hidden = $('#email');
    const emailStatus = $('#emailStatus');

    function toggleDomainInput(){
      const useCustom = sel.value === '_custom';
      sel.style.display = useCustom ? 'none' : 'block';
      custom.style.display = useCustom ? 'block' : 'none';
      if (!useCustom) custom.value = '';
      buildEmail();
    }
    function buildEmail(){
      const local = (emailLocal.value||'').trim();
      const domain = (sel.style.display==='none') ? (custom.value||'').trim() : sel.value;
      const full = (local && domain) ? (local + '@' + domain) : '';
      hidden.value = full;
      const ok = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(full);
      emailStatus.textContent = ok || !full ? '' : '이메일 형식이 올바르지 않습니다.';
      emailStatus.className = ok || !full ? 'hint' : 'hint bad';
      return ok;
    }
    sel.addEventListener('change', toggleDomainInput);
    [emailLocal, custom].forEach(el=> el.addEventListener('input', buildEmail));

    // 초기 상태
    toggleDomainInput();

    // ===== 휴대폰 간단 검증 =====
    const phone=$('#phone'), phoneStatus=$('#phoneStatus');
    function validatePhone(){
      const v=(phone.value||'').trim();
      const ok=/^01[0-9]{8,9}$/.test(v);
      phoneStatus.textContent = ok || !v ? '' : '숫자만 10~11자리(예: 01012345678)';
      phoneStatus.className = ok || !v ? 'hint' : 'hint bad';
      return ok;
    }
    phone.addEventListener('input', validatePhone);

    // ===== 제출 전 최종 검증 =====
    document.querySelector('form.auth-card').addEventListener('submit', (e)=>{
      if ($('#idChecked').value!=='true'){ e.preventDefault(); $('#idStatus').textContent='아이디 중복확인을 먼저 해주세요.'; $('#idStatus').className='hint bad'; return; }
      if (!validatePw()){ e.preventDefault(); return; }
      if (!buildEmail()){ e.preventDefault(); return; }
      if (!validatePhone()){ e.preventDefault(); return; }
    });
  })();
  </script>
</body>
</html>
