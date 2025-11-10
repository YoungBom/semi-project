<%@page import="dto.BurgerDTO"%>
<%@page import="dao.ReviewDAO"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>
<%@page import="dto.ReviewDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    // JSP가 단독 실행될 때 burger가 없으면 메인으로 이동 (burgerDetails.jsp를 실행하면 main으로 이동하게 바꿈)
    Object burgerObj = request.getAttribute("burger");
    if (burgerObj == null) {
        response.sendRedirect(request.getContextPath() + "/main");
        return;
    }
%>


<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${burger.name} - BurgerHub 🍔</title>

<!-- ✅ Bootstrap & Fonts -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/details.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/review.css">


<!-- JS 연결 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


</head>
<body 
  class="${burger.brand eq '맥도날드' ? 'mcdonalds' : (burger.brand eq '버거킹' ? 'burgerking' : (burger.brand eq '롯데리아' ? 'lotteria' : ''))}"
  data-is-logged-in="${not empty sessionScope.LOGIN_UID}" 
  data-ctx="${pageContext.request.contextPath}"
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
                   ${pageContext.request.contextPath}/img/mcdonalds_logo.png
                 </c:when>
                 <c:when test='${burger.brand eq "버거킹"}'>
                   ${pageContext.request.contextPath}/img/burgerking_logo.png
                 </c:when>
                 <c:when test='${burger.brand eq "롯데리아"}'>
                   ${pageContext.request.contextPath}/img/lotteria_logo.png
                 </c:when>
                 <c:otherwise>
                   ${pageContext.request.contextPath}/img/default_logo.png
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
      <div class="text-end mb-3">
        <button type="button"
        class="btn btn-warning rounded-3"
        id="openReviewBtn"
        data-bs-toggle="modal"
        data-bs-target="#reviewModal"
        data-is-logged-in="${not empty sessionScope.LOGIN_UID}"
        data-ctx="${pageContext.request.contextPath}">
        리뷰 등록
        </button>
      </div>
		  
      <div class="card-body">
      <h3 class="card-title mb-4 text-center">리뷰 목록</h3>
        <div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-lg modal-dialog-centered">
		    <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="reviewModalLabel">리뷰 등록</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
              </div>

		      <div class="modal-body">
                <form action="${pageContext.request.contextPath}/review/add?"
		              method="post"
		              enctype="multipart/form-data"
		              class="comment-form"
		              name="reviewForm">
			          <input type="hidden" name="burgerId" value="${burger.id}">
			
			          <div class="mb-3">
			            <label class="form-label">닉네임</label>
			            <input type="text" class="form-control" value="${nickname}" readonly>
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
					  <button type="button"
					    class="btn btn-outline-secondary btn-sm rounded-pill px-3 py-1"
					    id="oldImageButtonContainer"
					    data-bs-toggle="button"
					    style="display:none;"
					    onclick="checkUploadNewImage()">
					    기존 이미지 등록
					  </button>
					  </div>
			
			          <div class="mb-3">
			            <label for="rating" class="form-label">별점</label>
			            <input type="text" class="form-control" id="rating" name="rating" placeholder="별점을 입력하세요(0~5)" required>
			          </div>
			
			          <div class="text-end">
			            <button type="submit" class="btn btn-warning rounded-3" onclick="return checkForm(event)">등록</button>
			          </div>
		        </form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="my-4">
        <div class="border-0 rounded-0 bg-white shadow-0">
          <div class="review">
              <!-- 리뷰 리스트 반복 출력 -->
            <c:forEach var="record" items="${reviewList}">
              <div class="card-body px-4 py-4 border-bottom">
            
                <!-- 프로필 영역 -->
                <div class="d-flex align-items-center mb-3 position-relative">
                  <div class="me-3">
                    <i class="bi bi-person-circle profileIcon" style="font-size: 30px;"></i>
                  </div>
            
                  <div>
                    <strong class="d-block">${record.nickname}</strong>
            
                    <!-- 날짜 + 별점 -->
                    <div class="d-flex align-items-center gap-2">
                      <small class="text-muted">
                        <c:choose>
                          <c:when test="${record.updatedAt ne record.createdAt}">
                            <fmt:formatDate value="${record.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>  
                            <span class="text-secondary">(수정됨)</span>                    	
                          </c:when>
                          <c:otherwise>
                            <fmt:formatDate value="${record.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                          </c:otherwise>
                        </c:choose>
                      </small>
                      <!-- ⭐ 별점 -->
                      <div class="rating text-warning" style="font-size: 15px;">
                        <c:forEach begin="1" end="${record.rating}" var="i">★</c:forEach>
                        <c:forEach begin="1" end="${5 - record.rating}" var="i">☆</c:forEach>
                      </div>
                    </div>
                    <!-- ✅ 본인 리뷰일 때만 수정/삭제 버튼 노출 -->
                    <c:if test="${sessionScope.LOGIN_UID eq record.userId}">
                      <!-- 수정 버튼 -->
                      <a href="#"
                         class="btn btn-outline-danger btn-sm position-absolute top-0 end-0 my-1 me-0"
                         onclick="openUpdateModal(event, ${record.id}, '${fn:escapeXml(record.content)}', ${record.rating}, '${burger.id}')">
                         <i class="bi bi-pencil"></i> 수정
                      </a> 
                
                      <!-- 삭제 버튼 -->
                      <a href="${pageContext.request.contextPath}/review/delete?burgerId=${burger.id}&reviewId=${record.id}"
                         class="btn btn-outline-danger btn-sm position-absolute top-0 end-0 my-1 me-5"
                         onclick="return confirm('이 리뷰를 삭제하시겠습니까?');">
                         <i class="bi bi-trash"></i> 삭제
                      </a>
                    </c:if>
                  </div>
                </div>
            
                <!-- 리뷰 내용 -->
                <p class="mb-2">${record.content}</p>
            
                <!-- 리뷰 이미지 (있을 때만) -->
                <c:forEach var="img" items="${record.imageList}">
                  <c:if test="${not empty fn:trim(img)}">
                    <img src="${pageContext.request.contextPath}/image/${img}" 
                         alt="리뷰 이미지" class="review-img">
                  </c:if>
                </c:forEach>
              </div>
            </c:forEach>

          </div>
        </div>
      </div>
    </div>
  </div>
</main>

<!-- ✅ 푸터 -->
<%@ include file="/include/footer.jsp" %>

<script src="${pageContext.request.contextPath}/resources/js/details.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/review.js"></script>


</body>
</html>
