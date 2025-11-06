<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>마이페이지</title>
  <link rel="stylesheet" href="${ctx}/resources/css/user.css">
</head>
<body>
  <h1>마이페이지</h1>

  <!-- 서블릿에서 user/me 어느 이름으로 내려와도 통일해서 쓰기 -->
  <c:set var="u" value="${not empty user ? user : me}" />

  <div class="card">
    <p><strong>아이디 :</strong> <c:out value="${u.userId}"/></p>
    <p><strong>이름 :</strong> <c:out value="${u.name}"/></p>
    <p><strong>닉네임 :</strong> <c:out value="${u.nickname}"/></p>
    <p><strong>이메일 :</strong> <c:out value="${u.email}"/></p>
    <p><strong>휴대폰 :</strong> <c:out value="${u.phone}"/></p>
    <p><strong>생년월일 :</strong> <c:out value="${u.birth}"/></p>
    <p><strong>성별 :</strong> <c:out value="${u.gender}"/></p>
    <p><strong>주소 :</strong> <c:out value="${u.address}"/></p>
  </div>

  <p>
    <a href="${ctx}/user/edit">정보 수정</a>
    |
    <a href="${ctx}/user/password_change.jsp">비밀번호 변경</a>
  </p>
</body>
</html>