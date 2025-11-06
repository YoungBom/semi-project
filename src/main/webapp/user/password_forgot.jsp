<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>비밀번호 찾기</title></head>
<body>
<h1>비밀번호 찾기</h1>
<c:if test="${not empty msg}"><p>${msg}</p></c:if>
<c:if test="${not empty debug_token}">
  <p>DEBUG TOKEN: ${debug_token}</p>
</c:if>
</body>
</html>