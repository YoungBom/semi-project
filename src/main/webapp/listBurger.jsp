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
  <link href="${pageContext.request.contextPath}/resources/css/burgerList.css" rel="stylesheet">
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <!-- ✅ 스코프 시작 -->
  <div class="burger-page burger-list-page">

    <div class="page-header mb-5">
      <h2>버거 관리</h2>
    </div>

    <div class="container mb-5">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-semibold text-secondary">전체 버거 목록</h4>
		<a href="${pageContext.request.contextPath}/burger/add"
		   class="add-burger-btn shadow-sm px-3">
		  <i class="bi bi-plus-lg"></i> 새 버거 등록
		</a>
      </div>

      <div class="table-container">
        <table class="table table-hover align-middle text-center">
          <thead>
            <tr>
              <th style="width:10%;">번호</th>
              <th style="width:30%;">버거 이름</th>
              <th style="width:10%;">가격</th>
              <th style="width:20%;">브랜드</th>
              <th style="width:30%;">관리</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="burger" items="${burgerList}">
              <tr>
                <td class="text-muted">${burger.id}</td>
                <td class="fw-semibold">${burger.name}</td>
                <td class="text-muted fw-semibold small">${burger.price}원</td>
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
				              class="btn btn-sm ${burger.newBurger ? 'new-btn' : 'new-btn-outline'}">
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
	      const btn = form.querySelector("button");

	      // 스타일 토글
	      const isNew = btn.classList.contains("new-btn");

	      if (isNew) {
	        btn.classList.remove("new-btn");
	        btn.classList.add("new-btn-outline");
	        btn.textContent = "NEW 표시";     // 🔥 글자도 변경
	      } else {
	        btn.classList.remove("new-btn-outline");
	        btn.classList.add("new-btn");
	        btn.textContent = "NEW 해제";     // 🔥 글자도 변경
	      }
	    }
	  });

	  return false;
	}
  </script>

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
