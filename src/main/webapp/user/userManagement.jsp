<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	  <div>
      <h4 class="fw-semibold container mb-5">회원 목록</h4>	  
	  </div>
	  <div class="container user-container mb-5 position-relative">
        <form action="${pageContext.request.contextPath}/user/management" method="get" class="d-flex justify-content-between align-items-center position-relative mb-3">
            <div class="input-group w-25 position-absolute top-0 end-0">
              <input type="text" name="keyword" class="form-control form-control-sm" placeholder="회원 검색...">
              <button type="submit" class="btn btn-outline-secondary btn-sm">
              <i class="bi bi-search"></i>
              </button>
            </div>
        </form>
	  
	  <div class="table-responsive">
        <table class="table table-hover align-middle text-center shadow-sm ">
          <thead class="table-warning">
            <tr>
              <th scope="col">아이디</th>
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
              <tr id="selectUser-${user.id}">
                <td>${user.userId}</td>
                <td>${user.name}</td>
                <td>${user.nickname}</td>
                <td>${user.email}</td>
                <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" /></td>
                
                <td>
                  <form action="${pageContext.request.contextPath}/user/authorize"
                        method="post"
                        class="d-flex justify-content-center align-items-center gap-2 mb-0">
                
                    <input type="hidden" name="id" value="${user.id}">
                
                    <select id="roleSelect-${user.id}"
                            name="role"
                            class="form-select form-select-sm w-auto userRole"
                            data-old="${user.role.toUpperCase()}"
                            disabled>
                        <c:choose>
                            <c:when test="${user.role eq 'ADMIN'}">
                                <option value="USER">user</option>
                                <option value="ADMIN" selected>admin</option>
                            </c:when>
                            <c:otherwise>
                                <option value="USER" selected>user</option>
                                <option value="ADMIN">admin</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                
                    <button type="submit"
                            id="roleController-${user.id}"
                            class="btn btn-outline-success btn-sm disabled roleController"
                            aria-disabled="true"
                            onclick="return checkPosition('${user.id}')">
                        <i class="bi bi-check-circle"></i>
                    </button>
                  </form>
                </td>

                
                <td>
                  <a class="btn btn-outline-primary btn-sm" onclick="changeUser(${user.id})"><i class="bi bi-pencil">수정</i></a>
                  <a href="${pageContext.request.contextPath}/user/deletefromadmin?userId=${user.userId}" class="btn btn-outline-danger btn-sm"><i class="bi bi-pencil">삭제</i></a>
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
