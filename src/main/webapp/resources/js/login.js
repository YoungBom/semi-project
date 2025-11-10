(() => {
  const $ = (s, p = document) => p.querySelector(s);

  const CTX = window.CTX || '';
  const form = $('form[action$="/login"]') || $('form[data-login="true"]');
  if (!form) return;

  const userId = $('#user_id');
  const userPw = $('#user_pw');
  const remember = $('#remember_me');
  const btn = form.querySelector('button[type="submit"]');
  const err = $('#loginStatus'); // 선택: 에러 메시지 영역(없어도 무방)

  // 아이디는 항상 소문자
  userId && userId.addEventListener('input', () => {
    const v = userId.value || '';
    const lower = v.toLowerCase();
    if (v !== lower) userId.value = lower;
  });

  // 엔터키로 제출 시 중복 클릭 방지
  form.addEventListener('submit', (e) => {
    // 최종 소문자 보정
    if (userId) userId.value = (userId.value || '').trim().toLowerCase();

    // 기본 유효성
    if (!userId || !userId.value || !userPw || !userPw.value) {
      if (err) { err.textContent = '아이디와 비밀번호를 입력하세요.'; err.className = 'hint bad'; }
      e.preventDefault();
      return;
    }

    if (btn) {
      btn.disabled = true;
      setTimeout(() => (btn.disabled = false), 3000); // 3초 후 자동 해제(안전장치)
    }
    // remember_me 체크박스는 폼으로 그대로 제출 → 백엔드에서 처리
  });

  // 비밀번호 보기 토글(원하면 사용)
  const toggle = $('#togglePw');
  if (toggle && userPw) {
    toggle.addEventListener('click', () => {
      userPw.type = userPw.type === 'password' ? 'text' : 'password';
    });
  }
})();
