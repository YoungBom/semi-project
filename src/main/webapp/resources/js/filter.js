// /resources/js/filter.js
document.addEventListener("DOMContentLoaded", function () {
  const btns = document.querySelectorAll(".filter-btn");
  const cards = document.querySelectorAll(".burger-card");

  // 공백 제거 헬퍼
  function normalize(s) {
    return (s || "").trim();
  }

  // 카드의 패티 타입을 읽는 함수
  function cardPatty(card) {
    // 1️⃣ data-patty 속성이 있으면 우선 사용
    const fromData = normalize(card.getAttribute("data-patty"));
    if (fromData) return fromData;

    // 2️⃣ .patty-badge 엘리먼트에서 추출 (비프/치킨/기타)
    const badge = card.querySelector(".patty-badge");
    if (badge) return normalize(badge.textContent);

    // 3️⃣ 기존 백업 (card-text에서 읽기)
    const txtEl = card.querySelector(".card-text");
    return normalize(txtEl ? txtEl.textContent : "");
  }

  // 카드의 부모 컬럼 표시 / 숨기기
  function showColOf(card) {
    const col = card.closest(".col-12, .col-md-3, .col-sm-6, .col-lg-3");
    if (col) col.style.display = "";
  }

  function hideColOf(card) {
    const col = card.closest(".col-12, .col-md-3, .col-sm-6, .col-lg-3");
    if (col) col.style.display = "none";
  }

  // 필터 버튼 클릭 이벤트
  btns.forEach((btn) => {
    btn.addEventListener("click", () => {
      // 버튼 active 상태 갱신
      btns.forEach((b) => b.classList.remove("active", "btn-warning"));
      btns.forEach((b) => b.classList.add("btn-outline-warning"));
      btn.classList.add("active", "btn-warning");
      btn.classList.remove("btn-outline-warning");

      const type = btn.getAttribute("data-type"); // all | 비프 | 치킨 | 기타

      // 각 카드에 대해 필터링
      cards.forEach((card) => {
        const patty = cardPatty(card);
        if (type === "all" || patty === type) {
          showColOf(card);
        } else {
          hideColOf(card);
        }
      });
    });
  });
});
