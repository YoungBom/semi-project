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
<body class="login-page" data-check-id-url="${checkIdUrl}">

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
      <input type="date"
             id="birth"
             name="birth"
             max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" 
             required>
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
<script src="${pageContext.request.contextPath}/resources/js/register.js"></script>


</body>
</html>
