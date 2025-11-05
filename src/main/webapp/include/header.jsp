<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
  String ctx = request.getContextPath();
  Object uidObj = session.getAttribute("LOGIN_UID");
  String userName = (String) session.getAttribute("LOGIN_NAME");
  boolean loggedIn = (uidObj != null);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Header</title>
</head>
<body>
	<nav class="navbar">
		<div class="container">
			<a class="logo" href="<%=ctx%>/main.jsp">🍔 BurgerHub</a>

			<ul class="nav-links">
				<li><a href="<%=ctx%>/menu.jsp">메뉴</a></li>
				<li><a href="#">이벤트</a></li>
				<li><a href="#">공지사항</a></li>
				<li><a href="#">고객센터</a></li>
			</ul>

			<form action="<%=ctx%>/search" method="post" class="search-form">
				<input type="text" name="keyword" placeholder="버거 검색..."
					class="search-input" aria-label="버거 검색">
				<button type="submit" class="search-btn">
					<i class="bi bi-search" aria-hidden="true"></i><span
						class="sr-only">검색</span>
				</button>
			</form>

			<div class="user-actions">
				<% if (!loggedIn) { %>
				<!-- 비로그인 상태: 로그인 + 회원가입 + 버거리스트 -->
				<a href="<%=ctx%>/user/login.jsp">로그인</a>
				<!-- ✅ 회원가입 버튼 추가 -->
				<a href="<%=ctx%>/user/register.jsp" class="btn btn-primary">회원가입</a>
				<!-- 만약 서블릿 매핑이라면 위 줄을 <a href="<%=ctx%>/register" ...>로 변경 -->
				<a href="<%=ctx%>/burger/list" class="btn">버거리스트</a>
				<%
				} else {
				%>
				<!-- 로그인 상태: 환영문 + 마이페이지 + 로그아웃 -->
				<span class="welcome">안녕하세요, <%=(userName == null ? "회원" : userName)%>님
				</span> <a href="<%=ctx%>/user/mypage" class="btn">마이페이지</a>
				<form method="post" action="<%=ctx%>/logout" class="logout-form"
					style="display: inline;">
					<button type="submit" class="btn btn-outline">로그아웃</button>
				</form>
				<%
				}
				%>
			</div>
		</div>
	</nav>
</body>
</html>
