<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>댓글 작성 폼</title>
  <!-- ✅ Bootstrap 5 CSS CDN -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
  
</head>
<body class="bg-light">
	<div class="container mt-5">
	  <div class="card shadow-sm">
	    <div class="card-body">
	      <h3 class="card-title mb-4 text-center">리뷰 등록</h3>
	      <form action="${pageContext.request.contextPath}/ReviewAddProcess" method="post" class="comment-form" enctype="multipart/form-data">
	        
	        <div class="mb-3">
	          <label for="name" class="form-label">닉네임</label>
	          <input type="text" class="form-control" value="버거짱짱맨" readonly="readonly">
	        </div>
	
	        <div class="mb-3">
	          <label for="content" class="form-label">댓글</label>
	          <textarea class="form-control" id="content" name="content" rows="5" placeholder="댓글을 입력하세요" required></textarea>
	        </div>
	        
	        <div class="mb-3">          
	          <input type="file" class="form-control" id="image" name="images" multiple="multiple" required>
	        </div>
	        
	        <div class="mb-3">          
	          <label for="rating" class="form-label">별점</label>
	          <input type="text" class="form-control" id="rating" name="rating" placeholder="별점을 입력하세요(0~5)" required>
	        </div>
	
	        <div class="text-end">
	          <button type="submit" class="btn btn-outline-warning">등록</button>
	        </div>
	      </form>
	    </div>
	  </div>
	</div>
	
	<div class="container my-3">
  		<div class="card shadow-sm border-0">
    		<div class="card-body">
	    		<div class="review">
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
		document.getElementById("btn").addEventListner('click', function() {
			const reviewBody = document.getElementById("card-body");
			const review = document.createElement("div");
			review.className = "review";
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
							<img alt="이	미" src="" style="width:100px; height:100px; display:inline-block; background-color:red">
						</div>
					<p class="mb-0">
					가나다라마바사아자차카타파가나다라마바사아자차카타파가나다라마바사아자차카타파
					</p>
				</div>`;
			reviewBody.prepend(review);	
		})
	</script>
	
	</div>
	
	
</body>
</html>
