<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String ctx = request.getContextPath();
  dto.UserDTO user = (dto.UserDTO) request.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>프로필</title>
<link rel="stylesheet" href="<%=ctx%>/resources/css/user.css">
</head>
<body>

<% String flash = (String) session.getAttribute("FLASH_SUCCESS");
   if (flash != null) { session.removeAttribute("FLASH_SUCCESS"); %>
  <p class="msg success"><%= flash %></p>
<% } %>
<% if (request.getAttribute("error") != null) { %>
  <p class="msg error"><%= request.getAttribute("error") %></p>
<% } %>

<h1>프로필</h1>

<form method="post" action="<%=ctx%>/user/profile/update" autocomplete="on">
  <label>
    이메일
    <input type="email" name="email"
      value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : (user!=null?user.getEmail():"") %>"
      required>
  </label>

  <label>
    전화번호
    <input type="text" name="phone" placeholder="010-1234-5678"
      value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : (user!=null?user.getPhone():"") %>"
      required>
  </label>

  <label>
    닉네임
    <input type="text" name="nickname"
      value="<%= request.getAttribute("nickname") != null ? request.getAttribute("nickname") : (user!=null?user.getNickname():"") %>">
  </label>

  <label>
    주소
    <input type="text" name="address"
      value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : (user!=null?user.getAddress():"") %>">
  </label>

  <div class="actions">
    <button type="submit">저장</button>
    <a class="button" href="<%=ctx%>/user/change-password">비밀번호 변경</a>
  </div>
</form>

</body>
</html>
