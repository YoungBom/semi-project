<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원정보 수정</title>

  <c:set var="ctx" value="${pageContext.request.contextPath}" />
  <link rel="stylesheet" href="${ctx}/resources/css/user.css">
</head>
<body>
  <h1>회원정보 수정</h1>
  <c:if test="${not empty sessionScope.flash}">
  <div class="toast toast-success"><c:out value="${sessionScope.flash}" /></div>
  <c:remove var="flash" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.flash_error}">
  <div class="toast toast-error"><c:out value="${sessionScope.flash_error}" /></div>
  <c:remove var="flash_error" scope="session" />
</c:if>
  <!-- birth input용 표시 값 만들기: birthView(서버 포맷) > YYYYMMDD(8자리) > yyyy-MM-dd(이미 포맷) > 공백 -->
  <c:set var="birthVal" value=""/>
  <c:choose>
    <c:when test="${not empty birthView}">
      <c:set var="birthVal" value="${birthView}" />
    </c:when>
    <c:when test="${not empty me.birth and fn:length(me.birth) == 8}">
      <c:set var="birthVal"
             value="${fn:substring(me.birth,0,4)}-${fn:substring(me.birth,4,6)}-${fn:substring(me.birth,6,8)}" />
    </c:when>
    <c:when test="${not empty me.birth and fn:length(me.birth) == 10 and fn:contains(me.birth,'-')}">
      <c:set var="birthVal" value="${me.birth}" />
    </c:when>
  </c:choose>

  <form method="post" action="${ctx}/user/edit">
    <label>이메일
      <input type="email" name="email" value="<c:out value='${me.email}'/>" required>
    </label><br>

    <label>닉네임
      <input name="nickname" value="<c:out value='${me.nickname}'/>" required>
    </label><br>

    <label>휴대폰
     <input name="phone"
         value="${not empty form_phone ? form_phone : me.phone}">
    </label>
    <c:if test="${not empty error_phone}">
    <p class="field-error">${error_phone}</p>
    </c:if>
    <br>


    <label>생년월일
      <input type="date" name="birth" value="${birthVal}">
    </label><br>

    <label>성별
      <select name="gender">
        <option value="" disabled>선택</option>
        <option value="남" ${me.gender == '남' ? 'selected' : ''}>남성</option>
        <option value="여" ${me.gender == '여' ? 'selected' : ''}>여성</option>
      </select>
    </label><br>

    <label>주소
      <input name="address" value="<c:out value='${me.address}'/>">
    </label><br>

    <div class="actions">
      <button type="submit">저장</button>
      <a href="${ctx}/user/mypage">취소</a>
    </div>
  </form>
</body>
</html>
