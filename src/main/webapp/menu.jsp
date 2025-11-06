<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dao.BurgerDAO" %>
<%@ page import="dao.BurgerSearchDAO" %>
<%@ page import="dto.BurgerDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    request.setCharacterEncoding("UTF-8");
    List<BurgerDTO> burgers = (List<BurgerDTO>) request.getAttribute("burgers");
    if (burgers == null) {
        BurgerDAO dao = new BurgerDAO();
        burgers = dao.getAllBurgers();
        request.setAttribute("burgers", burgers);
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>BurgerHub | 전체 메뉴</title>

    <!-- ✅ Bootstrap & Fonts -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>

<body>
    <%@ include file="/include/header.jsp" %>

    <div class="container mt-5 text-center">

        <!-- ✅ 제목 영역 -->
        <c:choose>
            <c:when test="${not empty keyword}">
                <h2 class="fw-bold mb-2">🔍 검색 결과 메뉴</h2>
                <p class="text-muted mb-4">"${keyword}" 에 대한 검색 결과입니다.</p>
            </c:when>
            <c:otherwise>
                <h2 class="fw-bold mb-2">🍔 전체 메뉴</h2>
                <p class="text-muted mb-4">원하는 버거를 골라보세요!</p>
            </c:otherwise>
        </c:choose>

        <!-- ✅ 필터 버튼 -->
        <div class="d-flex justify-content-center gap-2 mb-5">
            <button class="btn btn-warning active rounded-pill px-4 fw-semibold filter-btn" data-type="all">전체</button>
            <button class="btn btn-outline-warning rounded-pill px-4 fw-semibold filter-btn" data-type="비프">비프</button>
            <button class="btn btn-outline-warning rounded-pill px-4 fw-semibold filter-btn" data-type="치킨">치킨</button>
            <button class="btn btn-outline-warning rounded-pill px-4 fw-semibold filter-btn" data-type="기타">기타</button>
        </div>

        <!-- ✅ 메뉴 목록 -->
        <c:choose>
            <c:when test="${empty burgers}">
                <div class="text-center my-5">
                    <p class="text-muted fs-5">🍔 검색된 버거가 없습니다 😢</p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="row g-4">

                    <c:forEach var="b" items="${burgers}">
                        <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                            <div class="card burger-card shadow-sm position-relative">

                                <!-- ✅ 브랜드 로고 -->
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
                    </c:forEach>

                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%@ include file="/include/footer.jsp" %>
    <script src="${pageContext.request.contextPath}/resources/js/filter.js"></script>
</body>
</html>
