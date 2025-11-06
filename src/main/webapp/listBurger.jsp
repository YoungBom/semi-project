<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>버거 관리</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ 공통 스타일 -->
  <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

  <style>
    /* ✅ 스코프 분리 */
    .burger-list-page {
      background-color: #f9fafb;
      font-family: 'Poppins', sans-serif;
      color: #444;
      min-height: 100vh;
      padding-top: 40px;
    }

    /* ✅ 페이지 전용 스타일 (스코프 내 한정) */
    .burger-list-page .page-header {
      background: linear-gradient(135deg, #ff922b, #ffa94d);
      color: #fff;
      padding: 2.5rem 0;
      text-align: center;
      border-radius: 0 0 20px 20px;
      box-shadow: 0 3px 10px rgba(0,0,0,0.08);
      position: relative;
      overflow: hidden;
    }
    .burger-list-page .page-header::after {
      content: "";
      position: absolute;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(255,255,255,0.1);
      mix-blend-mode: overlay;
    }
    .burger-list-page .page-header h2 {
      font-weight: 700;
      letter-spacing: -0.5px;
    }

    .burger-list-page .btn-primary {
      background-color: #ff922b;
      border: none;
      transition: all 0.25s ease-in-out;
    }
    .burger-list-page .btn-primary:hover {
      background-color: #f08c00;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(255,146,43,0.3);
    }

    .burger-list-page .btn-warning {
      background-color: #ffd43b;
      color: #5c4d00;
      border: none;
      transition: all 0.25s;
    }
    .burger-list-page .btn-warning:hover {
      background-color: #fcbf00;
      transform: scale(1.03);
    }

    .burger-list-page .btn-danger {
      background-color: #fa5252;
      border: none;
      transition: all 0.25s;
    }
    .burger-list-page .btn-danger:hover {
      background-color: #e03131;
      transform: scale(1.03);
    }

    .burger-list-page .table-container {
      background: #fff;
      border-radius: 14px;
      box-shadow: 0 4px 16px rgba(0,0,0,0.05);
      padding: 2rem;
      transition: all 0.3s;
    }

    .burger-list-page table {
      border-collapse: separate;
      border-spacing: 0 10px;
    }

    .burger-list-page th {
      background-color: #fff7e6 !important;
      color: #5f3e00;
      font-weight: 600;
      text-transform: uppercase;
      font-size: 0.9rem;
      border: none;
    }

    .burger-list-page td {
      vertical-align: middle;
      background: #fff;
      border-top: 1px solid #f1f3f5;
      border-bottom: 1px solid #f1f3f5;
      border-radius: 6px;
      box-shadow: 0 1px 2px rgba(0,0,0,0.03);
      font-size: 0.95rem;
      white-space: nowrap;
    }

    .burger-list-page tr:hover td {
      background-color: #fff8ef;
      transition: 0.2s;
    }

    .burger-list-page .brand-badge {
      background: #ffe8cc;
      color: #b86b00;
      font-weight: 600;
      border-radius: 8px;
      padding: 0.3rem 0.7rem;
      font-size: 0.85rem;
    }

    .burger-list-page .text-muted a {
      color: #ff922b;
      text-decoration: none;
      font-weight: 600;
    }
    .burger-list-page .text-muted a:hover {
      text-decoration: underline;
    }

    /* ✅ 없을 때 메시지 */
    .burger-list-page .empty-message {
      color: #777;
      padding: 4rem 0;
    }
    .burger-list-page .empty-message i {
      color: #ff922b;
    }

    /* ✅ 버튼 정렬 개선 */
    .burger-list-page .table-container td form {
      display: inline-block;
      margin: 0;
      padding: 0;
      vertical-align: middle;
    }
  </style>
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <!-- ✅ 스코프 시작 -->
  <div class="burger-list-page">

    <div class="page-header mb-5">
      <h2>버거 관리</h2>
    </div>

    <div class="container mb-5">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-semibold text-secondary">전체 버거 목록</h4>
        <a href="${pageContext.request.contextPath}/burger/add" class="btn btn-primary shadow-sm px-3">
          <i class="bi bi-plus-lg"></i> 새 버거 등록
        </a>
      </div>

      <div class="table-container">
        <table class="table table-hover align-middle text-center">
          <thead>
            <tr>
              <th style="width:10%;">번호</th>
              <th style="width:30%;">버거 이름</th>
              <th style="width:20%;">가격</th>
              <th style="width:20%;">브랜드</th>
              <th style="width:20%;">관리</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="burger" items="${burgerList}">
              <tr>
                <td class="text-muted">${burger.id}</td>
                <td class="fw-semibold">${burger.name}</td>
                <td class="text-warning fw-bold">${burger.price}원</td>
                <td><span class="brand-badge">${burger.brand}</span></td>
                <td>
                  <a href="${pageContext.request.contextPath}/burger/edit?id=${burger.id}" 
                     class="btn btn-sm btn-warning me-2">
                     <i class="bi bi-pencil-square"></i> 수정
                  </a>
                  <a href="${pageContext.request.contextPath}/burger/delete?id=${burger.id}" 
                     class="btn btn-sm btn-danger me-2"
                     onclick="return confirm('정말 삭제하시겠습니까?')">
                     <i class="bi bi-trash"></i> 삭제
                  </a>
                  <form action="${pageContext.request.contextPath}/burger/list" 
                        onsubmit="return toggleNew(event, ${burger.id}, this)">
                    <button type="submit" 
                            class="btn btn-sm ${burger.newBurger ? 'btn-primary' : 'btn-outline-primary'}">
                      ${burger.newBurger ? 'NEW 해제' : 'NEW 표시'}
                    </button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>

        <c:if test="${empty burgerList}">
          <div class="text-center empty-message">
            <i class="bi bi-emoji-neutral fs-1 d-block mb-2"></i>
            <p>등록된 버거가 없습니다.<br>
            <a href="${pageContext.request.contextPath}/burger/add">지금 추가하기</a></p>
          </div>
        </c:if>
      </div>
    </div>
  </div>
  <!-- ✅ 스코프 종료 -->

  <script>
  function toggleNew(e, id, form) {
    e.preventDefault();
    fetch(form.action, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: "id=" + id
    }).then(r => {
      if (r.ok) {
        const b = form.querySelector("button");
        b.classList.toggle("btn-primary");
        b.classList.toggle("btn-outline-primary");
      }
    });
    return false;
  }
  </script>

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
