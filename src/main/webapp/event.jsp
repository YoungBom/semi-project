<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Burger Dodge 🍔</title>

  <!-- ✅ 외부 CSS / 폰트 / 아이콘 로드 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">

  <!-- ✅ 별도 CSS 파일 (캔버스 배경 / border 등 정의) -->
  <link href="${pageContext.request.contextPath}/resources/css/event.css" rel="stylesheet">
</head>

<body>
  <%@ include file="/include/header.jsp" %> <!-- 상단 공통 헤더 포함 -->

  <main class="container text-center mt-4">
    <h2 class="fw-bold">🍔 버거 피하기</h2>
    <p class="text-muted"> 방향키(또는 WASD)로 이동하세요! 사방에서 날아오며 점점 빨라지는 버거를 피해 살아남으세요. (현재 최고기록 72초)</p>
    
    <!-- 🎮 게임 영역 (canvas) -->
    <canvas id="gameCanvas" width="800" height="500"></canvas>

    <!-- 📊 HUD (게임 정보 영역: 캔버스 아래 표시) -->
    <div class="hud">
      ⏱ 시간: <span id="time">0</span>s　
      ⭐ 점수: <span id="score">0</span>
    </div>

    <!-- ▶️ 게임 시작 버튼 -->
    <div class="controls">
      <button id="btnStart" class="btn btn-primary px-4">게임 시작</button>
    </div>

    <!-- 🧾 게임 종료 후 결과 카드 -->
    <div id="result-card" class="card text-center">
      <div class="card-body">
        <h5 class="card-title mb-2">게임 종료!</h5>
        <p class="mb-2">최종 점수: <strong id="finalScore">0</strong></p>
        <button id="btnRestart" class="btn btn-outline-danger mt-2 px-4">다시하기</button>
      </div>
    </div>
  </main>

  <%@ include file="/include/footer.jsp" %> <!-- 하단 공통 푸터 포함 -->

  <!-- ===========================
        🎮 JavaScript: Burger Dodge Game
       =========================== -->
  <script>
  (() => {
    /* ---------------------------------
       🔹 전역 변수 선언
       --------------------------------- */
    const ctxPath = "<%=contextPath%>"; // JSP contextPath (이미지 경로용)
    const canvas = document.getElementById("gameCanvas");
    const ctx = canvas.getContext("2d");

    // 주요 DOM 요소
    const btnStart = document.getElementById("btnStart");
    const btnRestart = document.getElementById("btnRestart");
    const timeEl = document.getElementById("time");
    const scoreEl = document.getElementById("score");
    const finalScoreEl = document.getElementById("finalScore");
    const resultCard = document.getElementById("result-card");

    // 게임 관련 변수
    let player, burgers, score, seconds, playing;
    let spawnTimer, gameTimer;
    let burgerSpeed = 2;      // 기본 버거 이동 속도
    let spawnInterval = 600;  // 버거 생성 간격(ms)
    const keys = {};          // 키 입력 상태 저장
    let imagesLoaded = 0;     // 이미지 로드 확인용

    /* ---------------------------------
       🔹 이미지 로드
       --------------------------------- */
    const playerImg = new Image();
    const burgerImg = new Image();
    playerImg.src = ctxPath + "/img/player.jpg";          // 플레이어 이미지
    burgerImg.src = ctxPath + "/img/cheese_whopper.jpg";  // 버거 이미지

    [playerImg, burgerImg].forEach(img => {
      img.onload = () => {
        imagesLoaded++;
        if (imagesLoaded === 2) reset(); // 둘 다 로드 완료 시 초기화
      };
    });

    /* ---------------------------------
       🔹 초기화 함수 (게임 시작 전 상태)
       --------------------------------- */
    function reset() {
      const size = 40; // ✅ 플레이어 크기 (40x40)
      player = { 
        x: canvas.width / 2 - size / 2, 
        y: canvas.height / 2 - size / 2, 
        w: size, 
        h: size, 
        speed: 6 
      };
      burgers = [];
      score = 0;
      seconds = 0;
      playing = false;
      burgerSpeed = 2;
      spawnInterval = 600;

      clearInterval(spawnTimer);
      clearInterval(gameTimer);
      resultCard.style.display = "none";

      timeEl.textContent = 0;
      scoreEl.textContent = 0;

      drawPlayer(); // 초기 캐릭터 표시
    }

    /* ---------------------------------
       🔹 플레이어 그리기
       --------------------------------- */
    function drawPlayer() {
      if (playerImg.complete)
        ctx.drawImage(playerImg, player.x, player.y, player.w, player.h);
      else {
        ctx.fillStyle = "#ff6600";
        ctx.fillRect(player.x, player.y, player.w, player.h);
      }
    }

    /* ---------------------------------
       🔹 버거 생성 (랜덤 위치 + 방향)
       --------------------------------- */
    function spawnBurger() {
      const size = 50;
      const edge = Math.floor(Math.random() * 4); // 0~3 방향 랜덤
      const speed = burgerSpeed + Math.random() * 2; // 속도 랜덤
      let x, y, vx, vy;

      // 4방향 중 랜덤 진입
      switch(edge) {
        case 0: x = Math.random() * (canvas.width - size); y = -size; vx = (player.x - x) / 100; vy = speed; break; // 위쪽
        case 1: x = Math.random() * (canvas.width - size); y = canvas.height + size; vx = (player.x - x) / 100; vy = -speed; break; // 아래쪽
        case 2: x = -size; y = Math.random() * (canvas.height - size); vx = speed; vy = (player.y - y) / 100; break; // 왼쪽
        case 3: x = canvas.width + size; y = Math.random() * (canvas.height - size); vx = -speed; vy = (player.y - y) / 100; break; // 오른쪽
      }

      burgers.push({ x, y, w:size, h:size, vx, vy });
    }

    /* ---------------------------------
       🔹 충돌 판정 (AABB 방식)
       --------------------------------- */
    function isColliding(a, b) {
      return a.x < b.x + b.w && a.x + a.w > b.x &&
             a.y < b.y + b.h && a.y + a.h > b.y;
    }

    /* ---------------------------------
       🔹 매 프레임마다 실행되는 draw() 루프
       --------------------------------- */
    function draw() {
      if (!playing) return;

      // 배경 초기화
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.fillStyle = "#fffaf0";
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      // 이동 처리
      if (keys["ArrowLeft"] || keys["a"]) player.x -= player.speed;
      if (keys["ArrowRight"] || keys["d"]) player.x += player.speed;
      if (keys["ArrowUp"] || keys["w"]) player.y -= player.speed;
      if (keys["ArrowDown"] || keys["s"]) player.y += player.speed;

      // 화면 경계 제한
      player.x = Math.max(0, Math.min(canvas.width - player.w, player.x));
      player.y = Math.max(0, Math.min(canvas.height - player.h, player.y));

      // 버거 이동 및 충돌 체크
      for (let i = burgers.length - 1; i >= 0; i--) {
        const b = burgers[i];
        b.x += b.vx * 0.5;
        b.y += b.vy * 0.5;
        ctx.drawImage(burgerImg, b.x, b.y, b.w, b.h);

        if (isColliding(player, b)) {
          endGame();
          return;
        }

        // 화면 밖 버거 제거
        if (b.x < -100 || b.x > canvas.width + 100 || b.y < -100 || b.y > canvas.height + 100)
          burgers.splice(i, 1);
      }

      drawPlayer();
      requestAnimationFrame(draw); // 다음 프레임 호출
    }

    /* ---------------------------------
       🔹 게임 시작
       --------------------------------- */
    function startGame() {
      if (imagesLoaded < 2) {
        alert("이미지를 불러오는 중입니다. 잠시만 기다려주세요!");
        return;
      }

      reset(); // 초기화
      playing = true;
      btnStart.disabled = true;

      // 버거 생성 타이머
      spawnTimer = setInterval(spawnBurger, spawnInterval);

      // 점수/시간/난이도 관리 타이머
      gameTimer = setInterval(() => {
        if (!playing) return;

        seconds++;
        score++;
        timeEl.textContent = seconds;
        scoreEl.textContent = score;

        // 5초마다 속도 증가
        if (seconds % 5 === 0 && burgerSpeed < 10) burgerSpeed += 1;

        // 10초마다 생성 주기 단축
        if (seconds % 10 === 0 && spawnInterval > 300) {
          clearInterval(spawnTimer);
          spawnInterval -= 50;
          spawnTimer = setInterval(spawnBurger, spawnInterval);
        }
      }, 1000);

      draw(); // 메인 루프 시작
    }

    /* ---------------------------------
       🔹 게임 종료
       --------------------------------- */
    function endGame() {
      playing = false;
      clearInterval(spawnTimer);
      clearInterval(gameTimer);
      finalScoreEl.textContent = score;
      resultCard.style.display = "block";
      btnStart.disabled = false;
    }

    /* ---------------------------------
       🔹 키보드 입력 처리 + 스크롤 방지
       --------------------------------- */
    document.addEventListener("keydown", e => {
      if (["ArrowUp","ArrowDown","ArrowLeft","ArrowRight"," ","w","a","s","d"].includes(e.key)) {
        e.preventDefault(); // 스크롤 방지
      }
      keys[e.key] = true;
    });

    document.addEventListener("keyup", e => { keys[e.key] = false; });

    // 버튼 이벤트 연결
    btnStart.addEventListener("click", startGame);
    btnRestart.addEventListener("click", startGame);
  })();
  </script>
</body>
</html>
