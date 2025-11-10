<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원정보 수정</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user.css?v=3">
</head>
<body>
  <main class="profile-wrap">
    <h1 class="page-title with-logo"><span class="title-icon" aria-hidden="true">🍔</span> 회원정보 수정</h1>

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
      <!-- 아이디: 읽기 전용 + 제출됨 -->
      <div class="form-row">
        <label class="form-label" for="uid">아이디</label>
        <input id="uid" class="input" type="text" name="user_id" value="${user.userId}" readonly>
      </div>

      <!-- 이메일: 고정 배치 (로컬) @ (도메인 셀렉트) (도메인 입력칸은 항상 보임, 직접입력일 때만 활성) -->
      <div class="form-row">
        <label class="form-label" for="emailLocal">이메일</label>
        <div style="display:flex; gap:10px; align-items:center; width:100%;">
          <input id="emailLocal" class="input" type="text" placeholder="example"
                 value="${emailLocal}" style="flex:1 1 0;">

          <span aria-hidden="true">@</span>

          <select id="emailDomainSel" class="input" style="width:220px;">
            <option value="naver.com"  <c:if test="${emailDomain eq 'naver.com'}">selected</c:if>>naver.com</option>
            <option value="gmail.com"  <c:if test="${emailDomain eq 'gmail.com'}">selected</c:if>>gmail.com</option>
            <option value="daum.net"   <c:if test="${emailDomain eq 'daum.net'}">selected</c:if>>daum.net</option>
            <option value="kakao.com"  <c:if test="${emailDomain eq 'kakao.com'}">selected</c:if>>kakao.com</option>
            <option value="hanmail.net"<c:if test="${emailDomain eq 'hanmail.net'}">selected</c:if>>hanmail.net</option>
            <option value="outlook.com"<c:if test="${emailDomain eq 'outlook.com'}">selected</c:if>>outlook.com</option>
            <option value="yahoo.com"  <c:if test="${emailDomain eq 'yahoo.com'}">selected</c:if>>yahoo.com</option>
            <option value="_custom"    <c:if test="${emailDomain ne 'naver.com' 
                                                    and emailDomain ne 'gmail.com' 
                                                    and emailDomain ne 'daum.net'
                                                    and emailDomain ne 'kakao.com'
                                                    and emailDomain ne 'hanmail.net'
                                                    and emailDomain ne 'outlook.com'
                                                    and emailDomain ne 'yahoo.com'}">selected</c:if>>직접입력</option>
          </select>

          <!-- 항상 같은 자리 유지, 직접입력 선택시에만 수정 가능 -->
          <input id="emailDomainBox" class="input" type="text" placeholder="domain.com"
                 style="width:220px;" value="${emailDomain}">
        </div>
        <!-- 서버로 제출되는 실제 이메일 -->
        <input type="hidden" id="emailFull" name="email" value="${user.email}">
      </div>

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
  <script>
    (function () {
      var sel   = document.getElementById('emailDomainSel');
      var box   = document.getElementById('emailDomainBox'); // 항상 보이는 입력칸
      var local = document.getElementById('emailLocal');
      var full  = document.getElementById('emailFull');

      function syncDomainBox() {
        if (sel.value === '_custom') {
          // 직접입력: 칸 활성화(편집 가능)
          box.removeAttribute('readonly');
          box.removeAttribute('disabled');
          box.placeholder = 'domain.com';
          if (!box.value || box.value.indexOf('.') === -1) {
            // 기본 안내만 유지
          }
        } else {
          // 사전도메인 선택: 칸 비활성 + 값 고정(자리 고정, 사라지지 않음)
          box.value = sel.value;
          box.setAttribute('readonly', 'readonly');
          box.setAttribute('disabled', 'disabled');
        }
      }

      function compose() {
        var domain = (sel.value === '_custom') ? (box.value || '').trim() : sel.value;
        var localPart = (local.value || '').trim();
        if (localPart && domain) {
          full.value = localPart + '@' + domain;
        } else {
          // 비어있으면 기존 값 유지 (서버에서 validation 권장)
          full.value = localPart ? (localPart + '@' + domain) : '';
        }
      }

      sel.addEventListener('change', function () {
        syncDomainBox();
        compose();
      });

      [box, local].forEach(function (el) {
        el.addEventListener('input', compose);
      });

      // 초기 상태 반영
      syncDomainBox();
      compose();

      // 제출 직전 한 번 더 합치기
      var form = document.querySelector('form.form-card');
      if (form) {
        form.addEventListener('submit', function () { syncDomainBox(); compose(); });
      }
    })();
  </script>
</body>
</html>
