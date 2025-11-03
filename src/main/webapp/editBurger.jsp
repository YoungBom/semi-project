<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>부거 수정</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
	body { background-color: #fffaf0; font-family: 'Poppins', sans-serif; }
	.container { max-width: 600px; margin-top: 60px; }
	h2 { text-align: center; font-weight: 700; color: #ff6600; margin-bottom: 25px; }
</style>
</head>

	
<body>
<!-- 테스트코드 -->
<%@ page import="dto.BurgerDTO" %>
	<form action="${pageContext.request.contextPath}/burger/edit" method="get">
		<div class="mb-3">
			<label class="form-label">버거 ID 입력</label>
			<input type="number" name="id">
		</div>
		<button type="submit">조회</button>
	</form>
<!-- 테스트코드 -->
	
	<div class="container py-5">
		<h2>버거 수정</h2>
		<form action="${pageContext.request.contextPath}/burger/edit" method="post" enctype="multipart/form-data">
			<input type="hidden" name="id" value="${burger.id}">
			<div class="mb-3">
				<label class="form-label">제품명</label>
				<input type="text" name="name" class="form-control" value="${burger.name}">
			</div>
			<div class="mb-3">
				<label class="form-label">브랜드</label>
				<input type="text" name="brand" class="form-control" value="${burger.brand}">
			</div>
			<div class="mb-4">
				<label class="form-label">가격</label>
				<input type="number" name="price" class="form-control" value="${burger.price}">
			</div>
			<div class="mb-3">
				<label class="form-label">패티</label>
				<input type="radio" name="pattyType" value="치킨"
					 ${burger.pattyType eq '치킨' ? 'checked' : ''}> 치킨
				<input type="radio" name="pattyType" value="비프"
					 ${burger.pattyType eq '비프' ? 'checked' : ''}> 비프
				<input type="radio" name="pattyType" value="기타"
					 ${burger.pattyType eq '기타' ? 'checked' : ''}> 기타
			</div>	
			<div class="mb-3">
				<label class="form-label">이미지</label>
				<input type="file" name="imagePath" class="form-control" accept="image/*">
			</div>
			<br>
			<h5 class="mt-4 mb-2">버거 상세</h5>
			<div>
				<div>
					<input type="number" name="calories" class="form-control" value="${burger.details.calories}">
				</div>
				<div>
					<input type="number" name="carbohydrates" class="form-control" value="${burger.details.carbohydrates}">
				</div>
				<div>
					<input type="number" name="protein" class="form-control" value="${burger.details.protein}">
				</div>
				<div>
					<input type="number" name="fat" class="form-control" value="${burger.details.fat}">
				</div>
				<div>
					<input type="number" name="sodium" class="form-control" value="${burger.details.sodium}">
				</div>
				<div>
					<input type="number" name="sugar" class="form-control" value="${burger.details.sugar}">
				</div>
			<h5 class="mt-4 mb-2">알레르기 유발 정보</h5>	
			<div class="mb-3">
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="우유"
						${fn:contains(burger.details.allergyInfo, '우유') ? 'checked' : ''}>
					<label class="form-check-label">우유</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="대두"
						${fn:contains(burger.details.allergyInfo, '대두') ? 'checked' : ''}>
					<label class="form-check-label">대두</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="밀"
						${fn:contains(burger.details.allergyInfo, '밀') ? 'checked' : ''}>
					<label class="form-check-label">밀</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="토마토"
						${fn:contains(burger.details.allergyInfo, '토마토') ? 'checked' : ''}>
					<label class="form-check-label">토마토</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="난류"
						${fn:contains(burger.details.allergyInfo, '난류') ? 'checked' : ''}>
					<label class="form-check-label">난류</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="닭고기"
						${fn:contains(burger.details.allergyInfo, '닭고기') ? 'checked' : ''}>
					<label class="form-check-label">닭고기</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="돼지고기"
						${fn:contains(burger.details.allergyInfo, '돼지고기') ? 'checked' : ''}>
					<label class="form-check-label">돼지고기</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="쇠고기"
						${fn:contains(burger.details.allergyInfo, '쇠고기') ? 'checked' : ''}>
					<label class="form-check-label">쇠고기</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="새우"
						${fn:contains(burger.details.allergyInfo, '새우') ? 'checked' : ''}>
					<label class="form-check-label">새우</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo" value="굴"
						${fn:contains(burger.details.allergyInfo, '굴') ? 'checked' : ''}>
					<label class="form-check-label">굴</label>
				</div>
			</div>


				
			</div>
			<button type="submit" class="btn btn-success">수정</button>
		</form>
	</div>
</body>
</html>