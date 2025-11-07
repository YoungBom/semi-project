<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="col-md-3 col-sm-6 mb-4">
    <div class="card burger-card shadow-sm position-relative">

        <!-- ✅ 책갈피 브랜드 로고 -->
        <div class="brand-flag">
            <img src="${pageContext.request.contextPath}/img/${b.brand eq '맥도날드' ? 'mcdonalds_logo.png' 
                    : (b.brand eq '버거킹' ? 'burgerking_logo.png' 
                    : (b.brand eq '롯데리아' ? 'lotteria_logo.png' : 'default_logo.png'))}"
                 alt="${b.brand} 로고">
        </div>

        <!-- ✅ 상세 페이지 링크 -->
        <a href="${pageContext.request.contextPath}/burger/details?id=${b.id}" class="text-decoration-none text-dark">

            <!-- ✅ 버거 이미지 -->
            <c:choose>
                <c:when test="${not empty b.imagePath and fn:startsWith(b.imagePath, '/')}">
                    <img src="${pageContext.request.contextPath}${b.imagePath}" 
                         class="card-img-top" alt="${b.name}" 
                         style="height:200px; object-fit:contain;">
                </c:when>

                <c:when test="${not empty b.imagePath}">
                    <img src="${b.imagePath}" 
                         class="card-img-top" alt="${b.name}" 
                         style="height:200px; object-fit:contain;">
                </c:when>

                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/image/noimage.png" 
                         class="card-img-top" alt="이미지 없음" 
                         style="height:200px; object-fit:contain;">
                </c:otherwise>
            </c:choose>

            <!-- ✅ 카드 본문 -->
            <div class="card-body text-start">
                <h5 class="card-title mt-2">${b.name}</h5>
                <span class="badge patty-badge ${b.pattyType}">${b.pattyType}</span>

                <!-- ✅ 가격 & 별점 -->
                <div class="d-flex justify-content-between align-items-center mt-3">
                    <span class="price fw-bold text-warning">${b.price}원</span>
                    
                    <span class="rating text-warning">
                        <fmt:parseNumber value="${b.avgRating}" integerOnly="true" var="starFull" />
                        <c:forEach begin="1" end="${starFull}" var="i">★</c:forEach>
                        <c:forEach begin="1" end="${5 - starFull}" var="i">☆</c:forEach>
                        <small>(<fmt:formatNumber value="${b.avgRating}" maxFractionDigits="1" />)</small>
                    </span>
                </div>
            </div>
            <!-- ✅ card-body 끝 -->
        </a>
    </div>
</div>