<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글 작성</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ 공통 스타일 -->
  <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resources/css/burgerList.css" rel="stylesheet">
</head>

<body>
  <%@ include file="/include/header.jsp" %>
	<div class="container mt-5">
  <h2 class="text-center mb-4">📋 게시글 목록</h2>

  
	<div class="text-end mb-3">
	  <c:choose>
	    <c:when test="${not empty sessionScope.LOGIN_UID}">
	      <a href="${pageContext.request.contextPath}/board/write" class="btn btn-primary">글쓰기</a>
	    </c:when>
	    <c:otherwise>
	      <button type="button" class="btn btn-secondary" 
	              onclick="alert('로그인 후 이용해주세요.'); location.href='${pageContext.request.contextPath}/user/login.jsp';">
	        글쓰기
	      </button>
	    </c:otherwise>
	  </c:choose>
	</div>
	<div class="border rounded-top bg-light py-2 px-3 mb-0" style="border-bottom: none;">
	  <div class="d-flex justify-content-start align-items-center gap-2">
	    <c:set var="categories" value="${fn:split('전체,공지사항,자유,맥도날드,롯데리아,수제버거', ',')}"/>
	    <c:forEach var="cat" items="${categories}">
	      <c:choose>
	        <c:when test="${cat eq selectedCategory}">
	          <a href="${pageContext.request.contextPath}/board/list?category=${cat}"
	             class="btn btn-sm btn-warning text-dark fw-semibold">${cat}</a>
	        </c:when>
	        <c:otherwise>
	          <a href="${pageContext.request.contextPath}/board/list?category=${cat}"
	             class="btn btn-sm btn-outline-secondary">${cat}</a>
	        </c:otherwise>
	      </c:choose>
	    </c:forEach>
	  </div>
	</div>
  <table class="table table-striped table-hover text-center align-middle">
    <thead class="table-dark">
      <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>카테고리</th>
        <th>작성일</th>
        <th>조회수</th>
      </tr>
    </thead>
    <tbody>
		<c:forEach var="b" items="${boardList}">
		  <tr class="${b.category eq '공지사항' ? 'table-warning fw-semibold' : ''}">
		    <td>${b.boardId}</td>
		    <td class="text-start">
		      <a href="${pageContext.request.contextPath}/board/view?id=${b.boardId}"
		         class="text-decoration-none text-dark">${b.title}</a>
		    </td>
		    <td>${b.writerNickname}</td>
		    <td>${b.category}</td>
		    <td>${b.createdAt}</td>
		    <td>${b.viewCount}</td>
		  </tr>
		</c:forEach>
      <c:if test="${empty boardList}">
        <tr>
          <td colspan="6" class="text-center text-muted">등록된 게시글이 없습니다.</td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
