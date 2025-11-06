<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    if (request.getAttribute("burgerList") == null) {
        response.sendRedirect(request.getContextPath() + "/main");
        return;
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>BurgerHub 🍔</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
</head>

<body>
<%@ include file="/include/header.jsp" %>

<!-- ✅ 메인 배너 -->
<div id="heroCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
  <div class="carousel-inner">
    <div class="carousel-item active hero-slide" style="background-image: url('${pageContext.request.contextPath}/img/banner1.png');">
      <div class="hero-content">
        <h1>국내 BEST 3 브랜드 버거 총집합!</h1>
        <p>맥도날드 · 버거킹 · 롯데리아 인기버거 한눈에 🍔🔥</p>
      </div>
    </div>
    <div class="carousel-item hero-slide" style="background-image: url('${pageContext.request.contextPath}/img/banner2.png');">
      <div class="hero-content">
        <h1>지금 인기 급상승 메뉴!</h1>
        <p>유저 평점 기반 BEST 메뉴 추천</p>
      </div>
    </div>
    <div class="carousel-item hero-slide" style="background-image: url('${pageContext.request.contextPath}/img/banner3.png');">
      <div class="hero-content">
        <h1>버거 매니아들의 피드백</h1>
        <p>실시간 리뷰 & 사진 업로드</p>
      </div>
    </div>
  </div>
  <button class="carousel-control-prev" type="button" data-bs-target="#heroCarousel" data-bs-slide="prev">
    <span class="carousel-control-prev-icon bg-dark rounded-circle p-3"></span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#heroCarousel" data-bs-slide="next">
    <span class="carousel-control-next-icon bg-dark rounded-circle p-3"></span>
  </button>
</div>

<!-- ✅ 새로운 버거 섹션 -->
<div class="container my-5">
  <h2 class="fw-bold mb-4 text-center">🔥 새로운 버거</h2>
  <div class="row justify-content-center">
    <c:set var="shown" value="0" />
    <c:forEach var="b" items="${burgerList}">
      <c:if test="${b.newBurger and shown < 4}">
        <div class="col-md-3 col-sm-6 mb-4">
          <div class="card burger-card shadow-sm position-relative">
            
            <!-- ✅ 책갈피 브랜드 로고 -->
            <div class="brand-flag">
              <img src="${pageContext.request.contextPath}/img/${b.brand eq '맥도날드' ? 'mcdonalds_logo.png' : (b.brand eq '버거킹' ? 'burgerking_logo.png' : (b.brand eq '롯데리아' ? 'lotteria_logo.png' : 'default_logo.png'))}"
                   alt="${b.brand} 로고">
            </div>

            <a href="${pageContext.request.contextPath}/burger/details?id=${b.id}" class="text-decoration-none text-dark">
              <c:choose>
                <c:when test="${fn:startsWith(b.imagePath, '/')}">
                  <img src="${pageContext.request.contextPath}${b.imagePath}" class="card-img-top" alt="${b.name}" style="height:200px; object-fit:contain;">
                </c:when>
                <c:otherwise>
                  <img src="${b.imagePath}" class="card-img-top" alt="${b.name}" style="height:200px; object-fit:contain;">
                </c:otherwise>
              </c:choose>
              <div class="card-body text-start">
                <span class="badge bg-danger text-light">NEW</span>
                <h5 class="card-title mt-2">${b.name}</h5>
                <span class="badge patty-badge ${b.pattyType}"> ${b.pattyType} </span>
                <div class="d-flex justify-content-between align-items-center mt-3">
                  <span class="price fw-bold text-warning">${b.price}원</span>
                  <span class="rating">⭐</span>
                </div>
              </div>
            </a>
          </div>
        </div>
        <c:set var="shown" value="${shown + 1}" />
      </c:if>
    </c:forEach>
  </div>
</div>

<!-- ✅ 인기 버거 메뉴 -->
<div class="container mt-5">
  <h2 class="fw-bold mb-4 text-center">🔥 인기 버거 메뉴</h2>
  <div class="row justify-content-center">
    <c:forEach var="b" items="${burgerList}">
      <div class="col-md-3 col-sm-6 mb-4">
        <div class="card burger-card shadow-sm position-relative">
          
          <!-- ✅ 책갈피 브랜드 로고 -->
          <div class="brand-flag">
            <img src="${pageContext.request.contextPath}/img/${b.brand eq '맥도날드' ? 'mcdonalds_logo.png' : (b.brand eq '버거킹' ? 'burgerking_logo.png' : (b.brand eq '롯데리아' ? 'lotteria_logo.png' : 'default_logo.png'))}"
                 alt="${b.brand} 로고">
          </div>

          <a href="${pageContext.request.contextPath}/burger/details?id=${b.id}" class="text-decoration-none text-dark">
            <c:choose>
              <c:when test="${fn:startsWith(b.imagePath, '/')}">
                <img src="${pageContext.request.contextPath}${b.imagePath}" class="card-img-top" alt="${b.name}" style="height:200px; object-fit:contain;">
              </c:when>
              <c:otherwise>
                <img src="${b.imagePath}" class="card-img-top" alt="${b.name}" style="height:200px; object-fit:contain;">
              </c:otherwise>
            </c:choose>
            <div class="card-body text-start">
              <h5 class="card-title mt-2">${b.name}</h5>
              <span class="badge patty-badge ${b.pattyType}"> ${b.pattyType} </span>
              <div class="d-flex justify-content-between align-items-center mt-3">
                <span class="price fw-bold text-warning">${b.price}원</span>
                <span class="rating text-warning">
                  <fmt:parseNumber value="${b.avgRating}" integerOnly="true" var="starFull" />
                  <c:forEach begin="1" end="${starFull}" var="i">★</c:forEach>
                  <c:forEach begin="1" end="${5 - starFull}" var="i">☆</c:forEach>
                  <small>(<fmt:formatNumber value="${b.avgRating}" maxFractionDigits="1" />)</small>
                </span>
              </div>
            </div>
          </a>
        </div>
      </div>
    </c:forEach>
  </div>
</div>

<%@ include file="/include/footer.jsp" %>
</body>
</html>
