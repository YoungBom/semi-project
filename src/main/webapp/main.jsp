<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>BurgerHub 🍔</title>

<!-- ✅ Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- ✅ Google Fonts -->
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">

<style>
body {
    font-family: 'Poppins', sans-serif;
    background-color: #fffaf0;
    color: #333;
}
.navbar {
    background: linear-gradient(90deg, #ff9900, #ffcc33);
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.navbar-brand {
    font-weight: 700;
    font-size: 1.5rem;
    color: white !important;
}
.hero {
    background: url('https://images.unsplash.com/photo-1606755962773-d3248f0ec02d?auto=format&fit=crop&w=1500&q=80') center/cover;
    color: white;
    text-align: center;
    padding: 100px 20px;
    background-blend-mode: darken;
    background-color: rgba(0,0,0,0.4);
}
.hero h1 {
    font-size: 3rem;
    font-weight: 700;
}
.hero p {
    font-size: 1.2rem;
    opacity: 0.9;
}
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
.card-body {
    padding: 15px 18px;
}
.card-title {
    font-weight: 700;
    color: #333;
}
.card-text {
    color: #666;
    font-size: 0.9rem;
}
.badge-brand {
    background-color: #ffcc33;
    color: #333;
    font-weight: 600;
    margin-bottom: 6px;
}
.footer {
    background-color: #333;
    color: #bbb;
    text-align: center;
    padding: 20px;
    margin-top: 50px;
    font-size: 0.9rem;
}
.price {
    color: #ff6600;
    font-weight: 700;
}
.rating {
    color: #ffb400;
}
</style>
</head>
<body>

<!-- ✅ 네비게이션 바 -->
<nav class="navbar navbar-expand-lg">
  <div class="container">
    <a class="navbar-brand" href="#">🍔 BurgerHub</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
  </div>
</nav>

<!-- ✅ Hero Section -->
<section class="hero">
  <h1>국내 모든 버거 브랜드 한눈에!</h1>
  <p>맥도날드 · 버거킹 · 맘스터치 · 롯데리아 등 인기 버거 총집합</p>
</section>

<!-- ✅ DB 연결 -->
<sql:setDataSource var="db" 
    driver="com.mysql.cj.jdbc.Driver"
    url="jdbc:mysql://127.0.0.1:3306/burgerhub_simple"
    user="root"
    password="mysql1234" />

<%-- <sql:query var="burgers" dataSource="${db}">
    SELECT * FROM burger ORDER BY brand, name;
</sql:query> --%>

<!-- ✅ 버거 리스트 -->
<div class="container my-5">
  <h2 class="fw-bold mb-4 text-center">🔥 인기 버거 메뉴</h2>
  <div class="row justify-content-center">
    <c:forEach var="b" items="${burgers.rows}">
      <div class="col-md-3 col-sm-6 mb-4">
        <div class="card burger-card">
          <img src="${b.image_url}" class="card-img-top" alt="${b.name}">
          <div class="card-body">
            <span class="badge badge-brand">${b.brand}</span>
            <h5 class="card-title">${b.name}</h5>
            <p class="card-text">${b.description}</p>
            <div class="d-flex justify-content-between align-items-center mt-3">
              <span class="price">${b.price}원</span>
              <span class="rating">⭐ ${b.avg_rating}</span>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>
</div>

<!-- ✅ Footer -->
<footer class="footer">
  <p>© 2025 BurgerHub. All rights reserved.</p>
</footer>

</body>
</html>
