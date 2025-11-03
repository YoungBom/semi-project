<%@page import="java.util.List"%>
<%@page import="dto.BurgerDTO"%>
<%@page import="dao.BurgerSearchDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>BurgerDAO 테스트</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="p-4">

<h2 class="mb-4 text-center">🍔 BurgerDAO 테스트 페이지</h2>

<c:choose>
  <c:when test="${empty burgers}">
    <div class="alert alert-warning text-center mt-5">
      🍔 검색된 버거가 없습니다 😢
    </div>
  </c:when>
  <c:otherwise>
    <table class="table table-bordered table-hover text-center align-middle mt-4">
      <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>브랜드</th>
          <th>버거 이름</th>
          <th>패티 종류</th>
          <th>가격</th>
          <th>이미지 경로</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="b" items="${burgers}">
          <tr>
            <td>${b.id}</td>
            <td>${b.brand}</td>
            <td>${b.name}</td>
            <td>${b.pattyType}</td>
            <td>${b.price}</td>
            <td>
              <c:choose>
                <c:when test="${not empty b.imagePath}">
                  ${b.imagePath}
                </c:when>
                <c:otherwise>-</c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>


</body>
</html>
