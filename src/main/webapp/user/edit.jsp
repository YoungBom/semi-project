<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- birth 표시값 만들기: form_birth 우선 -->
<c:set var="birthVal" value=""/>
<c:choose>
  <c:when test="${not empty form_birth}">
    <c:set var="birthVal" value="${form_birth}" />
  </c:when>
  <c:when test="${not empty me.birth and fn:length(me.birth) == 8}">
    <c:set var="birthVal" value="${fn:substring(me.birth,0,4)}-${fn:substring(me.birth,4,6)}-${fn:substring(me.birth,6,8)}" />
  </c:when>
  <c:when test="${not empty me.birth and fn:length(me.birth) == 10 and fn:contains(me.birth,'-')}">
    <c:set var="birthVal" value="${me.birth}" />
  </c:when>
</c:choose>

<!-- 성별 표시값: form_gender 우선 -->
<c:set var="g" value="${not empty form_gender ? form_gender : me.gender}" />

<form method="post" action="${ctx}/user/edit">
  <label>이메일
    <input type="email" name="email"
           value="${not empty form_email ? form_email : me.email}">
  </label><br>

  <label>닉네임
    <input name="nickname"
           value="${not empty form_nickname ? form_nickname : me.nickname}">
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
      <option value="남" ${g == '남' ? 'selected' : ''}>남성</option>
      <option value="여" ${g == '여' ? 'selected' : ''}>여성</option>
    </select>
  </label><br>

  <label>주소
    <input name="address"
           value="${not empty form_address ? form_address : me.address}">
  </label><br>

  <div class="actions">
    <button type="submit">저장</button>
    <a href="${ctx}/user/mypage">취소</a>
  </div>
</form>
