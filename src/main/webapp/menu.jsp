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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>

<body>

  <%@ include file="/include/header.jsp" %>

  <div class="container mt-5">
    <h2 class="fw-bold mb-3">🍔 전체 메뉴</h2>
    <p class="text-muted mb-5">원하는 버거를 골라보세요!</p>

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
              	<a href="${pageContext.request.contextPath}/burgerDetails?id=${b.id}" class="text-decoration-none text-dark">
               <c:choose>
				  <c:when test="${not empty b.imagePath and fn:startsWith(b.imagePath, '/')}">
				    <img 
				      src="${pageContext.request.contextPath}${b.imagePath}" 
				      class="card-img-top" 
				      alt="${b.name}"
				      style="height:200px; object-fit:cover;">
				  </c:when>
				
				  <c:when test="${not empty b.imagePath}">
				    <img 
				      src="data:image/png;base64,${b.imagePath}" 
				      class="card-img-top" 
				      alt="${b.name}"
				      style="height:200px; object-fit:cover;">
				  </c:when>
				
				  <c:otherwise>
				    <img 
				      src="${pageContext.request.contextPath}/image/noimage.png" 
				      class="card-img-top" 
				      alt="이미지 없음"
				      style="height:200px; object-fit:cover;">
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
      </c:otherwise>
    </c:choose>
  </div>

  <%@ include file="/include/footer.jsp" %>

</body>
</html>
