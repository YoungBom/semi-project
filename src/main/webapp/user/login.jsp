<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/user.css">
</head>
<body>


	<h1>로그인</h1>



	<form method="post" action="<%=request.getContextPath()%>/login"
		autocomplete="on">
		<label> 아이디 <input type="text" name="user_id" required
			maxlength="255" placeholder="아이디를 입력하세요" autocomplete="username">
		</label> <label> 비밀번호 <input type="password" name="user_pw" required
			minlength="8" maxlength="255" placeholder="비밀번호를 입력하세요"
			autocomplete="current-password">
		</label> <label class="inline"> <input type="checkbox"
			name="remember_me" value="1"> 로그인 상태 유지(선택)
		</label>

		<div class="actions">
			<button type="submit">로그인</button>
		</div>
	</form>

	<p class="sub">
		<a href="<%=request.getContextPath()%>/user/register.jsp">회원가입</a> · <a
			href="<%=request.getContextPath()%>/password/forgot">아이디/비밀번호 찾기</a>
		<!-- 나중에 연결 -->
	</p>
	<%-- 에러 문자열 가져오기 --%>
	<%
	String loginErr = (String) request.getAttribute("error");
	boolean hasLoginErr = (loginErr != null && !loginErr.isBlank());
	%>

	<!-- Login Error Modal -->
	<div id="login-error-backdrop" class="modal-backdrop"
		aria-hidden="true"></div>
	<div id="login-error-modal" class="modal" role="dialog"
		aria-modal="true" aria-labelledby="loginErrorTitle"
		aria-describedby="loginErrorMsg">
		<div class="modal-card">
			<h3 id="loginErrorTitle">로그인 실패</h3>
			<p id="loginErrorMsg"><%=hasLoginErr ? loginErr : ""%></p>
			<div class="modal-actions">
				<button type="button" class="btn primary" id="login-error-ok">확인</button>
			</div>
			<button type="button" class="modal-close" id="login-error-close"
				aria-label="닫기">×</button>
		</div>
	</div>

	<script>
  (function () {
    const hasError = <%=hasLoginErr ? "true" : "false"%>;
    if (!hasError) return;

    const modal    = document.getElementById('login-error-modal');
    const backdrop = document.getElementById('login-error-backdrop');
    const okBtn    = document.getElementById('login-error-ok');
    const xBtn     = document.getElementById('login-error-close');

    function openModal() {
      modal.classList.add('show');
      backdrop.classList.add('show');
      okBtn.focus();
    }
    function closeModal() {
      modal.classList.remove('show');
      backdrop.classList.remove('show');
    }
    okBtn.addEventListener('click', closeModal);
    xBtn.addEventListener('click', closeModal);
    backdrop.addEventListener('click', closeModal);
    document.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeModal(); });

    // 페이지 로드 시 바로 표시
    openModal();
  })();
</script>

</body>
</html>
