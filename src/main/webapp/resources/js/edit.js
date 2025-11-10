/**
 * register.js
 * 회원가입 페이지용 스크립트
 * - 아이디 중복확인
 * - 비밀번호 일치 및 규칙 검증
 * - 이메일 조립 및 유효성 검사
 * - 휴대폰 검증
 * - 관리자 계정 생성 버튼 처리
 */

(() => {
  const $ = (s, p = document) => p.querySelector(s);

  // ===== 아이디 중복확인 =====
  const userId = $('#user_id');
  const btnCheck = $('#btnCheckId');
  const idChecked = $('#idChecked');
  const idStatus = $('#idStatus');
  const checkIdUrl = document.body.dataset.checkIdUrl;

  btnCheck?.addEventListener('click', async () => {
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
      const data = await res.json();
      if (data.available) {
        idChecked.value = 'true';
        idStatus.textContent = '사용 가능한 아이디입니다.';
        idStatus.className = 'hint ok';
      } else {
        idChecked.value = 'false';
        idStatus.textContent = '이미 사용 중인 아이디입니다.';
        idStatus.className = 'hint bad';
      }
    } catch (e) {
      idChecked.value = 'false';
      idStatus.textContent = '확인 실패. 잠시 후 다시 시도해주세요.';
      idStatus.className = 'hint bad';
    }
  });

  userId?.addEventListener('input', () => {
    idChecked.value = 'false';
    idStatus.textContent = '중복확인을 눌러주세요.';
    idStatus.className = 'hint';
  });

  // ===== 비밀번호 규칙/일치 검사 =====
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

  pw1?.addEventListener('input', validatePw);
  pw2?.addEventListener('input', validatePw);

  // ===== 이메일 조립 + 유효성 검사 =====
  const emailLocal  = $('#emailLocal');
  const emailDomain = $('#emailDomainSel');
  const emailCustom = $('#emailDomainCustom');
  const emailHidden = $('#email');
  const emailStatus = $('#emailStatus');

  function isCustomOption(v) {
    return v === '_custom' || v === 'custom';
  }

  function buildEmail() {
    const local = (emailLocal?.value || '').trim();
    let domain = (emailDomain?.value || '').trim();

    if (isCustomOption(domain)) {
      domain = (emailCustom?.value || '').trim();
    }

    if (!local || !domain) {
      emailHidden.value = '';
      emailStatus.textContent = '이메일을 완성해주세요.';
      emailStatus.className = 'hint bad';
      return false;
    }

    const full = `${local}@${domain}`;
    emailHidden.value = full;

    const ok = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(full);
    emailStatus.textContent = ok ? '' : '이메일 형식이 올바르지 않습니다.';
    emailStatus.className = ok ? 'hint' : 'hint bad';
    return ok;
  }

  emailDomain?.addEventListener('change', () => {
    const useCustom = isCustomOption(emailDomain.value);
    if (emailCustom) {
      emailCustom.style.display = useCustom ? 'block' : 'none';
      if (!useCustom) emailCustom.value = '';
    }
    buildEmail();
  });

  emailLocal?.addEventListener('input', buildEmail);
  emailDomain?.addEventListener('input', buildEmail);
  emailCustom?.addEventListener('input', buildEmail);
  buildEmail(); // 초기화

  // ===== 휴대폰 검증 =====
  const phone = $('#phone');
  const phoneStatus = $('#phoneStatus');
  function validatePhone() {
    const v = (phone.value || '').trim();
    const ok = /^01[0-9]{8,9}$/.test(v);
    phoneStatus.textContent = ok ? '' : '숫자만 10~11자리 (예: 01012345678)';
    phoneStatus.className = ok ? 'hint' : 'hint bad';
    return ok;
  }
  phone?.addEventListener('input', validatePhone);

  // ===== 제출 전 최종 검증 =====
  $('#btnSubmit')?.closest('form')?.addEventListener('submit', (e) => {
	
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

  // ===== 관리자 계정 생성 버튼 =====
  const form = document.querySelector('form');
  const role = $('#role');
  const btnAdmin = $('#btnMakeAdmin');

  btnAdmin?.addEventListener('click', () => {
    if (!confirm('관리자 계정을 생성하시겠습니까? 일반 사용자에게 노출되면 안 됩니다.')) return;
    if (role) role.value = 'ADMIN';
    form?.submit();
  });
})();
