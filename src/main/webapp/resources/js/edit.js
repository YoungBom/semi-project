/* resources/js/edit.js
 * MyPage > 회원정보수정 전용 스크립트
 * - 이메일 local + domain 합쳐 hidden(name="email")로 전송
 * - select에 'custom' 옵션이 있을 경우, #emailDomainCustom 입력칸을 토글
 * - 제출 직전에 한 번 더 보정
 */
(function () {
  // ===== 요소 캐시 =====
  var form            = document.querySelector('form.form-card');
  if (!form) return;

  var emailLocal      = document.getElementById('emailLocal');     // 앞부분
  var emailDomainSel  = document.getElementById('emailDomain');    // 도메인 드롭박스 (@gmail.com 등 or 'custom')
  var emailHidden     = document.getElementById('emailHidden');    // 최종 전송(name="email")
  // 선택 사항: 직접입력 도메인 입력칸(있으면 사용, 없으면 무시)
  var emailDomainBox  = document.getElementById('emailDomainCustom');

  // ===== 유틸 =====
  function startsWithAt(s) { return s && s.charAt(0) === '@'; }
  function normDomain(s) {
    s = (s || '').trim();
    if (!s) return '';
    // 'gmail.com' -> '@gmail.com'
    return startsWithAt(s) ? s : ('@' + s);
  }

  

  // emailHidden 값 합성
  function composeEmail() {
    var local = (emailLocal ? emailLocal.value : '').trim();
    var domain;

    if (emailDomainSel) {
      if (emailDomainSel.value === 'custom' && emailDomainBox) {
        domain = normDomain(emailDomainBox.value);
      } else {
        // 셀렉트 값이 '@gmail.com' 같은 형식이라고 가정
        domain = (emailDomainSel.value || '').trim();
        // 혹시 '@' 빠진 값이 들어오면 보정
        if (domain && !startsWithAt(domain)) domain = '@' + domain;
      }
    } else {
      domain = '';
    }

    emailHidden.value = (local && domain) ? (local + domain) : '';
  }

  // ===== 초기 상태 세팅 =====
  syncCustomVisibility();
  composeEmail();

  // ===== 이벤트 바인딩 =====
  if (emailLocal)      emailLocal.addEventListener('input',  composeEmail);
  if (emailDomainSel)  emailDomainSel.addEventListener('change', function () {
    syncCustomVisibility();
    composeEmail();
  });
  if (emailDomainBox)  emailDomainBox.addEventListener('input', composeEmail);

  // 제출 직전에 최종 보정
  form.addEventListener('submit', function () {
    syncCustomVisibility();
    composeEmail();
    // (선택) 간단 검증: email이 비면 전송 막기
    // if (!emailHidden.value) {
    //   alert('이메일을 입력해 주세요.');
    //   event.preventDefault();
    // }
  });
})();
