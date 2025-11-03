<%@page import="dto.ReviewDTO"%>
<%@page import="java.util.List"%>
<%@page import="dao.ReviewDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<!-- ✅ Bootstrap 5 CSS CDN -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
<body>
	<button type="button" class="btn btn-outline-warning">등록</button>
	<div class="container my-3">
  		<div class="card shadow-sm border-0">
    		<div class="review">
	    		<div class="card-body">
				      <!-- 프로필 영역 -->
				      <div class="d-flex align-items-center mb-3">
				        <div class="me-3">
				          <img src="https://via.placeholder.com/50" alt="사용자이미지" class="rounded-circle border" width="50" height="50">
				        </div>
				        <div>
				          <strong class="d-block">닉네임</strong>
				          <small class="text-muted">2025.11.01 3시 30분</small>
				        </div>
				      </div>
			
				      <!-- 본문 영역 -->
				      <div class="mb-2">
				        <div class="mb-2">
				         	<img alt="이미" src="" style="width:100px; height:100px; display:inline-block; background-color:red">
				        </div>
				        <p class="mb-0">
				        	가나다라마바사아자차카타파가나다라마바사아자차카타파가나다라마바사아자차카타파
				        </p>
	    			</div>
				  </div>
	  		</div>
	  	</div>
	</div>
	
	<script type="text/javascript">
			document.querySelector(".btn").addEventListener('click', function() {
			const reviewBody = document.querySelector(".review");
			const review = document.createElement("div");
			review.className = "card-body";
			review.innerHTML = `<!-- 프로필 영역 -->
				<div class="d-flex align-items-center mb-3">
					<div class="me-3">
						<img src="https://via.placeholder.com/50" alt="사용자이미지" class="rounded-circle border" width="50" height="50">
					</div>
					<div>
						<strong class="d-block">닉네임</strong>
						<small class="text-muted">2025.11.01 3시 30분</small>
					</div>
					
					<!-- 본문 영역 -->
					<div class="mb-2">
						<div class="mb-2">
							<img alt="이미지" src="" style="width:100px; height:100px; display:inline-block; background-color:red">
						</div>
						<p class="mb-0">
						가나다라마바사아자차카타파가나다라마바사아자차카타파가나다라마바사아자차카타파
						</p>
					</div>
				</div>`;
			reviewBody.appendChild(review);
		})
	</script>

</body>
</html>