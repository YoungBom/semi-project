<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>버거 등록 🍔</title>

  <!-- ✅ Bootstrap & Fonts -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

  <!-- ✅ 공통 스타일 -->
  <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

  <style>
    /* ✅ 스코프 분리 */
    .burger-add-page {
      background-color: #f9fafb;
      font-family: 'Poppins', sans-serif;
      color: #444;
      min-height: 100vh;
      padding-top: 40px;
    }

    /* ✅ 페이지 전용 스타일은 모두 .burger-add-page 아래에만 적용 */
    .burger-add-page .page-header {
      background: linear-gradient(135deg, #ff922b, #ffa94d);
      color: #fff;
      padding: 2.5rem 0;
      text-align: center;
      border-radius: 0 0 20px 20px;
      box-shadow: 0 3px 10px rgba(0,0,0,0.08);
      position: relative;
      overflow: hidden;
    }
    .burger-add-page .page-header::after {
      content: "";
      position: absolute;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(255,255,255,0.1);
      mix-blend-mode: overlay;
    }
    .burger-add-page .page-header h2 {
      font-weight: 700;
      letter-spacing: -0.5px;
    }
    .burger-add-page .page-header p {
      font-size: 1rem;
      opacity: 0.9;
    }

    .burger-add-page .form-container {
      background: #fff;
      border-radius: 14px;
      box-shadow: 0 4px 16px rgba(0,0,0,0.05);
      padding: 2.5rem 3rem;
      max-width: 700px;
      margin: 3rem auto 6rem auto;
    }

    .burger-add-page label.form-label {
      font-weight: 600;
      color: #444;
    }

    .burger-add-page .form-control,
    .burger-add-page .form-select {
      border-radius: 8px;
      border: 1px solid #ddd;
      padding: 10px 12px;
      transition: all 0.2s ease;
    }

    .burger-add-page .form-control:focus,
    .burger-add-page .form-select:focus {
      border-color: #ff9500;
      box-shadow: 0 0 0 0.2rem rgba(255,149,0,0.25);
    }

    .burger-add-page h5 {
      color: #ff9500;
      font-weight: 700;
      margin-top: 2rem;
    }

    .burger-add-page .btn-submit {
      background-color: #ff6600;
      border: none;
      width: 100%;
      font-weight: 700;
      color: #fff;
      padding: 12px;
      border-radius: 10px;
      transition: background-color 0.2s ease, transform 0.1s;
    }

    .burger-add-page .btn-submit:hover {
      background-color: #e65500;
      transform: translateY(-1px);
    }

    .burger-add-page hr {
      margin: 1.5rem 0;
      border-top: 1px solid #eee;
    }
  </style>
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <!-- ✅ 페이지 스코프 시작 -->
  <div class="burger-add-page">

    <!-- ✅ 상단 헤더 -->
    <div class="page-header mb-5">
      <h2>버거 등록</h2>
      <p class="mb-0">새로운 메뉴를 추가해보세요 🍔</p>
    </div>

    <!-- ✅ 중앙 입력 폼 -->
    <div class="container">
      <div class="form-container">
        <form action="${pageContext.request.contextPath}/burger/add" method="post" enctype="multipart/form-data">

          <!-- 제품명 -->
          <div class="mb-3">
            <label class="form-label">제품명</label>
            <input type="text" name="name" class="form-control" placeholder="예: 불고기버거" required>
          </div>

          <!-- 브랜드 -->
          <div class="mb-3">
            <label class="form-label">브랜드</label>
            <select name="brand" class="form-select" required>
              <option value="" selected disabled>브랜드 선택</option>
              <option value="맥도날드">맥도날드</option>
              <option value="버거킹">버거킹</option>
              <option value="롯데리아">롯데리아</option>
            </select>
          </div>

          <!-- 가격 -->
          <div class="mb-3">
            <label class="form-label">가격</label>
            <input type="number" name="price" class="form-control" placeholder="예: 5500" required>
          </div>

          <!-- 패티 종류 -->
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

          <!-- 이미지 업로드 -->
          <div class="mb-4">
            <label class="form-label">이미지 업로드</label>
            <input type="file" name="imagePath" class="form-control" accept="image/*">
          </div>

          <hr>

          <!-- 상세정보 -->
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

          <!-- 알레르기 -->
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
  <!-- ✅ 스코프 종료 -->

  <%@ include file="/include/footer.jsp" %>
</body>
</html>
