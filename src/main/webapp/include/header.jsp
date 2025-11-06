<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String ctx = request.getContextPath();
  Object uidObj = (session == null) ? null : session.getAttribute("LOGIN_UID");
  String userName = (session == null) ? null : (String) session.getAttribute("LOGIN_NAME");
  boolean loggedIn = (uidObj != null);
%>

<!-- ✅ 헤더 전용 스코프 래퍼 -->
<div class="burger-header">

  <!-- ✅ Bootstrap (전역에서 이미 로드되어도 충돌 없음) -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ 헤더 전용 CSS -->
  <link rel="stylesheet" href="<%=ctx%>/resources/css/header.css">

  <!-- ============================== -->
  <!-- 🍔 BurgerHub 헤더 영역 -->
  <!-- ============================== -->
  <nav class="navbar navbar-expand-lg shadow-sm py-3">
    <div class="container">

      <!-- 브랜드 -->
      <a class="navbar-brand fw-bold fs-3" href="<%=ctx%>/main.jsp">
        🍔 BurgerHub
      </a>

      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navMenu">
        <!-- 메뉴 -->
        <ul class="navbar-nav me-auto mb-2 mb-lg-0 fw-semibold">
          <li class="nav-item"><a class="nav-link" href="<%=ctx%>/menu.jsp">메뉴</a></li>
          <li class="nav-item"><a class="nav-link" href="<%=ctx%>/event.jsp">이벤트</a></li>
          <li class="nav-item"><a class="nav-link" href="<%=ctx%>/notice.jsp">공지사항</a></li>
          <li class="nav-item"><a class="nav-link" href="<%=ctx%>/findStore.jsp">매장찾기</a></li>
        </ul>

        <!-- 검색창 -->
        <form action="<%=ctx%>/search" method="post" class="d-flex me-3 search-form">
          <input class="form-control me-2 rounded-3 search-input" type="text" name="keyword" placeholder="버거 검색...">
          <button class="btn search-btn" type="submit">
            <i class="bi bi-search"></i>
          </button>
        </form>

        <!-- 로그인/회원가입 -->
        <div class="d-flex align-items-center gap-2 user-actions">
          <% if (!loggedIn) { %>
            <a href="<%=ctx%>/user/login.jsp" class="btn login-btn">로그인</a>
            <a href="<%=ctx%>/user/register.jsp" class="btn register-btn">회원가입</a>
            <a href="<%=ctx%>/burger/list" class="btn list-btn">버거 리스트</a>
          <% } else { %>
            <span class="me-2">안녕하세요, <%= (userName == null ? "회원" : userName) %>님</span>
            <a href="<%=ctx%>/user/mypage" class="btn my-btn">마이페이지</a>
            <form method="post" action="<%=ctx%>/logout" class="d-inline m-0 p-0">
              <button type="submit" class="btn logout-btn">로그아웃</button>
            </form>
          <% } %>
        </div>

      </div>
    </div>
  </nav>
</div>
