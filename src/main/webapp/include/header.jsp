<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String ctx = request.getContextPath();
  Object uidObj = (session == null) ? null : session.getAttribute("LOGIN_UID");
  String userName = (session == null) ? null : (String) session.getAttribute("LOGIN_NAME");
  boolean loggedIn = (uidObj != null);
%>

<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

<div id="site-header">
<nav class="navbar navbar-expand-lg shadow-sm py-3" style="background:#fff8e6;">
  <div class="container">

    <!-- 브랜드 링크: 필요 시 /main.jsp 유지 -->
    <a class="navbar-brand fw-bold fs-3 text-brown" href="<%=ctx%>/main" style="color:#b35a00;">
      🍔 BurgerHub
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navMenu">

      <!-- ⛔ 아래 메뉴는 요청대로 수정하지 않음 -->
      <ul class="navbar-nav me-auto mb-2 mb-lg-0 fw-semibold">
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/burger/menu">메뉴</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/event.jsp">이벤트</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/notice.jsp">공지사항</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/findStore.jsp">매장찾기</a></li>
      </ul>

      <!-- 검색 폼 -->
      <form action="<%=ctx%>/burger/menu" method="get" class="d-flex me-3">
        <input class="form-control me-2 rounded-3" style="border:1px solid #f4c430;" type="text" name="keyword" placeholder="버거 검색...">
        <button class="btn" type="submit" style="background:#ffc300; border:none;">
          <i class="bi bi-search" aria-hidden="true"></i><span class="visually-hidden">검색</span>
        </button>
      </form>

      
      <div class="d-flex align-items-center gap-2">
        <% if (!loggedIn) { %>
         
          <a href="<%=ctx%>/user/login.jsp" class="btn me-1 rounded-3" style="background:#4caf50; color:white;">로그인</a>
          <a href="<%=ctx%>/user/register.jsp" class="btn me-1 rounded-3 btn-primary">회원가입</a>
          
          <a href="<%=ctx%>/burger/list" class="btn rounded-3" style="background:#ff8d00; color:white;">버거 리스트</a>
        <% } else { %>
          <span class="me-2 user-greeting text-nowrap"
          	 	title="<%= (userName == null ? "회원" : userName) %>님"
          >
           <%=(userName == null ? "회원" : userName) %>님</span>
          <a href="<%=ctx%>/user/mypage" class="btn me-1 rounded-3" style="background:#ff8d00; color:white;">마이페이지</a>
          <form method="post" action="<%=ctx%>/logout" class="d-inline m-0 p-0">
            <button type="submit" class="btn me-1 rounded-3" style="background:#4caf50; color:white;">로그아웃</button>
          </form>
        <% } %>
      </div>

    </div>
  </div>
</nav>
</div>
