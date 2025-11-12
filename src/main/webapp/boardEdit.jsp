<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글 수정</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/lang/summernote-ko-KR.min.js"></script>

  <!-- ✅ 공통 스타일 -->
  <link href="${pageContext.request.contextPath}/resources/css/burgerList.css" rel="stylesheet">
  <style>
    .note-editor.note-frame {
      border: 1px solid #ced4da;
      border-radius: .375rem;
    }
  </style>
</head>

<body class="bg-light">
  <%@ include file="/include/header.jsp" %>

  <div class="container mt-5" style="max-width:700px;">
    <h2 class="text-center mb-4">✏️ 게시글 수정</h2>

    <form action="<%=request.getContextPath()%>/board/edit" method="post">
        <input type="hidden" name="boardId" value="${board.boardId}">

        <div class="mb-3">
            <label class="form-label">제목</label>
            <input type="text" name="title" class="form-control" value="${board.title}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">카테고리</label>
            <select name="category" class="form-select">
                <c:if test="${sessionScope.LOGIN_ROLE eq 'ADMIN'}">
                	<option value="공지사항" ${board.category == '공지사항' ? 'selected' : ''}>공지사항</option>
  				</c:if>
                <option value="자유" ${board.category == '자유' ? 'selected' : ''}>자유</option>
                <option value="버거킹" ${board.category == '버거킹' ? 'selected' : ''}>버거킹</option>
                <option value="맥도날드" ${board.category == '맥도날드' ? 'selected' : ''}>맥도날드</option>
                <option value="롯데리아" ${board.category == '롯데리아' ? 'selected' : ''}>롯데리아</option>
                <option value="수제버거" ${board.category == '수제버거' ? 'selected' : ''}>수제버거</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">내용</label>
            <textarea id="summernote" name="content" required>${board.content}</textarea>
        </div>

        <div class="text-center">
            <button type="submit" class="btn btn-primary">수정 완료</button>
            <a href="<%=request.getContextPath()%>/board/view?id=${board.boardId}" class="btn btn-secondary">취소</a>
        </div>
    </form>
  </div>

  <%@ include file="/include/footer.jsp" %>
  
    <script>
    $(document).ready(function() {
      $('#summernote').summernote({
        height: 300,
        lang: 'ko-KR',
        placeholder: '내용을 입력하세요...',
        toolbar: [
          ['style', ['bold', 'italic', 'underline', 'clear']],
          ['font', ['fontsize', 'color']],
          ['para', ['ul', 'ol', 'paragraph']],
          ['insert', ['link', 'picture']],
          ['view', ['codeview']]
        ]
      });
    });
  </script>
  
</body>
</html>
