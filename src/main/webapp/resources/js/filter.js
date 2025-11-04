document.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("burgerContainer");
  const buttons = document.querySelectorAll(".filter-btn");
  const base = window.location.origin + "/semi-project";

  console.log("✅ base:", base);

  fetchBurgers("all");

  buttons.forEach(btn => {
    btn.addEventListener("click", () => {
      buttons.forEach(b => b.classList.remove("active"));
      btn.classList.add("active");
      const type = btn.dataset.type;
      fetchBurgers(type);
    });
  });

  async function fetchBurgers(type) {
    container.innerHTML = "<p class='text-center mt-5 text-muted'>로딩 중...</p>";
    try {
      const res = await fetch(base + "/filter", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "patty=" + encodeURIComponent(type)
      });

      const burgers = await res.json();
      if (!burgers || burgers.length === 0) {
        container.innerHTML = "<p class='text-center text-muted mt-5'>결과 없음 😢</p>";
        return;w
      }
      renderBurgers(burgers);
    } catch (err) {
      console.error("❌ 필터링 오류:", err);
      container.innerHTML = "<p class='text-center text-danger mt-5'>오류 발생 ❌</p>";
    }
  }

  function renderBurgers(burgers) {
    console.log("🎨 렌더링 데이터 확인:", burgers[0]);
    container.innerHTML = burgers.map(b => `
      <div class="col-md-3 col-sm-6">
        <div class="card burger-card">
          <div class="card-body">
            <span class="badge bg-warning text-dark mb-2">${b.brand || "-"}</span>
            <h5 class="card-title">${b.name || "-"}</h5>
            <p class="card-text">${b.pattyType || "-"}</p>
            <div class="d-flex justify-content-between align-items-center mt-2">
              <span class="price">${b.price || "-"} 원</span>
              <span class="rating">⭐</span>
            </div>
          </div>
        </div>
      </div>
    `).join("");
  }
});
