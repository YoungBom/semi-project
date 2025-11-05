<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-lg shadow-sm py-3" style="background:#fff8e6;">
  <div class="container">

    <a class="navbar-brand fw-bold fs-3 text-brown" 
       href="${pageContext.request.contextPath}/main" 
       style="color:#b35a00;">
      🍔 BurgerHub
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navMenu">
      
      <ul class="navbar-nav me-auto mb-2 mb-lg-0 fw-semibold">
        <li class="nav-item"><a class="nav-link text-dark" href="${pageContext.request.contextPath}/menu.jsp">메뉴</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="${pageContext.request.contextPath}/event.jsp">이벤트</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="${pageContext.request.contextPath}/notice.jsp">공지사항</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="${pageContext.request.contextPath}/findStore.jsp">매장찾기</a></li>
      </ul>

      <form action="${pageContext.request.contextPath}/search" method="post" class="d-flex me-3">
        <input class="form-control me-2 rounded-3" style="border:1px solid #f4c430;" type="text" name="keyword" placeholder="버거 검색...">
        <button class="btn" type="submit" style="background:#ffc300; border:none;">
          <i class="bi bi-search"></i>
        </button>
      </form>

      <div class="d-flex">
        <a href="#" class="btn me-2 rounded-3" style="background:#4caf50; color:white;">로그인</a>
        <a href="${pageContext.request.contextPath}/burger/list" class="btn rounded-3" style="background:#ff8d00; color:white;">버거 리스트</a>
      </div>
    </div>

  </div>
</nav>
