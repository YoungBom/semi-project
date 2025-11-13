<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>🍔 BurgerHub — 내 주변 매장</title>

<!-- ✅ Bootstrap & Google Fonts -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">



<!-- ✅ main.css -->
<link href="${pageContext.request.contextPath}/resources/css/findStore.css" rel="stylesheet">

<!-- ✅ 카카오 지도 SDK -->
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2890edbb91db82862429679da4bd158c&libraries=services"></script>
</head>
<body>
<%@ include file="/include/header.jsp" %>

<h2 class="page-title text-center" id="maptitle">
  <i class="bi bi-geo-alt-fill text-danger"></i>
  <span class="fw-bold" style="color:#ff6600;">BurgerHub</span>
  <span class="text-dark"> — 내 주변 패스트푸드 매장</span>
</h2>
<div id="map"></div>
<div id="list"></div>

<!-- ✅ 내 위치 버튼 -->
<button id="myLocationBtn" title="내 위치로 이동">📍</button>

<%@ include file="/include/footer.jsp" %>

<!-- ✅ contextPath를 JS로 전달 -->
<script>
  window.CTX = '${pageContext.request.contextPath}';
</script>

<!-- ✅ 외부 JS 파일 연결 -->
<script src="${pageContext.request.contextPath}/resources/js/findStore.js"></script>
</body>
</html>
