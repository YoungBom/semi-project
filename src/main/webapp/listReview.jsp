<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
<body data-is-logged-in="${not empty sessionScope.LOGIN_UID}" 
  	  data-ctx="${pageContext.request.contextPath}">
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
								<img src="${pageContext.request.contextPath}/image/${img}" class="review-img" alt="리뷰 이미지"
								style="cursor:pointer;"
         				 		onclick="showImageModal('${pageContext.request.contextPath}/image/${img}')">
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
							<div class="d-flex flex-row-reverse">
								<div>
									<a href="/semi-project/review/delete?burgerId=${review.burgerId}&reviewId=${review.id}&redirect=${pageContext.request.requestURI}" class="btn btn-outline-danger btn-sm my-1 ">
										<i class="bi bi-trash"></i> 삭제
									</a>
								</div>							
								<div>
									<a href="javascript:void(0)" class="btn btn-outline-success btn-sm my-1 me-1" onclick="openUpdateModal(event, ${review.id}, ${review.burgerId}, '${fn:escapeXml(review.content)}', ${review.rating}, '${review.imageList}')">
										<i class="bi bi-pencil"></i> 수정
									</a>
								</div>
								<div>
									<a href="/semi-project/burger/details?id=${review.burgerId}" class="btn btn-outline-secondary btn-sm my-1 me-1">
										<i class="bi bi-eye"></i> 보기
									</a>
								</div>
							</div>
						</div>
						
			    	</div>
			    </div>
			</c:forEach>
		  </div>
	</c:otherwise>
  </c:choose>
  <!-- 수정 모달 창 -->
  <div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="reviewModalLabel">리뷰 수정</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
              </div>

        <div class="modal-body">
          <form action="${pageContext.request.contextPath}/review/update?burgerId=${review.burgerId}&reviewId=${review.id}&redirect=${pageContext.request.requestURI}"
            method="post"
            enctype="multipart/form-data"
            class="comment-form">
           <div class="mb-3">
             <label class="form-label">닉네임</label>
             <input type="text" class="form-control" value="${LOGIN_NICKNAME}" readonly>
           </div>

	       <div class="mb-3">
	         <label for="content" class="form-label">댓글</label>
	         <textarea class="form-control" id="content" name="content" rows="5" placeholder="댓글을 입력하세요"></textarea>
	       </div>

	       <div class="mb-3">
	         <label for="image" class="form-label">이미지 업로드</label>
	         <input type="file" class="form-control" id="image" name="images" multiple>
	       </div>
       
		   <div class="mb-3 text-end mt-1">
		     <input type="hidden" name="imageCheck" id="imageCheck" value="false">
		     <input type="text" id="oldImageName" class="form-control mt-2" readonly style="display:none;">
		     <button type="button"
		       class="btn btn-outline-secondary btn-sm rounded-pill px-3 py-1"
		       id="oldImageButtonContainer"
		       style="display:none;"
		       onclick="checkImg()">
		       기존 이미지 등록
		    </button>
		   </div>

	       <div class="mb-3">
	         <label for="rating" class="form-label">별점</label>
	         <input type="text" class="form-control" id="rating" name="rating" placeholder="별점을 입력하세요(0~5)" required>
	       </div>

	       <div class="text-end">
	         <button type="submit" class="btn btn-warning rounded-3" onclick="return checkForm(event)">수정 완료</button>
	       </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <!-- 이미지 클릭시 모달창으로 띄우기 -->
  <div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
      <div class="modal-content bg-transparent border-0 shadow-none">
        <button type="button" class="btn-close btn-close-white position-absolute top-0 end-0 m-3"
                data-bs-dismiss="modal" aria-label="Close"></button>
        <img id="modalImage" src="" alt="리뷰 이미지" class="img-fluid rounded shadow">
      </div>
    </div>
  </div>
</main>

<%@ include file="/include/footer.jsp" %>

<script src="${pageContext.request.contextPath}/resources/js/reviewList.js"></script>

</body>
</html>