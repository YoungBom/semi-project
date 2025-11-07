<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>버거 등록</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ 공통 스타일 -->
  <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resources/css/burgerList.css" rel="stylesheet">
	
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <div class="burger-page burger-add-page">

    <div class="page-header mb-5">
      <h2>버거 등록</h2>
    </div>

    <div class="container">
      <div class="form-container">
        <form action="${pageContext.request.contextPath}/burger/add" method="post" enctype="multipart/form-data">

          <div class="mb-3">
            <label class="form-label">제품명</label>
            <input type="text" name="name" class="form-control" placeholder="예: 불고기버거" required>
          </div>

          <div class="mb-3">
            <label class="form-label">브랜드</label>
            <select name="brand" class="form-select" required>
              <option value="" selected disabled>브랜드 선택</option>
              <option value="맥도날드">맥도날드</option>
              <option value="버거킹">버거킹</option>
              <option value="롯데리아">롯데리아</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="form-label">가격</label>
            <input type="number" name="price" class="form-control" placeholder="예: 5500" required>
          </div>

          <div class="mb-3">
            <label class="form-label">패티 종류</label><br>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="pattyType" value="치킨">
              <label class="form-check-label">치킨</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="pattyType" value="비프">
              <label class="form-check-label">비프</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="pattyType" value="기타">
              <label class="form-check-label">기타</label>
            </div>
          </div>

          <div class="mb-4">
            <label class="form-label">이미지 업로드</label>
            <input type="file" name="imagePath" class="form-control" accept="image/*">
          </div>

          <hr>

          <h5>버거 상세</h5>
          <div class="row g-2">
            <div class="col-6"><input type="number" name="calories" class="form-control" placeholder="칼로리(kcal)" min="0"></div>
            <div class="col-6"><input type="number" name="carbohydrates" class="form-control" placeholder="탄수화물(g)" min="0"></div>
            <div class="col-6"><input type="number" name="protein" class="form-control" placeholder="단백질(g)" min="0"></div>
            <div class="col-6"><input type="number" name="fat" class="form-control" placeholder="지방(g)" min="0"></div>
            <div class="col-6"><input type="number" name="sodium" class="form-control" placeholder="나트륨(mg)" min="0"></div>
            <div class="col-6"><input type="number" name="sugar" class="form-control" placeholder="당류(g)" min="0"></div>
          </div>

          <hr>

          <h5>알레르기 유발 정보</h5>
          <div class="row">
            <div class="col-6">
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="우유"><label class="form-check-label">우유</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="대두"><label class="form-check-label">대두</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="밀"><label class="form-check-label">밀</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="토마토"><label class="form-check-label">토마토</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="난류"><label class="form-check-label">난류</label></div>
            </div>
            <div class="col-6">
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="닭고기"><label class="form-check-label">닭고기</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="돼지고기"><label class="form-check-label">돼지고기</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="쇠고기"><label class="form-check-label">쇠고기</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="새우"><label class="form-check-label">새우</label></div>
              <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="굴"><label class="form-check-label">굴</label></div>
            </div>
          </div>

          <button type="submit" class="btn-submit mt-4">등록하기</button>
        </form>
      </div>
    </div>
  </div>

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
