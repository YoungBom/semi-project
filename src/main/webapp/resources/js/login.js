(() => {
	const $ = (s, p = document) => p.querySelector(s);

	// ----- 필수 전역 체크 -----
	const CTX = window.CTX || '';
	const CHECK_ID_URL = window.CHECK_ID_URL || (CTX + '/user/check-id');

	// ===== 비밀번호: 소문자+숫자 8~20자 =====
	const rePw = /^(?=.*[a-z])(?=.*\d)[a-z0-9]{8,20}$/;

	const pw1 = $('#user_pw');
	const pw2 = $('#user_pw2');
	const pwStatus = $('#pwStatus');

	function validatePw() {
		if (!pw1 || !pw2 || !pwStatus) return true;

		// 대문자 자동 소문자화
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
	pw1 && pw1.addEventListener('input', validatePw);
	pw2 && pw2.addEventListener('input', validatePw);

	// ===== 이메일: local + domain 결합 =====
	const emailLocal = $('#emailLocal');
	const emailDomain = $('#emailDomain');
	const emailCustom = $('#emailCustom');
	const emailHidden = $('#email');
	const emailStatus = $('#emailStatus');

	function toggleCustomDomain() {
		if (!emailDomain || !emailCustom) return;
		const show = emailDomain.value === 'custom';
		emailCustom.style.display = show ? 'block' : 'none';
		if (!show) emailCustom.value = '';
	}

	function buildEmail() {
		if (!emailLocal || !emailDomain || !emailHidden || !emailStatus) return true;

		const local = (emailLocal.value || '').trim();
		let domain = emailDomain.value;

		if (domain === 'custom') {
			if (!emailCustom) return false;
			domain = (emailCustom.value || '').trim();
		}

		if (!local || !domain) {
			emailHidden.value = '';
			emailStatus.textContent = '이메일 형식을 입력해주세요.';
			emailStatus.className = 'hint bad';
			return false;
		}

		const full = local + domain; // 예: loser123 + @naver.com
		emailHidden.value = full;

		const ok = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(full);
		emailStatus.textContent = ok ? '' : '이메일 형식이 올바르지 않습니다.';
		emailStatus.className = ok ? 'hint' : 'hint bad';
		return ok;
	}

	emailDomain && emailDomain.addEventListener('change', () => {
		toggleCustomDomain();
		buildEmail();
	});
	emailLocal && emailLocal.addEventListener('input', buildEmail);
	emailCustom && emailCustom.addEventListener('input', buildEmail);

	toggleCustomDomain();
	buildEmail();

	// ===== 아이디 중복확인 =====
	const userId = $('#user_id');
	const btnCheck = $('#btnCheckId');
	const idChecked = $('#idChecked'); // hidden
	const idStatus = $('#idStatus');

	async function safeJson(res) {
		try {
			if (!res.ok) return null;
			const ct = res.headers.get('content-type') || '';
			if (!ct.includes('application/json')) return null;
			return await res.json();
		} catch { return null; }
	}

	btnCheck && btnCheck.addEventListener('click', async () => {
		const id = (userId && userId.value || '').trim();
		if (!id) {
			if (idChecked) idChecked.value = 'false';
			if (idStatus) { idStatus.textContent = '아이디를 입력해주세요.'; idStatus.className = 'hint bad'; }
			return;
		}
		if (idStatus) { idStatus.textContent = '확인 중...'; idStatus.className = 'hint'; }

		try {
			const res = await fetch(CHECK_ID_URL + '?user_id=' + encodeURIComponent(id), {
				headers: { 'Accept': 'application/json' }
			});
			const data = await safeJson(res);
			if (data && data.available === true) {
				if (idChecked) idChecked.value = 'true';
				if (idStatus) { idStatus.textContent = '사용 가능한 아이디입니다.'; idStatus.className = 'hint ok'; }
			} else if (data && data.available === false) {
				if (idChecked) idChecked.value = 'false';
				if (idStatus) { idStatus.textContent = '이미 사용중인 아이디입니다.'; idStatus.className = 'hint bad'; }
			} else {
				throw new Error('invalid json');
			}
		} catch {
			if (idChecked) idChecked.value = 'false';
			if (idStatus) { idStatus.textContent = '확인 실패. 잠시 후 다시 시도해주세요.'; idStatus.className = 'hint bad'; }
		}
	});

	userId && userId.addEventListener('input', () => {
		if (idChecked) idChecked.value = 'false';
		if (idStatus) { idStatus.textContent = '중복확인을 눌러주세요.'; idStatus.className = 'hint'; }
	});

	// ===== 최종 제출 =====
	const btnSubmit = $('#btnSubmit');
	const form = btnSubmit ? btnSubmit.closest('form') : null;
	if (!form) return;

	function openSuccessModal(msg, onClose) {
		const el = document.createElement('div');
		el.className = 'modal show';
		el.innerHTML = `
      <div class="modal-backdrop" data-close></div>
      <div class="modal-panel modal-card" role="dialog" aria-modal="true">
        <div class="modal-header">
          <h3>가입 완료</h3>
          <button class="modal-close" type="button" data-close>&times;</button>
        </div>
        <div class="modal-body">${msg}</div>
        <div class="modal-footer">
          <button class="btn primary" type="button" data-close>확인</button>
        </div>
      </div>`;
		document.body.appendChild(el);
		el.querySelectorAll('[data-close]').forEach(b =>
			b.addEventListener('click', () => { el.remove(); onClose && onClose(); })
		);
	}
	function openSimpleModal(msg) { openSuccessModal(msg); }

	form.addEventListener('submit', async (e) => {
		e.preventDefault();

		if (idChecked && idChecked.value !== 'true') {
			if (idStatus) { idStatus.textContent = '아이디 중복확인을 먼저 해주세요.'; idStatus.className = 'hint bad'; }
			return;
		}
		if (!validatePw()) return;
		if (!buildEmail()) return;

		const phone = $('#phone'), phoneStatus = $('#phoneStatus');
		if (phone) {
			const okPhone = /^01[0-9]{8,9}$/.test((phone.value || '').trim());
			if (!okPhone) {
				if (phoneStatus) { phoneStatus.textContent = '숫자만 10~11자리(예: 01012345678)'; phoneStatus.className = 'hint bad'; }
				return;
			}
		}

		// 중복 제출 방지
		if (btnSubmit) btnSubmit.disabled = true;

		try {
			const fd = new FormData(form);
			const res = await fetch(form.action, { method: 'POST', body: fd, headers: { 'Accept': 'application/json' } });
			const data = await safeJson(res);

			if (data && data.ok) {
				openSuccessModal('회원가입이 완료되었습니다.', () => {
					location.href = CTX + '/login?next=/main.jsp';
				});
			} else {
				openSimpleModal((data && data.message) || '회원가입에 실패했습니다.');
			}
		} catch {
			openSimpleModal('회원가입 중 오류가 발생했습니다.');
		} finally {
			if (btnSubmit) btnSubmit.disabled = false;
		}
	});
})();
