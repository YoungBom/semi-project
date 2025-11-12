<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>Burger Dodge 🍔</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resources/css/event.css" rel="stylesheet">
</head>

<body>
  <%@ include file="/include/header.jsp" %>

  <main class="container text-center mt-4">
    <h2 class="fw-bold">🍔 버거 피하기</h2>
    <p class="text-muted">방향키(또는 WASD)로 이동하세요! 사방에서 날아오며 점점 빨라지는 버거를 피해 살아남으세요.(저는 최고기록 46나옴 ㅎㅎ)</p>

    <canvas id="gameCanvas" width="800" height="500"></canvas>

    <!-- ✅ HUD (캔버스 아래 표시) -->
    <div class="hud">
      ⏱ 시간: <span id="time">0</span>s　
      ⭐ 점수: <span id="score">0</span>
    </div>

    <div class="controls">
      <button id="btnStart" class="btn btn-primary px-4">게임 시작</button>
    </div>

    <!-- ✅ 게임 종료 카드 -->
    <div id="result-card" class="card text-center">
      <div class="card-body">
        <h5 class="card-title mb-2">게임 종료!</h5>
        <p class="mb-2">최종 점수: <strong id="finalScore">0</strong></p>
        <button id="btnRestart" class="btn btn-outline-danger mt-2 px-4">다시하기</button>
      </div>
    </div>
  </main>

  <%@ include file="/include/footer.jsp" %>

  <script>
  (() => {
    const ctxPath = "<%=contextPath%>";
    const canvas = document.getElementById("gameCanvas");
    const ctx = canvas.getContext("2d");
    const btnStart = document.getElementById("btnStart");
    const btnRestart = document.getElementById("btnRestart");
    const timeEl = document.getElementById("time");
    const scoreEl = document.getElementById("score");
    const finalScoreEl = document.getElementById("finalScore");
    const resultCard = document.getElementById("result-card");

    let player, burgers, score, seconds, playing;
    let spawnTimer, gameTimer;
    let burgerSpeed = 2;           // 🔹 기본 버거 속도
    let spawnInterval = 600;       // 🔹 기본 생성 주기(ms)
    const keys = {};
    let imagesLoaded = 0;

    const playerImg = new Image();
    const burgerImg = new Image();
    playerImg.src = ctxPath + "/img/player.jpg";
    burgerImg.src = ctxPath + "/img/cheese_whopper.jpg";

    [playerImg, burgerImg].forEach(img => {
      img.onload = () => {
        imagesLoaded++;
        if (imagesLoaded === 2) reset();
      };
    });

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
      drawPlayer();
    }

    function drawPlayer() {
      if (playerImg.complete)
        ctx.drawImage(playerImg, player.x, player.y, player.w, player.h);
      else {
        ctx.fillStyle = "#ff6600";
        ctx.fillRect(player.x, player.y, player.w, player.h);
      }
    }

    function spawnBurger() {
      const size = 50;
      const edge = Math.floor(Math.random() * 4);
      const speed = burgerSpeed + Math.random() * 2; // 🔹 속도는 점점 빨라짐
      let x, y, vx, vy;

      switch(edge) {
        case 0: x = Math.random() * (canvas.width - size); y = -size; vx = (player.x - x) / 100; vy = speed; break;
        case 1: x = Math.random() * (canvas.width - size); y = canvas.height + size; vx = (player.x - x) / 100; vy = -speed; break;
        case 2: x = -size; y = Math.random() * (canvas.height - size); vx = speed; vy = (player.y - y) / 100; break;
        case 3: x = canvas.width + size; y = Math.random() * (canvas.height - size); vx = -speed; vy = (player.y - y) / 100; break;
      }

      burgers.push({ x, y, w:size, h:size, vx, vy });
    }

    function isColliding(a,b) {
      return a.x < b.x+b.w && a.x+a.w > b.x && a.y < b.y+b.h && a.y+a.h > b.y;
    }

    function draw() {
      if (!playing) return;
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.fillStyle = "#fffaf0";
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      if (keys["ArrowLeft"] || keys["a"]) player.x -= player.speed;
      if (keys["ArrowRight"] || keys["d"]) player.x += player.speed;
      if (keys["ArrowUp"] || keys["w"]) player.y -= player.speed;
      if (keys["ArrowDown"] || keys["s"]) player.y += player.speed;

      player.x = Math.max(0, Math.min(canvas.width - player.w, player.x));
      player.y = Math.max(0, Math.min(canvas.height - player.h, player.y));

      for (let i = burgers.length - 1; i >= 0; i--) {
        const b = burgers[i];
        b.x += b.vx * 0.5;
        b.y += b.vy * 0.5;
        ctx.drawImage(burgerImg, b.x, b.y, b.w, b.h);
        if (isColliding(player, b)) {
          endGame();
          return;
        }
        if (b.x < -100 || b.x > canvas.width + 100 || b.y < -100 || b.y > canvas.height + 100)
          burgers.splice(i, 1);
      }

      drawPlayer();
      requestAnimationFrame(draw);
    }

    function startGame() {
      if (imagesLoaded < 2) {
        alert("이미지를 불러오는 중입니다. 잠시만 기다려주세요!");
        return;
      }

      reset();
      playing = true;
      btnStart.disabled = true;

      // 🔹 버거 생성 타이머
      spawnTimer = setInterval(spawnBurger, spawnInterval);

      // 🔹 점수/시간 증가 + 난이도 상승
      gameTimer = setInterval(() => {
        if (!playing) return;

        seconds++;
        score++;
        timeEl.textContent = seconds;
        scoreEl.textContent = score;

        // 5초마다 속도 증가
        if (seconds % 5 === 0 && burgerSpeed < 10) {
          burgerSpeed += 1;
        }

        // 10초마다 생성 속도 단축 (최소 300ms)
        if (seconds % 10 === 0 && spawnInterval > 300) {
          clearInterval(spawnTimer);
          spawnInterval -= 50;
          spawnTimer = setInterval(spawnBurger, spawnInterval);
        }
      }, 1000);

      draw();
    }

    function endGame() {
      playing = false;
      clearInterval(spawnTimer);
      clearInterval(gameTimer);
      finalScoreEl.textContent = score;
      resultCard.style.display = "block";
      btnStart.disabled = false;
    }

    document.addEventListener("keydown", e => {
      if (["ArrowUp","ArrowDown","ArrowLeft","ArrowRight"," ","w","a","s","d"].includes(e.key)) {
        e.preventDefault();
      }
      keys[e.key] = true;
    });

    document.addEventListener("keyup", e => { keys[e.key] = false; });

    btnStart.addEventListener("click", startGame);
    btnRestart.addEventListener("click", startGame);
  })();
  </script>
</body>
</html>
