<%@page import="dto.BurgerDTO"%>
<%@page import="dao.ReviewDAO"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>
<%@page import="dto.ReviewDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${burger.name} - BurgerHub 🍔</title>

<!-- ✅ Bootstrap & Fonts -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">

<style>
body {
  font-family: 'Poppins', sans-serif;
  background-color: #fffaf0;
  transition: background 0.5s ease;
}

/* 공통 카드 */
.burger-card, .card{
  background: #fff;
  border-radius: 30px;
  box-shadow: 0 15px 35px rgba(0,0,0,0.08);
  padding: 50px 60px;
  max-width: 1100px;
  margin: auto;
  transition: all 0.4s ease;
}

.burger-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.1);
}

/* 이미지 */
.burger-image {
  background: #fffef8;
  border-radius: 25px;
  padding: 10px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.08);
  display: flex;
  justify-content: center;
  align-items: center;
  height: 310px;
}
.burger-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 20px;
}

/* 타이틀 */
.title-container {
  display: flex;
  align-items: center;
  gap: 12px;
}

.burger-logo {
  max-width: 55px;
  max-height: 55px;
  object-fit: contain;
  border-radius: 6px;
  flex-shrink: 0;
}

.burger-title {
  position: relative;
  font-weight: 800;
  font-size: 2.3rem;
  margin-bottom: 0.6rem;
  color: var(--main-color);
  display: inline-block;
  overflow: visible; /* 수정됨 */
}

.burger-title::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: -6px;
  width: 0;
  height: 4px;
  background-color: var(--main-color, #ff9900);
  border-radius: 4px;
  transition: width 0.4s ease-in-out;
}

/* ✅ 카드 hover 시 밑줄 등장 */
.burger-card:hover .burger-title::after {
  width: 100%;
}

/* ✅ 패티 타입 표시 */
.patty-type {
  font-weight: 600;
  font-size: 1.1rem;
  margin-bottom: 0.3rem;
  color: var(--main-color);
  background-color: rgba(255, 153, 0, 0.08);
  padding: 5px 10px;
  border-radius: 10px;
  display: inline-block;
}

/* 영양정보 카드 */
.nutrition-card {
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 8px 25px rgba(0,0,0,0.06);
  padding: 30px 35px;
  transition: 0.3s ease;
  border-top: 5px solid var(--main-color);
}
.nutrition-card h5 {
  font-weight: 700;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--main-color);
}
.nutrition-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  row-gap: 12px;
  column-gap: 15px;
}
.nutrition-item {
  background-color: #fff9e6;
  padding: 10px 15px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 10px;
}
.nutrition-item i {
  color: var(--main-color);
}

/* ✅ 브랜드별 테마 색상 */
:root { --main-color: #ff9900; }

body.mcdonalds {
  background: #fff5f5;
  --main-color: #ffb300;
}

body.burgerking {
  background: #fff8f0;
  --main-color: #b22222;
}

body.lotteria {
  background: #fff4f4;
  --main-color: #e60012;
}

/* ✅ 리뷰 */
.form-control:focus {
    border-color: #F59E0B;
    box-shadow: 0 0 0 0.2rem rgba(245, 158, 11, 0.25);
  }
.btn-warning:hover {
	background-color: #F59E0B;
	border-color: #F59E0B;
}
</style>

<script type="text/javascript">
	function checkForm(e) {
		const ratingValue = Number(document.reviewForm.rating.value); 
		if (isNaN(ratingValue) || ratingValue < 0 || ratingValue > 5) {
			alert("별점은 0~5점으로 입력해주세요");
			document.reviewForm.rating.select();
			e.preventDefault();
			return;
		}
		
		const content = document.reviewForm.content.value;
		if (content.length > 100){
			alert("내용이 너무 많습니다.");
			document.reviewForm.content.select();
			e.preventDefault();
			return;
		}
		
	}
</script>
</head>

<body 
  class="${burger.brand eq '맥도날드' ? 'mcdonalds' : (burger.brand eq '버거킹' ? 'burgerking' : (burger.brand eq '롯데리아' ? 'lotteria' : ''))}"
>

<!-- ✅ 헤더 -->
<%@ include file="/include/header.jsp" %>

<!-- ✅ 버거 상세 -->
<main class="my-5 py-5">
  <div class="burger-card row align-items-center g-5">
    
    <!-- 왼쪽 이미지 -->
    <div class="col-md-5 text-center burger-image">
		<c:choose>
	    	<c:when test="${fn:startsWith(burger.imagePath, '/')}">
	        	<img 
	            src="${pageContext.request.contextPath}${burger.imagePath}" 
	            class="card-img-top" 
	            alt="${burger.name}">
	    	</c:when>
	   	 	<c:otherwise>
	        	<img 
	            src="${burger.imagePath}" 
	            class="card-img-top" 
	            alt="${burger.name}">
	    	</c:otherwise>
		</c:choose>  
    </div>

    <!-- 오른쪽 정보 -->
    <div class="col-md-7">
      <div class="title-container">
        <img class="burger-logo"
          src="<c:choose>
                 <c:when test='${burger.brand eq "맥도날드"}'>
                   ${pageContext.request.contextPath}/image/mcdonalds_logo.png
                 </c:when>
                 <c:when test='${burger.brand eq "버거킹"}'>
                   ${pageContext.request.contextPath}/image/burgerking_logo.png
                 </c:when>
                 <c:when test='${burger.brand eq "롯데리아"}'>
                   ${pageContext.request.contextPath}/image/lotteria_logo.png
                 </c:when>
                 <c:otherwise>
                   ${pageContext.request.contextPath}/image/default_logo.png
                 </c:otherwise>
               </c:choose>" 
          alt="${burger.brand} 로고">
        <h2 class="burger-title">${burger.name}</h2>
      </div>

      <p class="badge badge-brand">${burger.brand}</p>

      <!-- ✅ 패티 타입 추가 -->
      <p class="patty-type">${burger.pattyType}</p>

      <h4 class="fw-bold mb-4" style="color: var(--main-color);">${burger.price}원</h4>

      <div class="nutrition-card">
        <h5><i class="bi bi-activity"></i>영양 정보</h5>
        <div class="nutrition-list">
          <div class="nutrition-item"><i class="bi bi-fire"></i><span>칼로리:</span> ${burger.details.calories} kcal</div>
          <div class="nutrition-item"><i class="bi bi-droplet-half"></i><span>탄수화물:</span> ${burger.details.carbohydrates} g</div>
          <div class="nutrition-item"><i class="bi bi-basket2-fill"></i><span>단백질:</span> ${burger.details.protein} g</div>
          <div class="nutrition-item"><i class="bi bi-circle-half"></i><span>지방:</span> ${burger.details.fat} g</div>
          <div class="nutrition-item"><i class="bi bi-shield-exclamation"></i><span>나트륨:</span> ${burger.details.sodium} mg</div>
          <div class="nutrition-item"><i class="bi bi-cup-hot"></i><span>당류:</span> ${burger.details.sugar} g</div>
          <div class="nutrition-item" style="grid-column: span 2;">
            <i class="bi bi-exclamation-triangle"></i><span>알레르기 정보:</span> ${burger.details.allergyInfo}
          </div>
        </div>
      </div>
    </div>
  </div>


	<div class="my-5 py-5">
		  <div class="card shadow-sm">
		    <div class="card-body">
		      <h3 class="card-title mb-4 text-center">리뷰 등록</h3>
		      <form action="${pageContext.request.contextPath}/ReviewAddProcess" name="reviewForm" method="post" class="comment-form" enctype="multipart/form-data">
		        <input type="hidden" name="burgerId" value="${burger.id}">
		        <div class="mb-3">
		          <label for="name" class="form-label">닉네임</label>
		          <input type="text" class="form-control" value="버거짱짱맨" readonly="readonly">
		        </div>
		
		        <div class="mb-3">
		          <label for="content" class="form-label">댓글</label>
		          <textarea class="form-control" id="content" name="content" rows="5" placeholder="댓글을 입력하세요"></textarea>
		        </div>
		        
		        <div class="mb-3">          
		          <input type="file" class="form-control" id="image" name="images" value="" multiple="multiple">
		        </div>
		        
		        <div class="mb-3">          
		          <label for="rating" class="form-label">별점</label>
		          <input type="text" class="form-control" id="rating" name="rating" placeholder="별점을 입력하세요(0~5)" required>
		        </div>
		
		        <div class="text-end">
		          <button type="submit" class="btn btn-warning rounded-3" onclick="checkForm(event)">등록</button>
		        </div>
		      </form>
		    </div>
			<div class="my-4">
				<div class="border-0 rounded-0 bg-white shadow-0">
					<div class="review">
					<!-- 리뷰 추가될 영역 -->
					<%
					int burgerId = Integer.parseInt(request.getParameter("id"));
	        		ReviewDAO rvDao = new ReviewDAO();
	        		List<ReviewDTO> recordList = rvDao.getReview(burgerId);
	        		
	        		for(int i = 0; i < recordList.size(); i++){
	        			ReviewDTO record = new ReviewDTO();
	        			record = recordList.get(i);
	        			
	        			Timestamp updatedAt = record.getUpdatedAt();
	        			String content = record.getContent();
	        			String imgPath = record.getImagePath();
					%>
		        		<!-- 프로필 영역 -->
						<div class="card-body px-4 py-4 border-bottom">
							<div class="d-flex align-items-center mb-3">
								<div class="me-3">
									<i class="bi bi-person-circle profileIcon" style="font-size: 30px;"></i>
								</div>
								<div>
									<strong class="d-block">닉네임</strong>
									<small class="text-muted"><%= updatedAt %></small>
								</div>
							</div>
						
							<!-- 본문 영역 -->
							<div class="mb-2">
								<div class="mb-2">
									<img alt="이미지" src="${pageContext.request.contextPath}/image/<%= imgPath %>" style="width:100px; height:100px; display:inline-block; background-color:#fffef8">
								</div>
								<p class="mb-0">
								<%= content %>
								</p>
							</div>		        	
						</div>
		        	<% 
		        		}
		        		
					%>
					</div>
				</div>
			</div>
		  </div>
		
	</div>
	
	
	</div>
</main>

<!-- ✅ 푸터 -->
<%@ include file="/include/footer.jsp" %>

</body>
</html>
