<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>아이디 찾기</title></head>
<body>
<h1>아이디 찾기</h1>
<c:if test="${not empty msg}"><p>${msg}</p></c:if>
<c:if test="${not empty result}"><p>${result}</p></c:if>
</body>
</html>