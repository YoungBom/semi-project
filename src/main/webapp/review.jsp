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
  
  <script type="text/javascript">
	
  </script>
</head>
<body class="bg-light">
	<div class="container mt-5">
	  <div class="card shadow-sm">
	    <div class="card-body">
	      <h3 class="card-title mb-4 text-center">리뷰 남기기</h3>
	      <form action="${pageContext.request.contextPath}/ReviewAddProcess" method="post" class="comment-form" enctype="multipart/form-data">
	        
	        <div class="mb-3">
	          <label for="name" class="form-label">닉네임</label>
	          <input type="text" class="form-control" id="name" name="name" placeholder="이름을 입력하세요" required>
	        </div>
	
	        <div class="mb-3">
	          <label for="content" class="form-label">댓글</label>
	          <textarea class="form-control" id="content" name="content" rows="5" placeholder="댓글을 입력하세요" required></textarea>
	        </div>
	        
	        <div class="mb-3">          
	          <input type="file" class="form-control" id="image" name="image" required>
	        </div>
	        
	        <div class="mb-3">          
	          <label for="rating" class="form-label">별점</label>
	          <input type="text" class="form-control" id="rating" name="rating" placeholder="별점을 입력하세요(0~5)" required>
	        </div>
	
	        <div class="text-end">
	          <button type="submit" class="btn btn-success" onclick="">등록</button>
	        </div>
	      </form>
	    </div>
	  </div>
	</div>
	
	<%@ include file="reviewList.jsp" %>
	</div>
	
	
</body>
</html>
