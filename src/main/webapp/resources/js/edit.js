/**
 * edit.js
 * 회원정보 수정 페이지용 스크립트
 * - 이메일 로컬/도메인 합치기 (hidden input 갱신)
 * - 휴대폰 번호 유효성 검사
 * - 폼 제출 전 검증
 */

(() => {
  const $ = (s, p = document) => p.querySelector(s);

  // ====== 이메일 조합 ======
  const emailLocal  = $('#emailLocal');
  const emailDomain = $('#emailDomain');
  const emailHidden = $('#emailHidden');
  const form        = document.querySelector('form');

  // 이메일 합치기 함수
  function buildEmail() {
    const local = (emailLocal?.value || '').trim();
    let domain = (emailDomain?.value || '').trim();

    // '@' 제거 (옵션값에는 '@naver.com' 이런 식으로 돼있음)
    if (domain.startsWith('@')) domain = domain.substring(1);

    if (!local || !domain) {
      emailHidden.value = '';
      return;
    }

    const newEmail = `${local}@${domain}`;
    emailHidden.value = newEmail;
    // console.log("💌 emailHidden updated:", newEmail); // 디버깅용
  }

  // ====== 초기값 세팅 ======
  document.addEventListener('DOMContentLoaded', () => {
    buildEmail(); // 페이지 로드 시 바로 반영
  });

  // ====== 이벤트 연결 ======
  emailLocal?.addEventListener('input', buildEmail);
  emailDomain?.addEventListener('change', buildEmail);

  // ====== 휴대폰 번호 검증 ======
  const phone = $('#phone');

  function validatePhone() {
    const val = (phone.value || '').trim();
    const ok = /^01[0-9]{8,9}$/.test(val);
    if (!ok && val.length > 0) {
      phone.classList.add('is-invalid');
      phone.title = '휴대폰 번호 형식이 올바르지 않습니다. 예) 01012345678';
    } else {
      phone.classList.remove('is-invalid');
      phone.title = '';
    }
    return ok;
  }

  phone?.addEventListener('input', validatePhone);

  // ====== 폼 제출 전 검증 ======
  form?.addEventListener('submit', (e) => {
    buildEmail(); // 제출 직전 최신 이메일로 강제 갱신

    if (!emailHidden.value) {
      alert('이메일을 입력해주세요.');
      e.preventDefault();
      return;
    }

    if (!validatePhone()) {
      alert('휴대폰 번호 형식이 올바르지 않습니다.');
      e.preventDefault();
      return;
    }
  });
})();
