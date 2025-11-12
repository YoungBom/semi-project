<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dao.BurgerDAO" %>
<%@ page import="dao.BurgerSearchDAO" %>
<%@ page import="dto.BurgerDTO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/menu.css">
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
            <c:when test="${empty burgerList}">
                <div class="text-center my-5">
                    <p class="text-muted fs-5">🍔 검색된 버거가 없습니다 😢</p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="row g-4">
                    <c:forEach var="b" items="${burgerList}">
                    
        				<!-- 버거 카드 -->            
						<%@ include file="/include/burgerCard.jsp" %>
                        
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%@ include file="/include/footer.jsp" %>
    <script src="${pageContext.request.contextPath}/resources/js/filter.js"></script>
</body>
</html>
