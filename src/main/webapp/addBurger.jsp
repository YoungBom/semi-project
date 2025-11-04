<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>부거 등록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
	body { background-color: #fffaf0; font-family: 'Poppins', sans-serif; }
	.container { max-width: 600px; margin-top: 60px; }
	h2 { text-align: center; font-weight: 700; color: #ff6600; margin-bottom: 25px; }
</style>
</head>
<body>
	<div class="container py-5">
		<h2>버거 등록</h2>
		<form action="${pageContext.request.contextPath}/burger/add" method="post" enctype="multipart/form-data">
			<div class="mb-3">
				<label class="form-label">제품명</label>
				<input type="text" name="name" class="form-control">
			</div>
			<div class="mb-3">
				<label class="form-label">브랜드</label>
				<input type="text" name="brand" class="form-control">
			</div>
			<div class="mb-4">
				<label class="form-label">가격</label>
				<input type="number" name="price" class="form-control">
			</div>
			<div class="mb-3">
				<label class="form-label">패티</label>
				<input type="radio" name="pattyType" value="치킨"> 치킨
				<input type="radio" name="pattyType" value="비프"> 비프
				<input type="radio" name="pattyType" value="기타"> 기타
			</div>
			<div class="mb-3">
				<label class="form-label">이미지</label>
				<input type="file" name="imagePath" class="form-control" accept="image/*">
			</div>
			
			<br>
			
			<h5 class="mt-4 mb-2">버거 상세</h5>
			<div>
				<div>
					<input type="number" name="calories" class="form-control" placeholder="칼로리">
				</div>
				<div>
					<input type="number" name="carbohydrates" class="form-control" placeholder="탄수화물">
				</div>
				<div>
					<input type="number" name="protein" class="form-control" placeholder="단백질">
				</div>
				<div>
					<input type="number" name="fat" class="form-control" placeholder="지방">
				</div>
				<div>
					<input type="number" name="sodium" class="form-control" placeholder="나트륨">
				</div>
				<div>
					<input type="number" name="sugar" class="form-control" placeholder="당류">
				</div>
			<h5 class="mt-4 mb-2">알레르기 유발 정보</h5>	
			<div class="mb-3">
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="우유">
					<label class="form-check-label">우유</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="대두">
					<label class="form-check-label">대두</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="밀">
					<label class="form-check-label">밀</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="토마토">
					<label class="form-check-label">토마토</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="난류">
					<label class="form-check-label">난류</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="닭고기">
					<label class="form-check-label">닭고기</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="돼지고기">
					<label class="form-check-label">돼지고기</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="쇠고기">
					<label class="form-check-label">쇠고기</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="새우">
					<label class="form-check-label">새우</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="checkbox" name="allergyInfo"	value="굴">
					<label class="form-check-label">굴</label>
				</div>
				
			</div>	


				
			</div>
			<button type="submit" class="btn btn-success">등록</button>
		</form>
	</div>
</body>
</html>