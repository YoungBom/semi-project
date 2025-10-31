<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<nav class="navbar">
  		<div class="container">
    	<a class="logo" href="#">🍔 BurgerHub</a>
    	<ul class="nav-links">
	      <li><a href="#">메뉴</a></li>
	      <li><a href="#">이벤트</a></li>
	      <li><a href="#">공지사항</a></li>
	      <li><a href="#">고객센터</a></li>
    	</ul>
    	
    	<form action="${pageContext.request.contextPath}/search" method="post" class="search-form">
	      <input type="text" name="keyword" placeholder="버거 검색..." class="search-input">
	      <button type="submit" class="search-btn">
	      	<i class="bi bi-search"></i>
	      </button>
   		</form>
    	
	    <div class="user-actions">
	      <a href="#">로그인</a>
	    </div>
	  </div>
	</nav>
	
</body>
</html>