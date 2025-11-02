<%@ page contentType="text/html; charset=UTF-8"%>
<%
Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER_ID");
String ctx = request.getContextPath();
%>
<header>
	<a href="<%=ctx%>/">Home</a>
	<nav>
		<%
		if (loginUserId == null) {
		%>
		<a href="<%=ctx%>/login">로그인</a> <a href="<%=ctx%>/signup">회원가입</a>
		<%
		} else {
		%>
		<a href="<%=ctx%>/mypage">마이페이지</a>
		<form method="post" action="<%=ctx%>/logout" style="display: inline">
			<button type="submit">로그아웃</button>
		</form>
		<%
		}
		%>
	</nav>
</header>
<hr />
s
