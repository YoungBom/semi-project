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
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    .board-view-page {
      background-color: #FFFAF0;
      font-family: 'Noto Sans KR', sans-serif;
      padding: 70px 0;
      min-height: 100vh;
    }

    .board-view-page .view-container {
      max-width: 1000px;
      margin: 0 auto;
      background-color: #FFFCF5;
      border-radius: 10px;
      padding: 50px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      border: 1px solid #F0E7D8;
    }

    /* 제목 */
    .board-view-page h2 {
      font-weight: 600;
      color: #000;
      font-size: 1.9rem;
      margin: 30px 0;
      border-bottom: 2px solid #B35A00;
      padding-bottom: 10px;
    }

    /* 메타 정보 */
    .board-view-page .meta-info {
      display: flex;
      flex-wrap: wrap;
      gap: 10px 25px;
      font-size: 14px;
      color: #555;
      background-color: #FFF8E6;
      border-radius: 6px;
      padding: 10px 14px;
      margin-bottom: 30px;
      box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.05);
    }

    /* 본문 */
    .board-view-page .content-box {
      background-color: #FFFEFB;
      border: 1px solid #EFE6D2;
      border-radius: 6px;
      min-height: 350px;
      line-height: 1.8;
      color: #333;
      padding: 30px;
      white-space: pre-wrap;
      word-break: break-word;
    }

    /* 버튼 영역 */
    .board-view-page .btn-area {
      margin-top: 30px;
      text-align: right;
    }

    /* 버튼 스타일 */
    .board-view-page .btn-warning {
      background-color: #FFD46E;
      border: none;
      color: #333;
    }
    .board-view-page .btn-warning:hover { background-color: #F5C85D; }

    .board-view-page .btn-danger {
      background-color: #E66A6A;
      border: none;
      color: #fff;
    }
    .board-view-page .btn-danger:hover { background-color: #D85555; }

    .board-view-page .btn-secondary {
      background-color: #F7EEDC;
      border: none;
      color: #555;
    }
    .board-view-page .btn-secondary:hover {
      background-color: #F0E4CF;
      color: #333;
    }
    .board-banner img {
	  width: 100%;
	  max-height: 200px;
	  object-fit: cover;
	  object-position: center 60%;
	  border-radius: 10px;
	  filter: brightness(0.97) saturate(0.85) contrast(0.8);
	  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
	}
  </style>
</head>

<body>
  <%@ include file="/include/header.jsp" %>
  <div class="board-banner mb-4">
    <img src="${pageContext.request.contextPath}/img/board_banner.png" alt="공지 배너">
  </div>

  <div class="board-view-page">
    <div class="view-container">

      <h2>${board.title}</h2>

      <div class="meta-info">
        <div>No. ${board.boardId}</div>
        <div>작성자: ${board.writerNickname}</div>
        <div>카테고리: ${board.category}</div>
        <div>작성일: ${board.formattedDateTime}</div>
        <div>조회수: ${board.viewCount}</div>
      </div>

      <div class="content-box">
        ${board.content}
      </div>

      <div class="btn-area">
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
