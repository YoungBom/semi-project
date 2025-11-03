<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>BurgerHub 🍔 메뉴 페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<meta name="context-path" content="${pageContext.request.contextPath}">

<style>
  body {
    font-family: 'Poppins', sans-serif;
    background-color: #fffaf0;
    color: #333;
  }

  h2 {
    text-align: center;
    font-weight: 700;
    margin-top: 40px;
  }

  /* 🔸 필터 버튼 스타일 */
  .filter-btns {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin: 25px 0;
  }
  .filter-btn {
    border: 1px solid #ff6600;
    background: white;
    color: #ff6600;
    font-weight: 600;
    border-radius: 25px;
    padding: 6px 18px;
    cursor: pointer;
    transition: all 0.2s ease;
  }
  .filter-btn:hover,
  .filter-btn.active {
    background: #ff6600;
    color: white;
  }

  /* 🔸 카드 스타일 */
  .burger-card {
    border: none;
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    transition: transform 0.2s ease, box-shadow 0.2s ease;
    background: #fff;
  }
  .burger-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 8px 20px rgba(0,0,0,0.15);
  }
  .burger-card img {
    height: 200px;
    object-fit: cover;
  }
  .price {
    color: #ff6600;
    font-weight: 700;
  }
</style>
</head>
<body>

<!-- ✅ 타이틀 -->
<h2>🍔 메뉴 페이지</h2>

<!-- ✅ 필터 버튼 -->
<div class="filter-btns">
  <button class="filter-btn active" data-type="all">전체</button>
  <button class="filter-btn" data-type="비프">비프</button>
  <button class="filter-btn" data-type="치킨">치킨</button>
  <button class="filter-btn" data-type="기타">기타</button>
</div>

<!-- ✅ 결과 영역 -->
<div class="container">
  <div id="burgerContainer" class="row justify-content-center g-4">
    <!-- AJAX로 버거 카드가 여기에 렌더링됨 -->
  </div>
</div>


<!-- ✅ JS 외부 파일 연결 -->
<script src="${pageContext.request.contextPath}/resources/js/filter.js"></script>

</body>
</html>
