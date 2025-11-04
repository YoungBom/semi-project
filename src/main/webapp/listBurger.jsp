<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>부거 관리</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

  <style>
    body {
      background-color: #f8f9fa;
      font-family: 'Poppins', sans-serif;
      color: #444;
    }

    .page-header {
      background: linear-gradient(135deg, #ff922b, #ffa94d);
      color: #fff;
      padding: 2rem 0;
      text-align: center;
      border-radius: 0 0 18px 18px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }

    .page-header h2 {
      font-weight: 600;
      margin-bottom: 0.3rem;
    }

    .table-container {
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
      padding: 1.8rem;
    }

    th {
      background-color: #fff7e6 !important;
      color: #5f3e00;
      font-weight: 600;
    }

    td {
      vertical-align: middle;
      color: #555;
    }

    .btn-primary {
      background-color: #ff922b;
      border: none;
    }
    .btn-primary:hover {
      background-color: #f08c00;
    }

    .btn-warning {
      background-color: #ffd43b;
      color: #5c4d00;
      border: none;
    }
    .btn-warning:hover {
      background-color: #fcc419;
      color: #3f3800;
    }

    .btn-danger {
      background-color: #fa5252;
      border: none;
    }
    .btn-danger:hover {
      background-color: #e03131;
    }

    .text-muted a {
      color: #ff922b;
    }
    .text-muted a:hover {
      text-decoration: underline;
    }
  </style>
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <!-- ✅ 상단 헤더 -->
  <div class="page-header mb-5">
    <h2>버거 관리</h2>
  </div>

  <!-- ✅ 메인 컨텐츠 -->
  <div class="container mb-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h4 class="fw-semibold text-secondary">전체 버거 목록</h4>
      <a href="${pageContext.request.contextPath}/burger/add" class="btn btn-primary shadow-sm">
        <i class="bi bi-plus-lg"></i> 새 버거 등록
      </a>
    </div>

    <div class="table-container">
      <table class="table table-hover align-middle text-center">
        <thead>
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
              <td class="text-muted">${burger.id}</td>
              <td class="fw-semibold">${burger.name}</td>
              <td class="text-warning fw-bold">${burger.price}원</td>
              <td><span class="fw-semibold">${burger.brand}</span></td>
              <td>
                <a href="${pageContext.request.contextPath}/burger/edit?id=${burger.id}" 
                   class="btn btn-sm btn-warning me-2">
                   <i class="bi bi-pencil-square"></i> 수정
                </a>
                <a href="${pageContext.request.contextPath}/burger/delete?id=${burger.id}" 
                   class="btn btn-sm btn-danger"
                   onclick="return confirm('정말 삭제하시겠습니까?')">
                   <i class="bi bi-trash"></i> 삭제
                </a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <!-- ✅ 버거가 없을 때 -->
      <c:if test="${empty burgerList}">
        <div class="text-center text-muted py-5">
          <i class="bi bi-emoji-neutral fs-2 d-block mb-2"></i>
          등록된 버거가 없습니다.
          <a href="${pageContext.request.contextPath}/burger/add" class="fw-semibold">지금 추가하기</a>
        </div>
      </c:if>
    </div>
  </div>

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
