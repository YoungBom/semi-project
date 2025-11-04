<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>버거 관리 페이지</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
  <div class="container py-4">
    <h2 class="text-center mb-4 fw-bold">버거 관리 페이지</h2>

    <!-- 상단 버튼 영역 -->
    <div class="d-flex justify-content-between mb-3">
      <div>
        <a href="${pageContext.request.contextPath}/burger/add" class="btn btn-primary">+ 새 버거 등록</a>
        
      </div>
      <form class="d-flex" action="searchBurger" method="get">
        <input type="text" name="keyword" class="form-control me-2" placeholder="버거 이름 검색">
        <button class="btn btn-outline-secondary" type="submit">검색</button>
      </form>
    </div>

    <!-- 버거 목록 테이블 -->
    <table class="table table-hover align-middle text-center bg-white shadow-sm">
      <thead class="table-warning">
        <tr>
          <th>번호</th>
          <th>버거 이름</th>
          <th>가격</th>
          <th>브랜드</th>
          <th>관리</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="burger" items="${burgerList}">
          <tr>
            <td>${burger.id}</td>
            <td>${burger.name}</td>
            <td>${burger.price}원</td>
            <td>${burger.brand}</td>
            <td>
              <a href="${pageContext.request.contextPath}/burger/edit?id=${burger.id}" class="btn btn-sm btn-warning">수정</a>
              <a href="deleteBurger?id=${burger.id}" class="btn btn-sm btn-danger"
                 onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <!-- 버거가 없을 때 -->
    <c:if test="${empty burgerList}">
      <div class="text-center text-muted mt-5">
        등록된 버거가 없습니다. <a href="addBurger.jsp" class="text-decoration-none">지금 추가하기</a>
      </div>
    </c:if>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
