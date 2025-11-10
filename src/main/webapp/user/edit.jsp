<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>




<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원정보 수정</title>
  	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/user.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
</head>
<body>
	<%@ include file="/include/header.jsp" %>
  <main class="profile-wrap mt-5">
    <h1 class="page-title with-logo mt-5"><span class="title-icon" aria-hidden="true">🍔</span> 회원정보 수정</h1>

    <c:if test="${not empty error}">
      <div class="alert error">${error}</div>
    </c:if>
    <c:if test="${not empty msg}">
      <div class="alert success">${msg}</div>
    </c:if>

    <!-- 이메일 분해 -->
    <c:set var="emailLocal" value="${fn:substringBefore(user.email, '@')}" />
    <c:set var="emailDomain" value="${fn:substringAfter(user.email,  '@')}" />

    <form class="form-card" method="post" action="${pageContext.request.contextPath}/user/edit">
     
	<!-- 아이디: 라벨 + 값(텍스트) + 전송용 hidden -->
	<div class="form-row inline">
  	<span class="form-label">아이디: ${user.userId}</span>
	</div>

    <!-- 이메일 -->
<div class="form-row two">
  <div>
    <label class="form-label" for="emailLocal">이메일</label>
    <input id="emailLocal" class="input" type="text" placeholder="example"
           value="${fn:split(user.email,'@')[0]}" autocomplete="off">
  </div>

  <div>
    <label class="form-label" for="emailDomain">도메인</label>
    <select id="emailDomain" class="input">
      <c:set var="domain" value="${fn:length(fn:split(user.email,'@'))==2 ? '@' += fn:split(user.email,'@')[1] : '@gmail.com'}"/>
      <option value="@gmail.com"  ${domain=='@gmail.com'  ? 'selected' : ''}>@gmail.com</option>
      <option value="@naver.com"  ${domain=='@naver.com'  ? 'selected' : ''}>@naver.com</option>
      <option value="@daum.net"   ${domain=='@daum.net'   ? 'selected' : ''}>@daum.net</option>
      <option value="@kakao.com"  ${domain=='@kakao.com'  ? 'selected' : ''}>@kakao.com</option>
      <option value="@nate.com"   ${domain=='@nate.com'   ? 'selected' : ''}>@nate.com</option>
    </select>
  </div>
</div>



<!-- 실제 서버로 보내는 값 -->
<input type="hidden" id="emailHidden" name="email" value="${user.email}">



      <!-- 닉네임 -->
      <div class="form-row">
        <label class="form-label" for="nickname">닉네임</label>
        <input id="nickname" class="input" type="text" name="nickname" value="${user.nickname}">
      </div>

      <!-- 휴대폰 -->
      <div class="form-row">
        <label class="form-label" for="phone">휴대폰</label>
        <input id="phone" class="input" type="text" name="phone" value="${user.phone}" placeholder="01012345678">
      </div>

      <!-- 생년월일 + 성별 -->
      <div class="form-row two">
        <div>
          <label class="form-label" for="birth">생년월일</label>
          <input id="birth" class="input" type="date" name="birth" value="${user.birth}">
        </div>
        <div>
          <label class="form-label" for="gender">성별</label>
          <select id="gender" class="input" name="gender">
            <option value="남" <c:if test="${user.gender eq '남'}">selected</c:if>>남성</option>
            <option value="여" <c:if test="${user.gender eq '여'}">selected</c:if>>여성</option>
            <option value="선택안함" <c:if test="${user.gender eq '선택안함'}">selected</c:if>>선택안함</option>
          </select>
        </div>
      </div>

      <!-- 이름 -->
      <div class="form-row">
        <label class="form-label" for="name">이름</label>
        <input id="name" class="input" type="text" name="name" value="${user.name}">
      </div>

      <!-- 주소 -->
      <div class="form-row">
        <label class="form-label" for="address">주소</label>
        <input id="address" class="input" type="text" name="address" value="${user.address}">
      </div>

      <!-- 액션 -->
      <div class="form-actions">
        <button type="submit" class="btn primary">저장</button>
        <a class="btn ghost" href="${pageContext.request.contextPath}/user/mypage">취소</a>

      </div>
    </form>
  </main>

  <!-- 최소 JS: 셀렉트 선택 시 오른쪽 도메인 입력칸은 항상 같은 자리, 직접입력일 때만 활성화.
       제출 시 hidden email 에 (local@domain) 합쳐서 전송 -->
  <script src="${pageContext.request.contextPath}/resources/js/edit.js"></script>

</body>
</html>
