<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글 목록</title>

  <!-- ✅ Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <style>
    .board-page {
      font-family: 'Noto Sans KR', sans-serif;
      padding: 40px 0;
      min-height: 100vh;
      background-color: #FFFAF0;
    }

    .board-page .board-container {
      max-width: 1100px;
      margin: 0 auto;
      background-color: #FFFAF0;
      border-radius: 12px;
      padding: 40px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    .board-page h2 {
      text-align: center;
      font-weight: 600;
      color: #000;
      border-bottom: 2px solid #B35A00;
      display: inline-block;
      padding-bottom: 6px;
      margin-bottom: 30px;
    }

    .board-page .filter-bar {
      background-color: #FFF8E6;
      border: 1px solid #F7EEDC;
      border-radius: 8px;
      padding: 10px 15px;
      margin-bottom: 20px;
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      align-items: center;
      gap: 10px;
    }

    .board-page .category-list {
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
    }

    .board-page .category-list a {
      border: 1px solid #E0D5B5;
      background-color: #fff;
      color: #555;
      font-size: 14px;
      border-radius: 6px;
      padding: 5px 12px;
      text-decoration: none;
      transition: 0.15s;
    }

    .board-page .category-list a.active,
    .board-page .category-list a:hover {
      background-color: #FFF2C6;
      color: #B35A00;
      border-color: #D6C28E;
    }

    .board-page .search-box {
      display: flex;
      align-items: center;
      border: 1px solid #E0D5B5;
      background-color: #fff;
      border-radius: 6px;
      overflow: hidden;
      height: 36px;
    }

    .board-page .search-box input {
      flex: 1;
      border: none;
      outline: none;
      padding: 0 10px;
      font-size: 14px;
    }

    .board-page .search-box button {
      background-color: #B35A00;
      color: #fff;
      border: none;
      padding: 0 14px;
      height: 100%;
      cursor: pointer;
    }

    .board-page .search-box button:hover {
      background-color: #944C00;
    }

    .board-page .btn-main {
      background-color: #B35A00;
      border: none;
      color: #fff;
      font-weight: 500;
      border-radius: 6px;
      padding: 7px 16px;
      font-size: 14px;
    }

    .board-page .btn-main:hover {
      background-color: #944C00;
    }
    .board-page .btn-secondary {
	  background-color: #EBD9B4;   /* 은은한 베이지톤 */
	  border: 1px solid #D6C28E;
	  color: #555;
	}
	
	.board-page .btn-secondary:hover {
	  background-color: #E2CFA5;
	  border-color: #CDB47C;
	  color: #333;
	}

    .board-page table {
      width: 100%;
      border: 1px solid #eee;
      border-radius: 8px;
      overflow: hidden;
      background-color: #fff;
      table-layout: fixed;
    }

    .board-page thead th:nth-child(1) { width: 6%; }
    .board-page thead th:nth-child(2) { width: 42%; }
    .board-page thead th:nth-child(3) { width: 13%; }
    .board-page thead th:nth-child(4) { width: 12%; }
    .board-page thead th:nth-child(5) { width: 15%; }
    .board-page thead th:nth-child(6) { width: 8%; }

	.board-page thead {
  	  background-color: #FFF8E6;
      color: #777;
      font-weight: 400;
      border-bottom: 2px solid #D8C9AA; /* ← 연한 갈색줄 */
    }

	.board-page th, 
	.board-page td {
	  text-align: center;
	  vertical-align: middle;
	  border-bottom: 1px solid #E9E2C9; /* ← 연한 베이지 선 */
	  padding: 8px 6px;
	  word-break: keep-all;
	  background-color: #FFFCF5; /* ← 상아색 톤 */
	  text-overflow: ellipsis;
	  white-space: nowrap;    /* ✅ 추가 */
	  overflow: hidden;       /* ✅ 추가 */
	  text-overflow: ellipsis;
	}
	
    .board-page tr.notice-row td{
 	  background-color: #fff8d9bd; /* 부드러운 크림톤 */
	}
	
	.board-page td.text-start {
	  text-align: left;
	  padding-left: 14px;
	}
	
	.board-page tbody tr:hover {
	  background-color: #FFF4DF;
	  transition: background-color 0.2s ease-in-out;
	}

    .board-page a.text-dark:hover {
      color: #B35A00 !important;
    }

    @media (max-width: 768px) {
      .board-page .filter-bar {
        flex-direction: column;
        align-items: flex-start;
      }
      .board-page .search-box {
        width: 100%;
      }
      .board-page table { font-size: 13px; }
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
  <div class="board-page">
    <div class="board-container">
      <h2>이 자리에 뭘 적을까요</h2>
      <div class="text-end mb-3">
        <c:choose>
          <c:when test="${not empty sessionScope.LOGIN_UID}">
            <a href="${pageContext.request.contextPath}/board/write" class="btn btn-main">글쓰기</a>
          </c:when>
          <c:otherwise>
            <button type="button" class="btn btn-secondary"
                    onclick="alert('로그인 후 이용해주세요.'); location.href='${pageContext.request.contextPath}/user/login.jsp';">
              글쓰기
            </button>
          </c:otherwise>
        </c:choose>
      </div>

      <div class="filter-bar">
        <div class="category-list">
          <c:set var="categories" value="${fn:split('전체,공지사항,이벤트,업데이트,롯데리아,수제버거', ',')}"/>
          <c:forEach var="cat" items="${categories}">
            <c:choose>
              <c:when test="${cat eq selectedCategory}">
                <a href="${pageContext.request.contextPath}/board/list?category=${cat}" class="active">${cat}</a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/board/list?category=${cat}">${cat}</a>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </div>
        <form method="get" action="${pageContext.request.contextPath}/board/list" class="search-box">
          <input type="hidden" name="category" value="${selectedCategory}">
          <input type="text" name="keyword" value="${keyword}" placeholder="검색어 입력...">
          <button type="submit">검색</button>
        </form>
      </div>

      <table class="table table-hover align-middle mb-0">
        <thead>
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
            <tr class="${b.category eq '공지사항' ? 'notice-row fw-semibold' : ''}">
              <td>${b.category eq '공지사항' ? '📢' : b.boardId}</td>
              <td class="text-start">
                <a href="${pageContext.request.contextPath}/board/view?id=${b.boardId}"
                   class="text-decoration-none text-dark">${b.title}</a>
              </td>
              <td>${b.writerNickname}</td>
              <td>${b.category}</td>
              <td>${b.formattedDate}</td>
              <td>${b.viewCount}</td>
            </tr>
          </c:forEach>
          <c:if test="${empty boardList}">
            <tr>
              <td colspan="6" class="text-center text-muted py-4">
                등록된 게시글이 없습니다.
              </td>
            </tr>
          </c:if>
        </tbody>
      </table>
      <div class="text-center mt-4">
		<c:forEach var="i" begin="1" end="${totalPage}">
	      <a href="${pageContext.request.contextPath}/board/list?category=${selectedCategory}&type=${type}&keyword=${keyword}&page=${i}"
	        class="mx-1 ${i == page ? 'fw-bold text-success' : 'text-secondary'}">
		    ${i}
		  </a>
		</c:forEach>
      </div>
    </div>
  </div>


  <%@ include file="/include/footer.jsp" %>
</body>
</html>
