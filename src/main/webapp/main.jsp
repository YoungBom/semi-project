<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
<link rel="stylesheet"
      href="<%=request.getContextPath()%>/resources/css/header.css?v=<%=System.currentTimeMillis()%>">
</head>

<body>
<!-- ✅ 헤더 include -->
<%@ include file="/include/header.jsp" %>

<!-- ✅ Hero Section -->
<section class="hero text-center text-white">
  <h1>국내 모든 버거 브랜드 한눈에!</h1>
  <p>맥도날드 · 버거킹 · 맘스터치 · 롯데리아 등 인기 버거 총집합</p>
</section>

<!-- ✅ DB 연결 -->
<sql:setDataSource var="db" 
   driver="com.mysql.cj.jdbc.Driver"
   url="jdbc:mysql://127.0.0.1:3306/semi_project"
   user="root"
   password="1234" />

<sql:query var="burgers" dataSource="${db}">
    SELECT * FROM burger ORDER BY brand, name;
</sql:query>

<!-- ✅ 버거 리스트 섹션 -->
<div class="container my-5">
  <h2 class="fw-bold mb-4 text-center">🔥 인기 버거 메뉴</h2>
  <div class="row justify-content-center">
    <c:forEach var="b" items="${burgers.rows}">
      <div class="col-md-3 col-sm-6 mb-4">
        <div class="card burger-card shadow-sm">
        
          <a href="${pageContext.request.contextPath}/burgerDetails?id=${b.id}" class="text-decoration-none text-dark">

			<c:choose>
			   	<c:when test="${fn:startsWith(b.image_path, '/')}">
			       	<img 
			           src="${pageContext.request.contextPath}${b.image_path}" 
			           class="card-img-top" 
			           alt="${b.name}"
			           style="height:200px; object-fit:cover;">
			   	</c:when>
		
	  	 		<c:otherwise>
			       	<img 
			           src="${b.image_path}" 
			           class="card-img-top" 
			           alt="${b.name}"
			           style="height:200px; object-fit:cover;">
	   			</c:otherwise>
			</c:choose>
			
			
            <div class="card-body">
              <span class="badge badge-brand">${b.brand}</span>
              <h5 class="card-title mt-2">${b.name}</h5>
              <p class="card-text text-secondary">${b.patty_type}</p>

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
