/**
 * register.js
 * 회원가입 유효성 검사 스크립트
 * - 아이디 중복확인
 * - 비밀번호 일치 및 규칙 검증
 * - 이메일 유효성 검사
 * - 휴대폰 검증
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
        idStatus.textContent = '이미 사용중인 아이디입니다.';
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
    const pwA = pw1.value.trim();
    const pwB = pw2.value.trim();

    // 자동으로 소문자로 변경
    if (pwA !== pwA.toLowerCase()) pw1.value = pwA.toLowerCase();
    if (pwB !== pwB.toLowerCase()) pw2.value = pwB.toLowerCase();

    // 형식 체크
    if (!rePw.test(pwA)) {
      pwStatus.textContent = '조건 불충족: 소문자+숫자 8~20자';
      pwStatus.className = 'hint bad';
      return false;
    }

    // 일치 여부 확인
    if (pwA && pwB && pwA !== pwB) {
      pwStatus.textContent = '비밀번호가 일치하지 않습니다.';
      pwStatus.className = 'hint bad';
      return false;
    }

    if (pwA && pwB && pwA === pwB) {
      pwStatus.textContent = '비밀번호가 일치합니다.';
      pwStatus.className = 'hint ok';
      return true;
    }

    pwStatus.textContent = '';
    pwStatus.className = 'hint';
    return false;
  }

  pw1?.addEventListener('input', validatePw);
  pw2?.addEventListener('input', validatePw);

  // ===== 이메일 합치기 + 유효성 검사 =====
  const emailLocal = $('#emailLocal');
  const emailDomainSel = $('#emailDomainSel');
  const emailDomainCustom = $('#emailDomainCustom');
  const emailHidden = $('#email');
  const emailStatus = $('#emailStatus');

  function buildEmail() {
    const local = (emailLocal?.value || '').trim();
    let domain = (emailDomainSel?.value || '').trim();

    if (domain === 'custom') {
      domain = (emailDomainCustom?.value || '').trim();
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
    emailStatus.textContent = ok ? '유효한 이메일 형식입니다.' : '이메일 형식이 올바르지 않습니다.';
    emailStatus.className = ok ? 'hint ok' : 'hint bad';
    return ok;
  }

  emailDomainSel?.addEventListener('change', () => {
    const useCustom = emailDomainSel.value === 'custom';
    if (emailDomainCustom) {
      emailDomainCustom.style.display = useCustom ? 'block' : 'none';
      if (!useCustom) emailDomainCustom.value = '';
    }
    buildEmail();
  });

  emailLocal?.addEventListener('input', buildEmail);
  emailDomainSel?.addEventListener('input', buildEmail);
  emailDomainCustom?.addEventListener('input', buildEmail);

  // ===== 휴대폰 검증 =====
  const phone = $('#phone');
  const phoneStatus = $('#phoneStatus');
  function validatePhone() {
    const v = (phone.value || '').trim();
    const ok = /^01[0-9]{8,9}$/.test(v);
    phoneStatus.textContent = ok ? '' : '숫자만 10~11자리(예: 01012345678)';
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
})();

	const pwInput = document.getElementById('user_pw');
	const togglePw = document.getElementById('togglePw');
	const pwIcon = togglePw.querySelector('i');

  togglePw.addEventListener('click', () => {
    const isHidden = pwInput.type === 'password';
    pwInput.type = isHidden ? 'text' : 'password';
    pwIcon.classList.toggle('bi-eye');
    pwIcon.classList.toggle('bi-eye-slash');
  });
  
  function execDaumPostcode() {
      new daum.Postcode({
          oncomplete: function(data) {
              // 기본 주소 문자열
              var addr = '';

              // 도로명 / 지번 구분
              if (data.userSelectedType === 'R') { 
                  addr = data.roadAddress; // 도로명 주소
              } else { 
                  addr = data.jibunAddress; // 지번 주소
              }

              // 우편번호 + 기본주소를 하나의 칸에 넣어도 되고
              // 우편번호를 따로 두고 싶으면 hidden/input 하나 더 만들어도 됨
              var fullAddress = '(' + data.zonecode + ') ' + addr;

              // 기존 address input에 채우기
              document.getElementById('address').value = fullAddress;

              // 상세주소 포커스 주기
              var detail = document.getElementById('detailAddress');
              if (detail) {
                  detail.focus();
              }
          }
      }).open();
  }

  