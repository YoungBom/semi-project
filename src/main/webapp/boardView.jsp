<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
  
	<div class="container mt-5" style="max-width:800px;">
	  <h2 class="mb-4">${board.title}</h2>
	
	  <div class="mb-2 text-muted">
	    작성자: ${board.writerNickname} |
	    카테고리: ${board.category} |
	    작성일: ${board.createdAt} |
	    조회수: ${board.viewCount}
	  </div>
	
	  <div class="border p-4 bg-white rounded-3" style="min-height:200px;">
	    <pre style="white-space:pre-wrap;">${board.content}</pre>
	  </div>
	
	  <div class="mt-4 text-end">
		<c:if test="${sessionScope.LOGIN_UID eq board.writerId}">
		  <a href="<%=request.getContextPath()%>/board/edit?id=${board.boardId}" class="btn btn-warning">수정</a>
		  <a href="<%=request.getContextPath()%>/board/delete?id=${board.boardId}"
		      class="btn btn-danger"
		      onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
		</c:if>
	    <a href="<%=request.getContextPath()%>/board/list" class="btn btn-secondary">목록으로</a>
	  </div>
	</div>
	
	

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
