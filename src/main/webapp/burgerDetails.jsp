<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${burger.name} - BurgerHub 🍔</title>

<!-- ✅ Bootstrap + Google Fonts + main.css 그대로 불러오기 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>

<body>
<!-- ✅ 헤더 -->
<%@ include file="/include/header.jsp" %>

<!-- ✅ 버거 상세 섹션 -->
<main class="container my-5 py-5" style="max-width:1100px;">
  <div class="row align-items-start justify-content-center g-5">
    
    <!-- 🍔 왼쪽: 이미지 -->
    <div class="col-md-5 text-center">
      <c:choose>
        <c:when test="${not empty burger.imagePath}">
          <img src="${pageContext.request.contextPath}${burger.imagePath}" 
               alt="${burger.name}" class="img-fluid rounded-4 shadow">
        </c:when>
        <c:otherwise>
          <img src="${pageContext.request.contextPath}/image/1.png" 
               alt="기본 버거 이미지" class="img-fluid rounded-4 shadow">
        </c:otherwise>
      </c:choose>
    </div>

    <!-- 📋 오른쪽: 정보 -->
    <div class="col-md-6">
      <h2 class="fw-bold text-dark mb-2">${burger.name}</h2>
      <p class="text-secondary mb-1">${burger.brand}</p>
      <h4 class="text-warning fw-bold mb-4">${burger.price}원</h4>

      <div class="p-4 bg-white rounded-4 shadow-sm">
        <h5 class="text-warning fw-bold mb-3">영양 정보</h5>
        <ul class="list-unstyled mb-0">
          <li>칼로리: ${details.calories} kcal</li>
          <li>탄수화물: ${details.carbohydrates} g</li>
          <li>단백질: ${details.protein} g</li>
          <li>지방: ${details.fat} g</li>
          <li>나트륨: ${details.sodium} mg</li>
          <li>당류: ${details.sugar} g</li>
          <li>알레르기 정보: ${details.allergyInfo}</li>
        </ul>
      </div>
    </div>
  </div>
</main>

<!-- ✅ 리뷰 (작성 전 주석처리) -->
<%-- <%@ include file="/include/review.jsp" %> --%>

<!-- ✅ 푸터 -->
<%@ include file="/include/footer.jsp" %>

</body>
</html>
