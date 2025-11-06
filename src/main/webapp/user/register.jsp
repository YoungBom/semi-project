<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원가입</title>
  <!-- 캐시 무력화 파라미터 v= 갱신하면서 사용 -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user.css?v=reg_inline_pwnum_1">
</head>
<body class="login-page">

<h1>🍔 회원가입</h1>

<main class="auth-viewport">
<form class="login-form" method="post" action="${pageContext.request.contextPath}/register" autocomplete="off">

  <!-- 아이디 + 중복확인 -->
  <label>아이디(*)
    <div class="row-compact">
      <input type="text" id="user_id" name="user_id" maxlength="30" required
             placeholder="로그인에 쓸 아이디" autocomplete="username" autocapitalize="off">
      <button type="button" class="btn-outline" id="btnCheckId">중복확인</button>
    </div>
    <small id="idStatus" class="hint">중복확인을 눌러주세요.</small>
    <input type="hidden" id="idChecked" value="false">
  </label>

  <!-- 비밀번호 & 확인 (소문자+숫자 8~20자) -->
  <label>비밀번호(*)
    <input type="password" id="user_pw" name="user_pw"
           minlength="8" maxlength="20" required
           placeholder="소문자+숫자 8~20자"
           pattern="[a-z0-9]{8,20}" inputmode="text"
           autocomplete="new-password" autocapitalize="off">
    <small class="hint">소문자와 숫자만 사용(8~20자)</small>
  </label>

  <label>비밀번호 확인(*)
    <input type="password" id="user_pw2" name="user_pw2"
           minlength="8" maxlength="20" required
           placeholder="비밀번호 다시한번 입력해주세요."
           pattern="[a-z0-9]{8,20}" inputmode="text"
           autocomplete="new-password" autocapitalize="off">
    <small id="pwStatus" class="hint"></small>
  </label>

  <!-- 이메일: local + 도메인 선택(또는 직접입력) -->
  <label>이메일(*)
    <div class="row-compact">
      <input type="text" id="emailLocal" placeholder="example" required autocapitalize="off">
      <select id="emailDomain" required>
        <option value="@gmail.com">@gmail.com</option>
        <option value="@naver.com">@naver.com</option>
        <option value="@daum.net">@daum.net</option>
        <option value="@yahoo.com">@yahoo.com</option>
        <option value="custom">직접입력</option>
      </select>
    </div>
    <input type="text" id="emailCustom" placeholder="직접입력 예: @mycompany.co.kr" style="display:none" autocapitalize="off">
    <!-- 서버로 실제 전송될 이메일 -->
    <input type="hidden" id="email" name="email">
    <small id="emailStatus" class="hint"></small>
  </label>

  <!-- 이름 / 성별 -->
  <div class="row">
    <label>이름(*)
      <input type="text" id="name" name="name" maxlength="50" required>
    </label>
    <label>성별(*)
      <select id="gender" name="gender" required>
        <option value="">선택</option>
        <option value="M">남성</option>
        <option value="F">여성</option>
        <option value="O">기타/응답하지 않음</option>
      </select>
    </label>
  </div>

  <!-- 생년월일 / 휴대폰 -->
  <div class="row">
    <label>생년월일(*)
      <input type="date" id="birth" name="birth" required>
    </label>
    <label>휴대폰(*)
      <input type="tel" id="phone" name="phone" required
             placeholder="01012345678" maxlength="11" inputmode="numeric" pattern="01[0-9]{8,9}">
      <small id="phoneStatus" class="hint"></small>
    </label>
  </div>

  <!-- 닉네임 -->
  <label>닉네임(*)
    <input type="text" id="nickname" name="nickname" maxlength="30" required>
  </label>

  <!-- 주소(선택) -->
  <label>주소(선택)
    <input type="text" id="address" name="address" maxlength="255" placeholder="">
  </label>

  <!-- 제출 -->
  <div class="actions center">
    <button type="submit" class="btn-primary" id="btnSubmit">가입하기</button>
  </div>

  <!-- 하단: 로그인 이동 -->
  <c:url var="loginUrl" value="/login"/>
  <p class="auth-switch tight-center">이미 계정이 있나요? <a class="link-accent" href="${loginUrl}">로그인</a></p>

</form>
</main>

<!-- 아이디 중복확인 요청 URL -->
<c:url var="checkIdUrl" value="/user/check-id"/>

<script>
(() => {
  const $ = (s, p=document) => p.querySelector(s);

  // ===== 아이디 중복확인 (인라인 메시지) =====
  const userId = $('#user_id');
  const btnCheck = $('#btnCheckId');
  const idChecked = $('#idChecked');
  const idStatus = $('#idStatus');

  btnCheck.addEventListener('click', async () => {
    const id = (userId.value || '').trim();
    if (!id) {
      idChecked.value = 'false';
      idStatus.textContent = '아이디를 입력해주세요.';
      idStatus.className = 'hint bad';
      return;
    }
    idChecked.value = 'false';
    idStatus.textContent = '확인 중...';
    idStatus.className = 'hint';

    try {
      const res = await fetch('${checkIdUrl}?user_id=' + encodeURIComponent(id), {headers: {'Accept': 'application/json'}});
      if (!res.ok) throw new Error('서버 오류');
      const data = await res.json(); // {available:true/false}
      if (data.available) {
        idChecked.value = 'true';
        idStatus.textContent = '사용 가능한 아이디입니다.';
        idStatus.className = 'hint ok';
      } else {
        idChecked.value = 'false';
        idStatus.textContent = '이미 사용중인 아이디입니다.';
        idStatus.className = 'hint bad';
      }
    } catch (e) {
      idChecked.value = 'false';
      idStatus.textContent = '확인 실패. 잠시 후 다시 시도해주세요.';
      idStatus.className = 'hint bad';
    }
  });

  // 아이디가 바뀌면 다시 확인하도록 상태 초기화
  userId.addEventListener('input', () => {
    idChecked.value = 'false';
    idStatus.textContent = '중복확인을 눌러주세요.';
    idStatus.className = 'hint';
  });

  // ===== 비밀번호 규칙/일치 검사 (소문자+숫자 8~20자) =====
  const rePw = /^(?=.*[a-z])(?=.*\\d)[a-z0-9]{8,20}$/;
  const pw1 = $('#user_pw');
  const pw2 = $('#user_pw2');
  const pwStatus = $('#pwStatus');

  function validatePw(){
    // 대문자 자동 소문자화(사용성 보완)
    if (pw1.value !== pw1.value.toLowerCase()) pw1.value = pw1.value.toLowerCase();
    if (pw2.value !== pw2.value.toLowerCase()) pw2.value = pw2.value.toLowerCase();

    if (!rePw.test(pw1.value)) {
      pwStatus.textContent = '조건 불충족: 소문자+숫자 8~20자';
      pwStatus.className = 'hint bad';
      return false;
    }
    if (pw2.value && pw1.value !== pw2.value) {
      pwStatus.textContent = '비밀번호가 일치하지 않습니다.';
      pwStatus.className = 'hint bad';
      return false;
    }
    if (pw1.value && pw2.value && pw1.value === pw2.value) {
      pwStatus.textContent = '사용 가능한 비밀번호입니다.';
      pwStatus.className = 'hint ok';
    } else {
      pwStatus.textContent = '';
      pwStatus.className = 'hint';
    }
    return true;
  }
  pw1.addEventListener('input', validatePw);
  pw2.addEventListener('input', validatePw);

  // ===== 이메일 합치기 + 형식 점검 =====
  const emailLocal = $('#emailLocal');
  const emailDomain = $('#emailDomain');
  const emailCustom = $('#emailCustom');
  const emailHidden = $('#email');
  const emailStatus = $('#emailStatus');

  emailDomain.addEventListener('change', () => {
    const custom = emailDomain.value === 'custom';
    emailCustom.style.display = custom ? 'block' : 'none';
    if (!custom) emailCustom.value = '';
    buildEmail();
  });

  function buildEmail(){
    const local = (emailLocal.value || '').trim();
    let domain = emailDomain.value;
    if (domain === 'custom') domain = (emailCustom.value || '').trim();
    const full = local + domain;
    emailHidden.value = full;
    const ok = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$/.test(full);
    emailStatus.textContent = ok ? '' : '이메일 형식이 올바르지 않습니다.';
    emailStatus.className = ok ? 'hint' : 'hint bad';
    return ok;
  }
  emailLocal.addEventListener('input', buildEmail);
  emailDomain.addEventListener('input', buildEmail);
  emailCustom.addEventListener('input', buildEmail);

  // ===== 휴대폰 간단 검증 (010으로 시작 10~11자리) =====
  const phone = $('#phone');
  const phoneStatus = $('#phoneStatus');
  function validatePhone(){
    const v = (phone.value || '').trim();
    const ok = /^01[0-9]{8,9}$/.test(v);
    phoneStatus.textContent = ok ? '' : '숫자만 10~11자리(예: 01012345678)';
    phoneStatus.className = ok ? 'hint' : 'hint bad';
    return ok;
  }
  phone.addEventListener('input', validatePhone);

  // ===== 제출 전 최종 검증 =====
  $('#btnSubmit').closest('form').addEventListener('submit', (e) => {
    if (idChecked.value !== 'true') {
      e.preventDefault();
      idStatus.textContent = '아이디 중복확인을 먼저 해주세요.';
      idStatus.className = 'hint bad';
      return;
    }
    if (!validatePw()) { e.preventDefault(); return; }
    if (!buildEmail()) { e.preventDefault(); return; }
    if (!validatePhone()) { e.preventDefault(); return; }
  });
})();
</script>

</body>
</html>
