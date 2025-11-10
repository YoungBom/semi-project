<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

<style>
body {
  background-color: #f8f9fa;
}

.card {
  border: none;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}
.card:hover {
  transform: translateY(-2px);
}

.review-img {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  object-fit: cover;
  margin-right: 8px;
}

.rating {
  color: #ffc107;
  font-size: 15px;
}

.btn-outline-warning {
  color: #f39c12;
  border-color: #f39c12;
}
.btn-outline-warning:hover {
  background-color: #f39c12;
  color: white;
}
</style>
</head>
<body>
<%@ include file="/include/header.jsp" %>

<main class="container py-5">
  <h2 class="fw-bold mb-4 text-center">🍔 내 리뷰 목록</h2>

  <!-- 리뷰가 없을 때 -->
  <!-- <div class="alert alert-secondary text-center">
    아직 작성한 리뷰가 없습니다 😢
  </div> -->

  <div class="row row-cols-1 row-cols-md-2 g-4">
    <!-- 리뷰 카드 1 -->
    <div class="col">
      <div class="card p-3">
        <div class="d-flex align-items-center mb-3">
          <div class="flex-grow-1">
            <h5 class="mb-1 fw-bold">빅맥</h5>
            <small class="text-muted">맥도날드</small>
          </div>
          <div class="rating">★★★★☆</div>
        </div>

        <p class="mb-2">고기 맛이 진하고 소스가 진짜 맛있어요. 다만 약간 짰어요.</p>

        <div class="d-flex mb-3">
          <img src="https://via.placeholder.com/100x100?text=Review1" class="review-img" alt="리뷰 이미지">
          <img src="https://via.placeholder.com/100x100?text=Review2" class="review-img" alt="리뷰 이미지">
        </div>

        <div class="d-flex justify-content-between align-items-center">
          <small class="text-muted">2025-11-08 14:32 (수정됨)</small>
          <div>
            <a href="#" class="btn btn-outline-secondary btn-sm me-2">
              <i class="bi bi-eye"></i> 보기
            </a>
            <a href="#" class="btn btn-outline-danger btn-sm">
              <i class="bi bi-trash"></i> 삭제
            </a>
          </div>
        </div>
      </div>
    </div>

    <!-- 리뷰 카드 2 -->
    <div class="col">
      <div class="card p-3">
        <div class="d-flex align-items-center mb-3">
          <div class="flex-grow-1">
            <h5 class="mb-1 fw-bold">통새우와퍼</h5>
            <small class="text-muted">버거킹</small>
          </div>
          <div class="rating">★★★★★</div>
        </div>

        <p class="mb-2">새우가 통통하고 패티도 두꺼워서 완전 만족! 또 먹을 거예요 😋</p>

        <div class="d-flex mb-3">
          <img src="https://via.placeholder.com/100x100?text=Shrimp1" class="review-img" alt="리뷰 이미지">
        </div>

        <div class="d-flex justify-content-between align-items-center">
          <small class="text-muted">2025-10-31 09:15</small>
          <div>
            <a href="#" class="btn btn-outline-secondary btn-sm me-2">
              <i class="bi bi-eye"></i> 보기
            </a>
            <a href="#" class="btn btn-outline-danger btn-sm">
              <i class="bi bi-trash"></i> 삭제
            </a>
          </div>
        </div>
      </div>
    </div>

    <!-- 리뷰 카드 3 -->
    <div class="col">
      <div class="card p-3">
        <div class="d-flex align-items-center mb-3">
          <div class="flex-grow-1">
            <h5 class="mb-1 fw-bold">한우불고기버거</h5>
            <small class="text-muted">롯데리아</small>
          </div>
          <div class="rating">★★★☆☆</div>
        </div>

        <p class="mb-2">무난한 맛이에요. 특별한 점은 없지만 가끔 생각날 듯!</p>

        <div class="d-flex mb-3">
          <img src="https://via.placeholder.com/100x100?text=Bulgogi" class="review-img" alt="리뷰 이미지">
        </div>

        <div class="d-flex justify-content-between align-items-center">
          <small class="text-muted">2025-09-20 18:05</small>
          <div>
            <a href="#" class="btn btn-outline-secondary btn-sm me-2">
              <i class="bi bi-eye"></i> 보기
            </a>
            <a href="#" class="btn btn-outline-danger btn-sm">
              <i class="bi bi-trash"></i> 삭제
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

<%@ include file="/include/footer.jsp" %>

</body>
</html>