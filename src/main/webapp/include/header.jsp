<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm py-3">
  <div class="container">

    <a class="navbar-brand fw-bold fs-4" href="${pageContext.request.contextPath}/main.jsp">
      🍔 BurgerHub
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navMenu">
      
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/menu.jsp">메뉴</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/event.jsp">이벤트</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/notice.jsp">공지사항</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/customerService.jsp">고객센터</a></li>
      </ul>

      <form action="${pageContext.request.contextPath}/search" method="post" class="d-flex me-3">
        <input class="form-control me-2" type="text" name="keyword" placeholder="버거 검색...">
        <button class="btn btn-warning" type="submit"><i class="bi bi-search"></i></button>
      </form>

      <div class="d-flex">
        <a href="#" class="btn btn-outline-light me-2">로그인</a>
        <a href="${pageContext.request.contextPath}/burger/list" class="btn btn-warning">버거리스트</a>
      </div>
    </div>

  </div>
</nav>
