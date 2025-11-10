/**
 * 
 */


(() => {
  const $ = (s, p = document) => p.querySelector(s);

  // ===== 아이디 중복확인 (인라인 메시지) =====
  const userId = $('#user_id');
  const btnCheck = $('#btnCheckId');
  const idChecked = $('#idChecked');
  const idStatus = $('#idStatus');

  // 서버에서 JSTL로 전달받은 체크 URL
  const checkIdUrl = document.body.dataset.checkIdUrl;

  btnCheck.addEventListener('click', async () => {
    const id = (userId.value || '').trim();
    if (!id) {
      idChecked.value = 'false';
      idStatus.textContent = '아이디를 입력해주세요.';
      idStatus.className = 'hint bad';
      return;
    }
    idChecked.value = 'false';
    idStatus.textContent = '확인 중...';
    idStatus.className = 'hint';

    try {
      const res = await fetch(checkIdUrl + '?user_id=' + encodeURIComponent(id), {
        headers: { 'Accept': 'application/json' }
      });
      if (!res.ok) throw new Error('서버 오류');
      const data = await res.json(); // {available:true/false}
      if (data.available) {
        idChecked.value = 'true';
        idStatus.textContent = '사용 가능한 아이디입니다.';
        idStatus.className = 'hint ok';
      } else {
        idChecked.value = 'false';
        idStatus.textContent = '이미 사용중인 아이디입니다.';
        idStatus.className = 'hint bad';
      }
    } catch (e) {
      idChecked.value = 'false';
      idStatus.textContent = '확인 실패. 잠시 후 다시 시도해주세요.';
      idStatus.className = 'hint bad';
    }
  });

  // 아이디가 바뀌면 다시 확인하도록 상태 초기화
  userId.addEventListener('input', () => {
    idChecked.value = 'false';
    idStatus.textContent = '중복확인을 눌러주세요.';
    idStatus.className = 'hint';
  });

  // ===== 비밀번호 규칙/일치 검사 (소문자+숫자 8~20자) =====
  const rePw = /^(?=.*[a-z])(?=.*\d)[a-z0-9]{8,20}$/;
  const pw1 = $('#user_pw');
  const pw2 = $('#user_pw2');
  const pwStatus = $('#pwStatus');

  function validatePw() {
    if (pw1.value !== pw1.value.toLowerCase()) pw1.value = pw1.value.toLowerCase();
    if (pw2.value !== pw2.value.toLowerCase()) pw2.value = pw2.value.toLowerCase();

    if (!rePw.test(pw1.value)) {
      pwStatus.textContent = '조건 불충족: 소문자+숫자 8~20자';
      pwStatus.className = 'hint bad';
      return false;
    }
    if (pw2.value && pw1.value !== pw2.value) {
      pwStatus.textContent = '비밀번호가 일치하지 않습니다.';
      pwStatus.className = 'hint bad';
      return false;
    }
    if (pw1.value && pw2.value && pw1.value === pw2.value) {
      pwStatus.textContent = '사용 가능한 비밀번호입니다.';
      pwStatus.className = 'hint ok';
    } else {
      pwStatus.textContent = '';
      pwStatus.className = 'hint';
    }
    return true;
  }
  pw1.addEventListener('input', validatePw);
  pw2.addEventListener('input', validatePw);

  // ===== 이메일 합치기 + 형식 점검 =====
  const emailLocal = $('#emailLocal');
  const emailDomain = $('#emailDomain');
  const emailCustom = $('#emailCustom');
  const emailHidden = $('#email');
  const emailStatus = $('#emailStatus');

  emailDomain.addEventListener('change', () => {
    const custom = emailDomain.value === 'custom';
    emailCustom.style.display = custom ? 'block' : 'none';
    if (!custom) emailCustom.value = '';
    buildEmail();
  });

  function buildEmail() {
    const local = (emailLocal.value || '').trim();
    let domain = emailDomain.value;
    if (domain === 'custom') domain = (emailCustom.value || '').trim();
    const full = local + domain;
    emailHidden.value = full;
    const ok = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(full);
    emailStatus.textContent = ok ? '' : '이메일 형식이 올바르지 않습니다.';
    emailStatus.className = ok ? 'hint' : 'hint bad';
    return ok;
  }
  emailLocal.addEventListener('input', buildEmail);
  emailDomain.addEventListener('input', buildEmail);
  emailCustom.addEventListener('input', buildEmail);
	
  
  // ===== 휴대폰 간단 검증 =====
  const phone = $('#phone');
  const phoneStatus = $('#phoneStatus');
  function validatePhone() {
    const v = (phone.value || '').trim();
    const ok = /^01[0-9]{8,9}$/.test(v);
    phoneStatus.textContent = ok ? '' : '숫자만 10~11자리(예: 01012345678)';
    phoneStatus.className = ok ? 'hint' : 'hint bad';
    return ok;
  }
  phone.addEventListener('input', validatePhone);

  // ===== 제출 전 최종 검증 =====
  $('#btnSubmit').closest('form').addEventListener('submit', (e) => {
    if (idChecked.value !== 'true') {
      e.preventDefault();
      idStatus.textContent = '아이디 중복확인을 먼저 해주세요.';
      idStatus.className = 'hint bad';
      return;
    }
    if (!validatePw()) { e.preventDefault(); return; }
    if (!buildEmail()) { e.preventDefault(); return; }
    if (!validatePhone()) { e.preventDefault(); return; }
  });
})();
