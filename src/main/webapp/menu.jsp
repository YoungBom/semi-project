<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="dao.BurgerSearchDAO"%>
<%@ page import="dto.BurgerDTO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    request.setCharacterEncoding("UTF-8");

    // 검색 결과가 있으면 그대로 사용
    List<BurgerDTO> burgers = (List<BurgerDTO>) request.getAttribute("burgers");

    // 검색 결과가 없으면 전체 메뉴 불러오기
    if (burgers == null) {
        BurgerSearchDAO dao = new BurgerSearchDAO();
        burgers = dao.getAllburger();
        request.setAttribute("burgers", burgers);
    }
%>


<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>BurgerHub | 전체 메뉴</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
 <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>

<body>

  <%@ include file="/include/header.jsp" %>

  <div class="container mt-5">
  
	  <c:choose>
	    <c:when test="${not empty keyword}">
	      <h2 class="fw-bold mb-3">🔍 검색 결과 메뉴</h2>
	      <p class="text-muted mb-5">"${keyword}" 에 대한 검색 결과입니다.</p>
	    </c:when>
	
	    <c:otherwise>
	      <h2 class="fw-bold mb-3">🍔 전체 메뉴</h2>
	      <p class="text-muted mb-5">원하는 버거를 골라보세요!</p>
	    </c:otherwise>
	  </c:choose>
	  <div class="filter-btns">
		  <button class="filter-btn active" data-type="all">전체</button>
		  <button class="filter-btn" data-type="비프">비프</button>
		  <button class="filter-btn" data-type="치킨">치킨</button>
		  <button class="filter-btn" data-type="기타">기타</button>
	 </div>
     <c:choose>
      <c:when test="${empty burgers}">
        <div class="text-center mt-5 mb-5">
          <p>🍔 검색된 버거가 없습니다 😢</p>
        </div>
      </c:when>
      <c:otherwise>
        <div class="row g-4">
          <c:forEach var="b" items="${burgers}">
            <div class="col-12 col-sm-6 col-md-4 col-lg-3">
              <div class="card burger-card shadow-sm">
              	<a href="${pageContext.request.contextPath}/burger/details?id=${b.id}" class="text-decoration-none text-dark">
               <c:choose>
				  <c:when test="${not empty b.imagePath and fn:startsWith(b.imagePath, '/')}">
				    <img 
				      src="${pageContext.request.contextPath}${b.imagePath}" 
				      class="card-img-top" 
				      alt="${b.name}"
				      style="height:200px; object-fit:contain;">
				  </c:when>
				
				  <c:when test="${not empty b.imagePath}">
				    <img 
				      src="${b.imagePath}" 
				      class="card-img-top" 
				      alt="${b.name}"
				      style="height:200px; object-fit:contain;">
				  </c:when>
				
				  <c:otherwise>
				    <img 
				      src="${pageContext.request.contextPath}/image/noimage.png" 
				      class="card-img-top" 
				      alt="이미지 없음"
				      style="height:200px; object-fit:contain;">
				  </c:otherwise>
				</c:choose>
				
                <div class="card-body text-center">
                  <span class="badge badge-brand d-inline-block mb-2">${b.brand}</span>
                  <h5 class="card-title">${b.name}</h5>
                  <p class="card-text">${b.pattyType}</p>
                  <p class="price">${b.price}원</p>
                </div>
                </a>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>

  <%@ include file="/include/footer.jsp" %>
  <script src="${pageContext.request.contextPath}/resources/js/filter.js"></script>
</body>
</html>
