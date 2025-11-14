<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>BurgerHub 🍔</title>

	<!-- ✅ Bootstrap & Fonts -->
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
	  <h2 class="fw-bold mb-5 text-center">🔥 새로운 버거</h2>
	  <div class="row justify-content-center">
	    <c:set var="shown" value="0" />
	    <c:forEach var="b" items="${newBurgerList}">
	      <c:if test="${b.newBurger and shown < 4}">
	      
	      <!-- 버거 카드 -->
			<%@ include file="/include/burgerCard.jsp" %>
	        
	        
	        <c:set var="shown" value="${shown + 1}" />
	      </c:if>
	    </c:forEach>
	  </div>
	</div>
	
	<!-- ✅ 인기 버거 메뉴 -->
	<div class="container mt-5">
	  <h2 class="fw-bold mb-5 text-center">🔥 인기 버거 메뉴</h2>
	  <div class="row justify-content-center">
	    <c:forEach var="b" items="${topRatedList}">
	    
	    	<!-- 버거 카드 -->
			<%@ include file="/include/burgerCard.jsp" %>
	      
	    </c:forEach>
	  </div>
	</div>
  
	<%@ include file="/include/footer.jsp" %>
  

</body>
</html>
