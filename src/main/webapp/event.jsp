<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  // JSP 내장 객체 request에서 현재 프로젝트 경로를 가져옴 (ex: /semi-project)
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Burger Dodge 🍔</title>

  <!-- ✅ 외부 CSS / 아이콘 / 폰트 로드 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <!-- 프로젝트 내부 CSS 파일 (캔버스 배경, 버튼 정렬 등) -->
  <link href="${pageContext.request.contextPath}/resources/css/event.css" rel="stylesheet">
</head>

<body>
  <%@ include file="/include/header.jsp" %>  <!-- 상단 공통 헤더 포함 -->

  <main class="container text-center mt-4">
    <!-- 게임 제목 및 설명 -->
    <h2 class="fw-bold">🍔 버거 피하기</h2>
    <p class="text-muted">방향키(또는 WASD)로 이동하세요! 사방에서 날아오는 버거를 피해 살아남으세요. (히트박스 보고싶으면 DEBUG_HITBOX = false ㅡ> true로 전환)</p>

    <!-- 🎮 게임 캔버스 (게임 실행 영역) -->
    <canvas id="gameCanvas" width="800" height="720"></canvas>

    <!-- 📊 HUD: 게임 시간 / 점수 표시 -->
    <div class="hud">
      ⏱ 시간: <span id="time">0</span>s　
      ⭐ 점수: <span id="score">0</span>
    </div>

    <!-- 🎮 버튼 영역 -->
    <div class="controls" id="gameControls">
      <button id="btnStart" class="btn btn-primary px-4">게임 시작 (스페이스바)</button>
      <button id="btnReset" class="btn btn-outline-secondary ms-2">기록 초기화</button>
    </div>

    <!-- 🧾 게임 종료 시 결과 카드 -->
    <div id="result-card" class="card text-center">
      <div class="card-body">
        <h5 class="card-title mb-2">게임 종료!</h5>
        <p class="mb-2">최종 점수: <strong id="finalScore">0</strong></p>
        <button id="btnRestart" class="btn btn-outline-danger mt-2 px-4">다시하기 <br>(스페이스바)</button>
      </div>
    </div>
  </main>

  <%@ include file="/include/footer.jsp" %> <!-- 하단 공통 푸터 포함 -->

  <!-- ==============================
        🎮 게임 자바스크립트 로직
  =============================== -->
  <script>
  (() => {
    // ✅ JSP에서 전달받은 context path (이미지 로드용)
    const ctxPath = "<%=contextPath%>";

    // 🎮 캔버스 및 컨텍스트 가져오기
    const canvas = document.getElementById("gameCanvas");
    const ctx = canvas.getContext("2d");

    // ✅ 주요 DOM 요소 선택
    const btnStart = document.getElementById("btnStart");
    const btnRestart = document.getElementById("btnRestart");
    const btnReset = document.getElementById("btnReset");
    const timeEl = document.getElementById("time");
    const scoreEl = document.getElementById("score");
    const finalScoreEl = document.getElementById("finalScore");
    const resultCard = document.getElementById("result-card");

    // ✅ 게임 상태 관련 변수들
    let player, burgers, score, seconds, playing;
    let spawnTimer, gameTimer;         // 버거 생성 타이머, 게임 시간 타이머
    let burgerSpeed = 2, spawnInterval = 600; // 버거 속도, 생성 주기(ms)
    const keys = {};                   // 키 입력 상태 저장
    let imagesLoaded = 0;              // 이미지 로드 상태 카운트

    // ✅ 이미지 로드
    const playerImg = new Image();
    const burgerImg = new Image();
    playerImg.src = ctxPath + "/img/player.jpg";
    burgerImg.src = ctxPath + "/img/lot_teri.jpg";

    // 두 이미지 모두 로드 완료되면 reset() 실행
    [playerImg, burgerImg].forEach(img => {
      img.onload = () => { 
        imagesLoaded++; 
        if (imagesLoaded === 2) reset(); 
      };
    });

    // 🔄 초기화 함수
    function reset() {
      const size = 30;
      player = { x: canvas.width/2 - size/2, y: canvas.height/2 - size/2, w: size, h: size, speed: 6 };
      burgers = [];
      score = 0; seconds = 0; playing = false;
      burgerSpeed = 2; spawnInterval = 600;
      clearInterval(spawnTimer); clearInterval(gameTimer);
      resultCard.style.display = "none";
      timeEl.textContent = 0; scoreEl.textContent = 0;
      drawPlayer(); // 초기 플레이어 표시
    }

    // 👤 플레이어 그리기
    function drawPlayer() {
      if (playerImg.complete)
        ctx.drawImage(playerImg, player.x, player.y, player.w, player.h);
      else {
        // 로딩 실패시 대체 사각형 표시
        ctx.fillStyle = "#ff6600"; 
        ctx.fillRect(player.x, player.y, player.w, player.h); 
      }
    }

    // 🍔 버거 생성 (랜덤 방향)
    function spawnBurger() {
      const size = 40;
      const edge = Math.floor(Math.random() * 4);  // 상하좌우 랜덤
      const speed = burgerSpeed + Math.random() * 2; // 약간 랜덤 가속
      let x, y;

      // 버거 생성 위치 결정
      switch (edge) {
        case 0: x = Math.random() * (canvas.width - size); y = -size; break;           // 위
        case 1: x = Math.random() * (canvas.width - size); y = canvas.height + size; break; // 아래
        case 2: x = -size; y = Math.random() * (canvas.height - size); break;          // 왼쪽
        case 3: x = canvas.width + size; y = Math.random() * (canvas.height - size); break; // 오른쪽
      }

      // 플레이어 방향으로 이동하는 속도 벡터 계산
      const dx = (player.x + player.w/2) - (x + size/2);
      const dy = (player.y + player.h/2) - (y + size/2);
      const len = Math.sqrt(dx*dx + dy*dy);
      const vx = (dx / len) * speed, vy = (dy / len) * speed;

      // burgers 배열에 추가
      burgers.push({ x, y, w: size, h: size, vx, vy });
    }

    // 💥 충돌 판정 (살짝 축소된 박스로 계산)
    let DEBUG_HITBOX = false; // true면 보이고, false면 안보임
    function isColliding(a, b) {
    
      // 보이는 히트박스보다 여유로운 판정을 위해 1(원본)에서 줄임
      const shrinkA = 0.8, shrinkB = 0.9;
      
      const aw = a.w * shrinkA, ah = a.h * shrinkA, bw = b.w * shrinkB, bh = b.h * shrinkB;
      const ax = a.x + (a.w - aw)/2, ay = a.y + (a.h - ah)/2;
      const bx = b.x + (b.w - bw)/2, by = b.y + (b.h - bh)/2;
      return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
    }

    // 🎨 메인 게임 루프
    function draw() {
      if (!playing) return;

      // 배경 초기화
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.fillStyle = "#fffaf0";
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      // 방향키 입력 처리
      if (keys["ArrowLeft"] || keys["a"]) player.x -= player.speed;
      if (keys["ArrowRight"] || keys["d"]) player.x += player.speed;
      if (keys["ArrowUp"] || keys["w"]) player.y -= player.speed;
      if (keys["ArrowDown"] || keys["s"]) player.y += player.speed;

      // 경계 밖 이동 방지
      player.x = Math.max(0, Math.min(canvas.width - player.w, player.x));
      player.y = Math.max(0, Math.min(canvas.height - player.h, player.y));

      // 버거 이동 및 충돌 검사
      for (let i = burgers.length - 1; i >= 0; i--) {
        const b = burgers[i];
        b.x += b.vx * 0.5;
        b.y += b.vy * 0.5;
        ctx.drawImage(burgerImg, b.x, b.y, b.w, b.h);

        if (isColliding(player, b)) { endGame(); return; } // 충돌 시 종료
        if (b.x < -100 || b.x > canvas.width + 100 || b.y < -100 || b.y > canvas.height + 100)
          burgers.splice(i, 1); // 화면 밖으로 나간 버거 제거
      }

      drawPlayer();
	   // ===============================
	   // 🔍 히트박스 그리기 (DEBUG 모드)
	   // ===============================
		   
      if (DEBUG_HITBOX) {
    	    // 플레이어 히트박스
    	    ctx.strokeStyle = "red";
    	    ctx.strokeRect(player.x, player.y, player.w, player.h);

    	    // 버거 히트박스
    	    burgers.forEach(b => {
    	        ctx.strokeStyle = "blue";
    	        ctx.strokeRect(b.x, b.y, b.w, b.h);
    	    });
    	}
	   
      requestAnimationFrame(draw); // 다음 프레임 호출
    }

    // ▶️ 게임 시작
    function startGame() {
      if (imagesLoaded < 2) { alert("이미지를 불러오는 중입니다."); return; }
      reset(); playing = true; btnStart.disabled = true;

      // 일정 주기마다 버거 생성
      spawnTimer = setInterval(spawnBurger, spawnInterval);

      // 1초마다 시간/점수 증가 및 난이도 조정
      gameTimer = setInterval(() => {
        if (!playing) return;
        seconds++; score++;
        timeEl.textContent = seconds; 
        scoreEl.textContent = score;

        // 5초마다 버거 속도 증가
        if (seconds % 5 === 0 && burgerSpeed < 10) burgerSpeed += 1;

        // 10초마다 버거 생성속도 증가 (간격 감소)
        if (seconds % 10 === 0 && spawnInterval > 300) {
          clearInterval(spawnTimer);
          spawnInterval -= 50;
          spawnTimer = setInterval(spawnBurger, spawnInterval);
        }
      }, 1000);

      draw(); // 게임 루프 시작
    }

    // 🛑 게임 종료
    function endGame() {
      playing = false;
      clearInterval(spawnTimer);
      clearInterval(gameTimer);
      finalScoreEl.textContent = score;
      resultCard.style.display = "block";
      btnStart.disabled = false;
      updateHighScore(score); // 최고기록 갱신
    }

    // 🎮 키 입력 처리
    document.addEventListener("keydown", e => {
      const key = e.key.toLowerCase();
      // 스페이스/방향키 입력 시 페이지 스크롤 방지
      if (["arrowup","arrowdown","arrowleft","arrowright"," ","w","a","s","d"].includes(key)) e.preventDefault();
      // 스페이스바로 게임 시작 가능
      if (key === " " && !playing) startGame();
      keys[e.key] = true;
    });
    document.addEventListener("keyup", e => { keys[e.key] = false; });

    // 버튼 클릭 이벤트
    btnStart.addEventListener("click", startGame);
    btnRestart.addEventListener("click", startGame);

  })();

  // ===============================
  // 🏆 최고기록(localStorage) 관리
  // ===============================
  function updateHighScore(currentScore) {
    const highScore = parseInt(localStorage.getItem("burger_high_score") || "0", 10);
    if (currentScore > highScore) localStorage.setItem("burger_high_score", currentScore);
    showHighScore();
  }

  // 최고기록 표시
  function showHighScore() {
    const controls = document.querySelector(".controls");
    if (!controls) return;

    // 기록 없으면 0으로 초기화
    if (localStorage.getItem("burger_high_score") === null)
      localStorage.setItem("burger_high_score", "0");

    // 기존 p 요소가 없으면 새로 생성
    let highScoreEl = document.getElementById("highScoreText");
    if (!highScoreEl) {
      highScoreEl = document.createElement("p");
      highScoreEl.id = "highScoreText";
      highScoreEl.className = "text-muted mt-2";
      controls.appendChild(highScoreEl);
    }

    const highScore = parseInt(localStorage.getItem("burger_high_score") || "0", 10);
    highScoreEl.innerHTML = `🏆 <strong>내 최고기록:</strong> \${highScore}`;
  }

  document.addEventListener("DOMContentLoaded", () => {
	  // 기록 초기화 버튼
	  const resetBtn = document.getElementById("btnReset");
	  resetBtn.addEventListener("click", () => {
	    if (confirm("정말 최고기록을 초기화할까요?")) {
	      localStorage.removeItem("burger_high_score");
	      showHighScore();
	    }
	  });

	  // 페이지 로드 시 최고기록 표시
	  showHighScore();
	});
  </script>
</body>
</html>
