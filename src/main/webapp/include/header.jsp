<%@ page contentType="text/html; charset=UTF-8"%>
<%
String ctx = request.getContextPath();
jakarta.servlet.http.HttpSession s = request.getSession(false);
Integer loginUid = (s == null) ? null : (Integer) s.getAttribute("LOGIN_UID");
String loginName = (s == null) ? null : (String) s.getAttribute("LOGIN_NAME");
%>

<nav class="bh-navbar">
	<div class="bh-nav-container">
		<div class="bh-left">
			<a class="bh-logo" href="<%=ctx%>/">🍔 BurgerHub</a>

			<ul class="bh-links">
				<li><a href="<%=ctx%>/menu.jsp">메뉴</a></li>
				<li><a href="<%=ctx%>/event.jsp">이벤트</a></li>
				<li><a href="<%=ctx%>/notice.jsp">공지사항</a></li>
				<li><a href="<%=ctx%>/help.jsp">고객센터</a></li>
			</ul>

			<form class="bh-search" method="get" action="<%=ctx%>/search">
				<input class="bh-search-input" type="text" name="q"
					placeholder="버거 검색..." />
				<button class="bh-search-btn" type="submit" aria-label="검색">🔍</button>
			</form>
		</div>

		<div class="bh-actions">
			<%
			if (loginUid == null) {
			%>
			<!-- 비로그인 상태: 로그인 / (로그인 후) 마이페이지로 이동 -->
			<a class="bh-btn bh-outline" href="<%=ctx%>/login">로그인</a> <a
				class="bh-btn"
				href="<%=ctx%>/login?next=<%=java.net.URLEncoder.encode(ctx + "/mypage", "UTF-8")%>">마이페이지</a>
			<%
			} else {
			%>
			<!-- 로그인 상태 -->
			<span class="bh-hello">안녕하세요, <strong><%=loginName%></strong>님
			</span> <a class="bh-btn" href="<%=ctx%>/mypage">마이페이지</a> <a
				class="bh-btn bh-outline" href="<%=ctx%>/edit">회원정보 수정</a>
			<form method="post" action="<%=ctx%>/logout" style="display: inline;">
				<button class="bh-btn bh-ghost" type="submit">로그아웃</button>
			</form>
			<%
			} // end if
			%>
		</div>
	</div>
</nav>
