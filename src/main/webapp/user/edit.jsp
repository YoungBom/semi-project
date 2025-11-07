<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>회원정보 수정</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/user.css" />
</head>
<body>
  <main class="profile-wrap">
    <h1 class="page-title with-logo">🍔 회원정보 수정</h1>

    <c:if test="${not empty error}">
      <div class="alert error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/user/update" class="form-card">

      <div class="form-row">
        <label class="form-label">아이디</label>
        <input class="input" type="text" value="${user.userId}" disabled />
      </div>

      <div class="form-row">
        <label class="form-label" for="email">이메일</label>
        <input class="input" id="email" name="email" type="email" value="${user.email}" placeholder="example@domain.com" />
        <p class="hint" id="emailStatus"></p>
      </div>

      <div class="form-row">
        <label class="form-label" for="nickname">닉네임</label>
        <input class="input" id="nickname" name="nickname" type="text" value="${user.nickname}" />
      </div>

      <div class="form-row">
        <label class="form-label" for="phone">휴대폰</label>
        <input class="input" id="phone" name="phone" type="text" value="${user.phone}" placeholder="01012345678" />
        <p class="hint bad" id="phoneStatus" style="display:none;">이미 등록된 전화번호입니다.</p>
      </div>

      <div class="form-row two">
        <div>
          <label class="form-label" for="birth">생년월일</label>
          <input class="input" id="birth" name="birth" type="text" value="${user.birth}" placeholder="YYYY-MM-DD" />
        </div>
        <div>
          <label class="form-label" for="gender">성별</label>
          <select class="input" id="gender" name="gender">
            <option value="남" ${user.gender == '남' ? 'selected' : ''}>남성</option>
            <option value="여" ${user.gender == '여' ? 'selected' : ''}>여성</option>
          </select>
        </div>
      </div>

      <div class="form-row">
        <label class="form-label" for="name">이름</label>
        <input class="input" id="name" name="name" type="text" value="${user.name}" />
      </div>

      <div class="form-row">
        <label class="form-label" for="address">주소</label>
        <input class="input" id="address" name="address" type="text" value="${user.address}" />
      </div>

      <div class="form-actions">
        <button type="submit" class="btn primary">저장</button>
        <a class="btn ghost" href="${pageContext.request.contextPath}/user/mypage">취소</a>
      </div>
    </form>
  </main>
</body>
</html>
