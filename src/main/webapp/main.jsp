<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

<!-- ✅ Bootstrap & Google Fonts -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">



<!-- ✅ main.css -->
<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
</head>

<body>
<%@ include file="/include/header.jsp" %>

<!-- ✅ 메인 배너, 3초(3000ms)마다 슬라이드 되게함 -->
<div id="heroCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
  <div class="carousel-inner">

    <div class="carousel-item active hero-slide" style="background-image: url('${pageContext.request.contextPath}/image/banner1.png');">
      <div class="hero-content">
        <h1>국내 BEST 3 브랜드 버거 총집합!</h1>
        <p>맥도날드 · 버거킹 · 롯데리아 인기버거 한눈에 🍔🔥</p>
      </div>
    </div>

    <div class="carousel-item hero-slide" style="background-image: url('${pageContext.request.contextPath}/image/banner2.png');">
      <div class="hero-content">
        <h1>지금 인기 급상승 메뉴!</h1>
        <p>유저 평점 기반 BEST 메뉴 추천</p>
      </div>
    </div>

    <div class="carousel-item hero-slide" style="background-image: url('${pageContext.request.contextPath}/image/banner3.png');">
      <div class="hero-content">
        <h1>버거 매니아들의 피드백</h1>
        <p>실시간 리뷰 & 사진 업로드</p>
      </div>
    </div>

  </div>
  
  <!-- 배너 컨트롤하는 부분 (전,후 화살표) -->
  <button class="carousel-control-prev" type="button" data-bs-target="#heroCarousel" data-bs-slide="prev">
    <span class="carousel-control-prev-icon bg-dark rounded-circle p-3"></span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#heroCarousel" data-bs-slide="next">
    <span class="carousel-control-next-icon bg-dark rounded-circle p-3"></span>
  </button>
</div>



<!-- ✅ 버거 리스트 섹션 -->
<div class="container mt-5 text-center">
  <h2 class="fw-bold mb-4 text-center">🔥 인기 버거 메뉴</h2>
  <div class="row justify-content-center">
    <c:forEach var="b" items="${burgerList}">
      <div class="col-md-3 col-sm-6 mb-4">
        <div class="card burger-card shadow-sm">
        
          <a href="${pageContext.request.contextPath}/burger/details?id=${b.id}" class="text-decoration-none text-dark">

			<c:choose>
			   	<c:when test="${fn:startsWith(b.imagePath, '/')}">
			       	<img 
			           src="${pageContext.request.contextPath}${b.imagePath}" 
			           class="card-img-top" 
			           alt="${b.name}"
			           style="height:200px; object-fit:contain;">
			   	</c:when>
	  	 		<c:otherwise>
			       	<img 
			           src="${b.imagePath}" 
			           class="card-img-top" 
			           alt="${b.name}"
			           style="height:200px; object-fit:contain;">
	   			</c:otherwise>
			</c:choose>
            <div class="card-body">
              <span class="badge badge-brand">${b.brand}</span>
              <h5 class="card-title mt-2">${b.name}</h5>
              <p class="card-text text-secondary">${b.pattyType}</p>

              <div class="d-flex justify-content-between align-items-center mt-3">
                <span class="price fw-bold text-warning">${b.price}원</span>
                <span class="rating">⭐</span>
              </div>
            </div>
          </a>

        </div>
      </div>
    </c:forEach>
  </div>
</div>

<!-- ✅ 푸터 include -->
<%@ include file="/include/footer.jsp" %>
</body>
</html>
