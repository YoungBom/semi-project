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
  <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;600&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ Summernote -->
  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/lang/summernote-ko-KR.min.js"></script>

  <!-- 🎨 board-edit-page 전용 스타일 -->
  <style>
    .board-edit-page {
      background-color: #FFFAF0;
      font-family: 'Noto Sans KR', sans-serif;
      padding: 60px 0;
      min-height: 100vh;
    }

    .board-edit-page .edit-container {
      max-width: 700px;
      margin: 0 auto;
      background: linear-gradient(180deg, #FFFDF8 0%, #FFFAF0 100%);
      border-radius: 12px;
      padding: 40px 45px;
      box-shadow: 0 1px 6px rgba(0, 0, 0, 0.04);
    }

    .board-edit-page h2 {
      text-align: center;
      font-weight: 600;
      color: #000;
      border-bottom: 2px solid #B35A00;
      display: inline-block;
      padding-bottom: 6px;
      margin-bottom: 30px;
    }

    .board-edit-page label.form-label {
      font-weight: 500;
      color: #333;
    }

    .board-edit-page input.form-control,
    .board-edit-page select.form-select {
      border: 1px solid #E0D5B5;
      background-color: #fff;
    }

    .board-edit-page input.form-control:focus,
    .board-edit-page select.form-select:focus {
      border-color: #B35A00;
      box-shadow: 0 0 0 0.1rem rgba(179, 90, 0, 0.15);
    }

    /* ✅ 버튼 스타일 */
    .board-edit-page .btn-primary {
      background-color: #B35A00;
      border: none;
      color: #fff;
      font-weight: 500;
      border-radius: 6px;
      padding: 7px 18px;
    }

    .board-edit-page .btn-primary:hover {
      background-color: #944C00;
    }

    .board-edit-page .btn-secondary {
      background-color: #F7EEDC;
      border: 1px solid #E0D5B5;
      color: #555;
    }

    .board-edit-page .btn-secondary:hover {
      background-color: #F0E4CF;
      border-color: #D6C28E;
      color: #333;
    }

    /* ✅ Summernote */
    .board-edit-page .note-editor.note-frame {
      border: 1px solid #E0D5B5 !important;
      border-radius: 8px;
      background-color: #fff;
    }

    .board-edit-page .note-toolbar {
      background-color: #FFF8E6 !important;
      border-bottom: 1px solid #E9E2C9 !important;
    }

    .board-edit-page .note-editable {
      background-color: #FFFCF5 !important;
    }
  </style>
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <div class="board-edit-page">
    <div class="edit-container">
      <h2>✏️ 게시글 수정</h2>

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
          <button type="submit" class="btn btn-primary me-2">수정 완료</button>
          <a href="<%=request.getContextPath()%>/board/view?id=${board.boardId}" class="btn btn-secondary">취소</a>
        </div>
      </form>
    </div>
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
