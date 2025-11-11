<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/user.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/mypage.css" rel="stylesheet">
</head>

<body class="mt-3">
<%@ include file="/include/header.jsp" %>

<main class="profile-wrap">
  <h1 class="profile-title">
    <a href="${pageContext.request.contextPath}/main.jsp" class="text-decoration-none">🍔</a> 마이페이지
  </h1>

  <c:if test="${not empty error}">
    <div class="alert error">${error}</div>
  </c:if>

  <!-- ✅ 메인 레이아웃: 좌측 프로필 / 우측 정보 -->
  <section class="profile-layout d-flex flex-wrap align-items-start gap-4">

    <!-- ✅ 왼쪽 프로필 -->
    <div class="text-center mb-4">
      <c:choose>
        <c:when test="${not empty user.profileImage}">
          <img id="profilePreview"
               src="${pageContext.request.contextPath}/profile/${user.profileImage}"
               alt="프로필 사진"
               style="width:150px;height:150px;border-radius:50%;object-fit:cover;">
        </c:when>
        <c:otherwise>
          <img id="profilePreview"
               src="${pageContext.request.contextPath}/img/default_profile.png"
               alt="기본 프로필"
               style="width:150px;height:150px;border-radius:50%;object-fit:cover;">
        </c:otherwise>
      </c:choose>
    
      <!-- 파일 업로드 폼 (숨김 상태) -->
      <form id="profileForm"
            action="${pageContext.request.contextPath}/user/updateProfile"
            method="post" enctype="multipart/form-data"
            style="display:none;">
        <input type="file" id="fileInput" name="profileImg" accept="image/*">
      </form>
    
      <!-- 사진 변경 버튼 -->
      <div class="mt-3">
        <button type="button" id="changeBtn" class="btn btn-warning">사진 변경</button>
      </div>
    </div>

    <!-- ✅ 오른쪽 정보 -->
    <div class="profile-right flex-grow-1">
      <section class="profile-card">
        <div class="profile-row position-relative">
          <span class="k">아이디</span>
          <span class="v">${user.userId}</span>
          <button type="button"
                  class="btn btn-outline-danger btn-sm px-3 position-absolute top-50 end-0 translate-middle-y me-3"
                  data-bs-toggle="modal"
                  data-bs-target="#deleteModal">
            탈퇴
          </button>
        </div>
        <div class="profile-row"><span class="k">이름</span><span class="v">${user.name}</span></div>
        <div class="profile-row"><span class="k">이메일</span><span class="v">${user.email}</span></div>
        <div class="profile-row"><span class="k">닉네임</span><span class="v">${user.nickname}</span></div>
        <div class="profile-row"><span class="k">권한</span><span class="v">${user.role}</span></div>
        <div class="profile-row"><span class="k">연락처</span><span class="v">${user.phone}</span></div>
        <div class="profile-row"><span class="k">생년월일</span><span class="v">${user.birth}</span></div>
        <div class="profile-row"><span class="k">성별</span><span class="v">${user.gender}</span></div>
        <div class="profile-row"><span class="k">주소</span><span class="v">${user.address}</span></div>
      </section>

      <div class="profile-actions d-flex mt-4">
        <a class="btn primary text-center lh-1" href="${pageContext.request.contextPath}/user/edit">정보 수정</a>
        <a class="btn secondary text-center lh-1" href="${pageContext.request.contextPath}/logout">로그아웃</a>
        <a class="btn btn-outline-warning text-center lh-1 ms-auto" href="${pageContext.request.contextPath}/review/list">나의 리뷰</a>
      </div>
    </div>
  </section>
</main>

<!-- 회원탈퇴 모달 -->
<div class="modal fade" id="deleteModal" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">회원 탈퇴</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <p>아이디를 입력하세요.</p>
        <input type="password" id="deletePw" class="form-control" placeholder="아이디 입력">
        <div id="deleteMsg" class="text-danger small mt-2"></div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
        <button class="btn btn-danger" id="confirmDeleteBtn">확인</button>
      </div>
    </div>
  </div>
</div>

<!-- JS -->
<script>
  const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/resources/js/mypage.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/profile.js"></script>

<%@ include file="/include/footer.jsp" %>
</body>
</html>
