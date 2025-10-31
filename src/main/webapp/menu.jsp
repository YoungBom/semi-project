<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>BurgerHub 메뉴</title>

<!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Google Fonts -->
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">

<!-- Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

<!-- main.css -->
<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
</head>
<body>

<!-- ✅ 헤더 -->
<%@ include file="/include/header.jsp" %>

<!-- ✅ Hero 섹션 -->
<section class="hero">
  <h1>🍔 전체 버거 메뉴</h1>
  <p>모든 브랜드의 버거를 한눈에 확인하세요!</p>
</section>

<!-- ✅ DB 연결 -->
<sql:setDataSource var="db" 
   driver="com.mysql.cj.jdbc.Driver"
   url="jdbc:mysql://127.0.0.1:3306/semi_project"
   user="root"
   password="1234" />

<!-- ✅ 검색어에 따라 필터링된 쿼리 실행 -->
<c:choose>
  <c:when test="${not empty param.keyword}">
    <sql:query var="burgers" dataSource="${db}">
      SELECT * FROM burger
      WHERE name LIKE CONCAT('%', ?, '%') OR brand LIKE CONCAT('%', ?, '%')
      ORDER BY brand, name;
      <sql:param value="${param.keyword}" />
      <sql:param value="${param.keyword}" />
    </sql:query>
  </c:when>
  <c:otherwise>
    <sql:query var="burgers" dataSource="${db}">
      SELECT * FROM burger ORDER BY brand, name;
    </sql:query>
  </c:otherwise>
</c:choose>

<!-- ✅ 버거 리스트 -->
<div class="container my-5">
  <h2 class="fw-bold mb-4 text-center">
    <c:choose>
      <c:when test="${not empty param.keyword}">
        🔍 "${param.keyword}" 검색 결과
      </c:when>
      <c:otherwise>
        🍟 전체 버거 메뉴
      </c:otherwise>
    </c:choose>
  </h2>

  <div class="row justify-content-center">
    <c:forEach var="b" items="${burgers.rows}">
      <div class="col-md-3 col-sm-6 mb-4">
        <div class="card burger-card">
          <img src="${pageContext.request.contextPath}/image/1.png" class="card-img-top" alt="${b.name}">
          <div class="card-body">
            <span class="badge badge-brand">${b.brand}</span>
            <h5 class="card-title">${b.name}</h5>
            <p class="card-text">${b.patty_type}</p>
            <div class="d-flex justify-content-between align-items-center mt-3">
              <span class="price">${b.price}원</span>
              <span class="rating">⭐</span>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>

    <c:if test="${burgers.rowCount == 0}">
      <p class="text-center text-muted mt-4">검색 결과가 없습니다 😢</p>
    </c:if>
  </div>
</div>

<!-- ✅ Footer -->
<%@ include file="/include/footer.jsp" %>

</body>
</html>
