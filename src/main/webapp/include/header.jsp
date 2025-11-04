<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String ctx = request.getContextPath();
  jakarta.servlet.http.HttpSession s = request.getSession(false);  // ← jakarta로
  Integer loginUid  = (s == null) ? null : (Integer) s.getAttribute("LOGIN_UID");
  String  loginName = (s == null) ? null : (String)  s.getAttribute("LOGIN_NAME");
  String  csrf      = (s == null) ? null : (String)  s.getAttribute("CSRF_TOKEN");
%>

<% Integer uid = (session==null)?null:(Integer)session.getAttribute("LOGIN_UID"); %>
<% if (uid == null) { %>
  <a class="btn" href="<%=ctx%>/login">로그인</a>
<% } else { %>
  <a class="btn" href="<%=ctx%>/mypage">마이페이지</a>
<% } %>

<header class="site-header">
  <div class="header-left">
    <a class="logo" href="<%=ctx%>/">🍔 BurgerHub</a>
    <nav class="gnb">
      <a href="<%=ctx%>/menu.jsp">메뉴</a>
      <a href="<%=ctx%>/event.jsp">이벤트</a>
      <a href="<%=ctx%>/notice.jsp">공지사항</a>
      <a href="<%=ctx%>/help.jsp">고객센터</a>
    </nav>
  </div>

  <div class="header-right">
    <% if (loginUid != null) { %>
      <span class="hello">안녕하세요, <strong><%=loginName%></strong>님</span>
      <a class="btn" href="<%=ctx%>/mypage">마이페이지</a>

      <!-- 로그아웃은 POST 권장 -->
      <form method="post" action="<%=ctx%>/logout" style="display:inline;">
        <% if (csrf != null) { %>
          <input type="hidden" name="_csrf" value="<%=csrf%>">
        <% } %>
        <button class="btn outline" type="submit">로그아웃</button>
      </form>
    <% } else { %>
      <a class="btn outline" href="<%=ctx%>/login">로그인</a>
      <a class="btn" href="<%=ctx%>/signup">회원가입</a>
    <% } %>
  </div>
</header>

<style>
  .site-header{display:flex;justify-content:space-between;align-items:center;padding:10px 16px}
  .logo{font-weight:800;text-decoration:none}
  .gnb a{margin:0 10px;text-decoration:none;color:#333}
  .header-right{display:flex;align-items:center;gap:10px}
  .hello{margin-right:6px;color:#666}
  .btn{padding:6px 12px;border-radius:8px;border:1px solid #ff6a00;background:#ff6a00;color:#fff;text-decoration:none}
  .btn.outline{background:#fff;color:#ff6a00;border:1px solid #ff6a00}
  .btn:hover{opacity:.9}
</style>
