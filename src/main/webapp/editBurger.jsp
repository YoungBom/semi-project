<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>부거 수정</title>

  <!-- ✅ Bootstrap & Google Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ main.css -->
  <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

  <style>
    body {
      background-color: #f8f9fa;
      font-family: 'Poppins', sans-serif;
      color: #444;
    }

    .page-header {
      background: linear-gradient(135deg, #ff922b, #ffa94d);
      color: #fff;
      text-align: center;
      padding: 2rem 0;
      border-radius: 0 0 18px 18px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      margin-bottom: 3rem;
    }

    .page-header h2 {
      font-weight: 600;
      margin-bottom: 0.3rem;
    }

    .form-container {
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.05);
      padding: 2rem 2.5rem;
    }

    h5 {
      color: #ff922b;
      font-weight: 600;
      margin-top: 1.5rem;
    }

    label.form-label {
      font-weight: 500;
      color: #555;
    }

    .form-control,
    .form-select {
      border-radius: 6px;
      border-color: #dee2e6;
    }

    .form-select:focus {
      border-color: #ff922b;
      box-shadow: 0 0 0 0.2rem rgba(255,146,43,0.25);
    }

    .btn-submit {
      background-color: #ff922b;
      border: none;
      width: 100%;
      font-weight: 600;
    }

    .btn-submit:hover {
      background-color: #f08c00;
    }

    .form-check-label {
      color: #555;
    }

    hr {
      margin: 1.5rem 0;
      border-top: 1px solid #e9ecef;
    }
  </style>
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <div class="page-header">
    <h2>버거 수정</h2>
  </div>

  <div class="container mb-5">
    <div class="col-lg-6 col-md-8 mx-auto form-container">
      <form action="${pageContext.request.contextPath}/burger/edit" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${burger.id}">

        <div class="mb-3">
          <label class="form-label">제품명</label>
          <input type="text" name="name" class="form-control" value="${burger.name}">
        </div>

        <div class="mb-3">
          <label class="form-label">브랜드</label>
          <select name="brand" class="form-select" required>
            <option value="" disabled>브랜드를 선택하세요</option>
            <option value="맥도날드" ${burger.brand eq '맥도날드' ? 'selected' : ''}>맥도날드</option>
            <option value="버거킹" ${burger.brand eq '버거킹' ? 'selected' : ''}>버거킹</option>
            <option value="롯데리아" ${burger.brand eq '롯데리아' ? 'selected' : ''}>롯데리아</option>
          </select>
        </div>

        <div class="mb-4">
          <label class="form-label">가격</label>
          <input type="number" name="price" class="form-control" value="${burger.price}">
        </div>

        <div class="mb-3">
          <label class="form-label">패티 종류</label><br>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="pattyType" value="치킨" ${burger.pattyType eq '치킨' ? 'checked' : ''}>
            <label class="form-check-label">치킨</label>
          </div>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="pattyType" value="비프" ${burger.pattyType eq '비프' ? 'checked' : ''}>
            <label class="form-check-label">비프</label>
          </div>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="pattyType" value="기타" ${burger.pattyType eq '기타' ? 'checked' : ''}>
            <label class="form-check-label">기타</label>
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label">이미지 업로드</label>
          <input type="file" name="imagePath" class="form-control" accept="image/*">
        </div>

        <hr>

        <h5>버거 상세</h5>
        <div class="row g-2">
          <div class="col-6"><input type="number" name="calories" class="form-control" value="${burger.details.calories}" placeholder="칼로리" min="0"></div>
          <div class="col-6"><input type="number" name="carbohydrates" class="form-control" value="${burger.details.carbohydrates}" placeholder="탄수화물" min="0"></div>
          <div class="col-6"><input type="number" name="protein" class="form-control" value="${burger.details.protein}" placeholder="단백질" min="0"></div>
          <div class="col-6"><input type="number" name="fat" class="form-control" value="${burger.details.fat}" placeholder="지방" min="0"></div>
          <div class="col-6"><input type="number" name="sodium" class="form-control" value="${burger.details.sodium}" placeholder="나트륨" min="0"></div>
          <div class="col-6"><input type="number" name="sugar" class="form-control" value="${burger.details.sugar}" placeholder="당류" min="0"></div>
        </div>

        <hr>

        <h5>알레르기 유발 정보</h5>
        <div class="row">
          <div class="col-6">
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="우유" ${fn:contains(burger.details.allergyInfo, '우유') ? 'checked' : ''}><label class="form-check-label">우유</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="대두" ${fn:contains(burger.details.allergyInfo, '대두') ? 'checked' : ''}><label class="form-check-label">대두</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="밀" ${fn:contains(burger.details.allergyInfo, '밀') ? 'checked' : ''}><label class="form-check-label">밀</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="토마토" ${fn:contains(burger.details.allergyInfo, '토마토') ? 'checked' : ''}><label class="form-check-label">토마토</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="난류" ${fn:contains(burger.details.allergyInfo, '난류') ? 'checked' : ''}><label class="form-check-label">난류</label></div>
          </div>

          <div class="col-6">
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="닭고기" ${fn:contains(burger.details.allergyInfo, '닭고기') ? 'checked' : ''}><label class="form-check-label">닭고기</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="돼지고기" ${fn:contains(burger.details.allergyInfo, '돼지고기') ? 'checked' : ''}><label class="form-check-label">돼지고기</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="쇠고기" ${fn:contains(burger.details.allergyInfo, '쇠고기') ? 'checked' : ''}><label class="form-check-label">쇠고기</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="새우" ${fn:contains(burger.details.allergyInfo, '새우') ? 'checked' : ''}><label class="form-check-label">새우</label></div>
            <div class="form-check"><input class="form-check-input" type="checkbox" name="allergyInfo" value="굴" ${fn:contains(burger.details.allergyInfo, '굴') ? 'checked' : ''}><label class="form-check-label">굴</label></div>
          </div>
        </div>

        <button type="submit" class="btn btn-submit mt-4 py-2">수정하기</button>
      </form>
    </div>
  </div>

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
