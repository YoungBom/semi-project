<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원 관리</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ 공통 스타일 -->
  <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resources/css/userManagement.css" rel="stylesheet">

</head>

<body>
  <%@ include file="/include/header.jsp" %>
  <div class="user-page">
	  <div class="page-header mb-5">
	      <h2>회원 관리</h2>
	  </div>
	  
	  <div class="container user-container mb-5">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="fw-bold">회원 목록</h5>
        <div class="input-group w-25">
          <input type="text" class="form-control form-control-sm" placeholder="회원 검색...">
          <button class="btn btn-outline-secondary btn-sm"><i class="bi bi-search"></i></button>
        </div>
      </div>
	  
	  <div class="table-responsive">
        <table class="table table-hover align-middle text-center shadow-sm">
          <thead class="table-warning">
            <tr>
              <th scope="col">회원번호</th>
              <th scope="col">이름</th>
              <th scope="col">닉네임</th>
              <th scope="col">이메일</th>
              <th scope="col">가입일</th>
              <th scope="col">상태</th> <!-- 유저상태면 'user' 관리자면 'admin' --> 
              <th scope="col">관리</th> <!-- user의 경우 삭제가능하도록 -->
            </tr>
          </thead>
          <tbody>
            <c:forEach var="user" items="${userList}">
              <tr>
                <td>${user.userId}</td>
                <td>${user.name}</td>
                <td>${user.nickname}</td>
                <td>${user.email}</td>
                <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" /></td>
                <td>
                  <div class="d-flex justify-content-center align-items-center gap-2">
                    <select id="roleSelect-${user.id}" class="form-select form-select-sm w-auto userRole" disabled>
                      <option value="user" >user</option>
                      <option value="admin" >admin</option>
                    </select>
                    <a href="#" id="roleController-${user.id}" class="btn btn-outline-success btn-sm disabled" aria-disabled="true">
   					<i class="bi bi-check-circle"></i>
					</a>
                  </div>
                </td>
                <td>
                  <a class="btn btn-outline-primary btn-sm" onclick="changeUser(${user.id})"><i class="bi bi-pencil">수정</i></a>
                  <a href="#" class="btn btn-outline-danger btn-sm"><i class="bi bi-pencil">삭제</i></a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  </div>

  <%@ include file="/include/footer.jsp" %>
  
  <script src="${pageContext.request.contextPath}/resources/js/user.js"></script>
</body>
</html>
