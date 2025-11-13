<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>내 리뷰 - BurgerHub 🍔</title>

	<!-- ✅ Bootstrap & Fonts -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/reviewList.css" rel="stylesheet">

</head>
<body>
<%@ include file="/include/header.jsp" %>

<main class="container py-5">
  <h2 class="fw-bold mb-4 text-center">🍔 내 리뷰 목록</h2>

  <!-- 리뷰가 없을 때 -->
  <c:choose>
  	<c:when test="${empty reviewAllList}">
		<div class="alert alert-secondary text-center">
			아직 작성한 리뷰가 없습니다 😢
		</div>
	</c:when>
	<c:otherwise>
		  <div class="row row-cols-1 row-cols-md-2 g-4">
			<c:forEach var="review" items="${reviewAllList}">
			    <!-- 리뷰 카드  -->
			    <div class="col">
			    	<!-- 카드 전체가 같은 높이를 유지하도록 -->
			    	<div class="card h-100 d-flex flex-column p-3 shadow-sm border-0">
			    		<!-- 상단: 버거 정보 -->
				        <div class="d-flex align-items-center mb-3">
				          <div class="flex-grow-1">
				            <h5 class="mb-1 fw-bold">${review.burgerName}</h5>
				            <small class="text-muted">${review.brand}</small>
				          </div>
				          <div class="rating fw-bold text-warning">${review.rating}</div>
				        </div>
				        
						<!-- 본문 내용 -->
				        <p class="mb-2">${review.content}</p>
						<!-- 이미지 영역 (고정 높이) -->
				        <div class="d-flex flex-wrap mb-3 gap-2">
							<c:forEach var="img" items="${review.imageList}">
								<img src="${pageContext.request.contextPath}/image/${img}" class="review-img" alt="리뷰 이미지">
							</c:forEach>
						</div>
				
						<!-- ✅ 하단 고정 footer -->
						<div class="mt-auto d-flex justify-content-between align-items-center">
							<c:choose>
								<c:when test="${review.createdAt ne review.updatedAt}">
								<small class="text-muted">${review.updatedAt} (수정됨)</small>
								</c:when>
								<c:otherwise>
									<small class="text-muted">${review.createdAt}</small>
								</c:otherwise>
							</c:choose>
							<div>
								<a href="/semi-project/burger/details?id=${review.burgerId}" class="btn btn-outline-secondary btn-sm me-2">
									<i class="bi bi-eye"></i> 보기
								</a>
							</div>
						</div>
			    	</div>
			    </div>
			</c:forEach>
		  </div>
	</c:otherwise>
  </c:choose>

</main>

<%@ include file="/include/footer.jsp" %>

</body>
</html>