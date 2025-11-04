// /resources/js/filter.js
document.addEventListener("DOMContentLoaded", function () {
  const btns = document.querySelectorAll(".filter-btn");
  // 카드(.burger-card)와 그 격자 칼럼(.col-*)를 같이 잡아둠
  const cards = document.querySelectorAll(".burger-card");

  function normalize(s) {
    return (s || "").trim();
  }

  function cardPatty(card) {
    // 우선 data-patty 우선
    const fromData = normalize(card.getAttribute("data-patty"));
    if (fromData) return fromData;

    // data-가 없으면 카드 내부 텍스트에서 백업으로 추출
    const txtEl = card.querySelector(".card-text");
    return normalize(txtEl ? txtEl.textContent : "");
  }

  function showColOf(card) {
    // .burger-card 의 부모가 곧 .col-12 col-sm-6 ... 칼럼
    const col = card.parentElement;
    if (col) col.style.display = "";
  }

  function hideColOf(card) {
    const col = card.parentElement;
    if (col) col.style.display = "none";
  }

  btns.forEach((btn) => {
    btn.addEventListener("click", () => {
      // 버튼 active 토글
      btns.forEach((b) => b.classList.remove("active"));
      btn.classList.add("active");

      const type = btn.getAttribute("data-type"); // all | 비프 | 치킨 | 기타

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
