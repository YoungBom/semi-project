<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글 보기</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;600&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- 🎨 board-view-page 전용 스타일 -->
  <style>
    .board-view-page {
      background-color: #FFFAF0;
      font-family: 'Noto Sans KR', sans-serif;
      padding: 60px 0;
      min-height: 100vh;
    }

    .board-view-page .view-container {
      max-width: 800px;
      margin: 0 auto;
      background: linear-gradient(180deg, #FFFDF8 0%, #FFFAF0 100%);
      border-radius: 12px;
      padding: 40px 45px;
      box-shadow: 0 1px 6px rgba(0, 0, 0, 0.04);
    }

    .board-view-page h2 {
      font-weight: 600;
      color: #000;
      border-bottom: 2px solid #B35A00;
      display: inline-block;
      padding-bottom: 6px;
      margin-bottom: 20px;
    }

    .board-view-page .meta-info {
      font-size: 14px;
      color: #777;
      background-color: #FFF8E6;
      border: 1px solid #E9E2C9;
      border-radius: 6px;
      padding: 10px 12px;
      margin-bottom: 20px;
    }

    .board-view-page .content-box {
      background-color: #FFFCF5;
      border: 1px solid #E9E2C9;
      border-radius: 8px;
      min-height: 350px;
      line-height: 1.7;
      color: #333;
      white-space: pre-wrap;
    }

    /* ✅ 버튼 */
    .board-view-page .btn-primary {
      background-color: #B35A00;
      border: none;
      color: #fff;
      font-weight: 500;
      border-radius: 6px;
      padding: 7px 16px;
    }

    .board-view-page .btn-primary:hover {
      background-color: #944C00;
    }

    .board-view-page .btn-secondary {
      background-color: #F7EEDC;
      border: 1px solid #E0D5B5;
      color: #555;
    }

    .board-view-page .btn-secondary:hover {
      background-color: #F0E4CF;
      border-color: #D6C28E;
      color: #333;
    }

    .board-view-page .btn-warning {
      background-color: #FFD46E;
      border: none;
      color: #333;
    }

    .board-view-page .btn-warning:hover {
      background-color: #F5C85D;
    }

    .board-view-page .btn-danger {
      background-color: #E66A6A;
      border: none;
    }

    .board-view-page .btn-danger:hover {
      background-color: #D85555;
    }
  </style>
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <div class="board-view-page">
    <div class="view-container">
      <h2>${board.title}</h2>

      <div class="meta-info">
        작성자: ${board.writerNickname}   |
        카테고리: ${board.category}   |
        작성일: ${board.formattedDateTime}   |
        조회수: ${board.viewCount}
      </div>

      <div class="content-box">
        ${board.content}
      </div>

      <div class="mt-4 text-end">
        <c:if test="${sessionScope.LOGIN_UID eq board.writerId}">
          <a href="<%=request.getContextPath()%>/board/edit?id=${board.boardId}" class="btn btn-warning me-1">수정</a>
          <a href="<%=request.getContextPath()%>/board/delete?id=${board.boardId}"
             class="btn btn-danger me-1"
             onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
        </c:if>
        <a href="<%=request.getContextPath()%>/board/list" class="btn btn-secondary">목록으로</a>
      </div>
    </div>
  </div>

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
