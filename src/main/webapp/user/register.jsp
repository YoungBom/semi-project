<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<style>

:root{
  --mc-red:#DA291C;
  --mc-yellow:#FFC72C;
  --mc-dark:#111111;
  --mc-gray:#F6F6F6;
  --mc-border:#E5E5E5;
}

*{box-sizing:border-box}
html,body{margin:0;padding:0}
body{
  font-family: system-ui, -apple-system, Segoe UI, Roboto, Noto Sans, "Apple SD Gothic Neo", "Malgun Gothic", Arial, sans-serif;
  background: linear-gradient(180deg, #fff 0%, #fff 60%, var(--mc-gray) 100%);
  color: var(--mc-dark);
  line-height:1.5;
}

h1{
  margin:32px auto 16px;
  max-width:720px;
  padding:0 16px;
  font-size:28px;
  font-weight:800;
  letter-spacing:-0.2px;
  display:flex;
  align-items:center;
  gap:10px;
}
h1::before{
  content:"";
  background:var(--mc-yellow);
  border-radius:50%;
  display:inline-flex;
  align-items:center;
  justify-content:center;
  width:40px;height:40px;
  box-shadow:0 3px 0 rgba(0,0,0,0.08) inset;
}


form{
  max-width:720px;
  margin:0 auto;
  background:#fff;
  padding:24px;
  border-radius:16px;
  border:1px solid var(--mc-border);
  box-shadow: 0 8px 24px rgba(0,0,0,0.06);
}


.row{
  display:grid;
  grid-template-columns: 1fr 1fr;
  gap:16px;
}
@media (max-width:640px){
  .row{ grid-template-columns: 1fr; }
}


label{
  display:flex;
  flex-direction:column;
  gap:8px;
  margin:12px 0;
  font-weight:600;
}
input, select{
  appearance:none;
  border:1.5px solid var(--mc-border);
  border-radius:12px;
  padding:12px 14px;
  font-size:15px;
  background:#fff;
  transition: border-color .15s ease, box-shadow .15s ease, transform .02s ease;
}
input::placeholder{ color:#9aa0a6; }
input:focus, select:focus{
  outline:none;
  border-color: var(--mc-red);
  box-shadow: 0 0 0 4px rgba(218,41,28,0.12);
}
input:invalid{ 
  border-color:#ff9c9c;
}


p{
  max-width:720px;
  margin:8px auto;
  padding:0 16px;
}
p:empty{ display:none; }
p:nth-of-type(1){ 
  color:#B00020;
  font-weight:700;
}
p:nth-of-type(2){ 
  color:#0B6B0B;
  font-weight:700;
}


.actions{
  margin-top:20px;
  display:flex;
  gap:12px;
}
.actions button{
  cursor:pointer;
  border:0;
  border-radius:999px;
  padding:12px 20px;
  font-size:16px;
  font-weight:900;
  letter-spacing:.2px;
  background: linear-gradient(180deg, var(--mc-red), #b71f16);
  color:#fff;
  box-shadow: 0 6px 16px rgba(218,41,28,0.25), 0 2px 0 rgba(0,0,0,0.06) inset;
  transition: transform .06s ease, box-shadow .15s ease, filter .15s ease;
}
.actions button:hover{
  filter:saturate(1.05) brightness(1.02);
  box-shadow: 0 8px 20px rgba(218,41,28,0.35), 0 2px 0 rgba(0,0,0,0.08) inset;
}
.actions button:active{
  transform: translateY(1px);
}


a{
  color:var(--mc-red);
  font-weight:700;
  text-decoration:none;
  border-bottom:2px solid transparent;
  transition:border-color .15s ease, color .15s ease;
}
a:hover{
  color:#b71f16;
  border-color:#ffd4d0;
}


body > p:last-of-type{
  margin-top:16px;
  text-align:center;
}


form::after{
  content:"";
  display:block;
  height:6px;
  margin-top:18px;
  border-radius:6px;
  background: linear-gradient(90deg, var(--mc-yellow) 0%, #ffe27a 100%);
}

  
</style>
</head>

  <meta charset="UTF-8" />
  <title>회원가입</title>
<body>
  <h1>회원가입</h1>

  
  <p ><%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %></p>
  <p ><%= request.getAttribute("msg") != null ? request.getAttribute("msg") : "" %></p>

  <form method="post" action="<%= request.getContextPath() %>/register">
    <label>아이디
      <input type="text" name="user_id" required maxlength="255" placeholder="로그인에 쓸 아이디" />
    </label>

    <label>비밀번호
      <input type="password" name="user_pw" required minlength="8" maxlength="255" placeholder="8자 이상" />
    </label>

    <label>이메일
      <input type="email" name="email" required maxlength="255" placeholder="example@domain.com" />
    </label>

    <div class="row">
      <label>이름
        <input type="text" name="name" required maxlength="255" />
      </label>
      <label>성별
        <select name="gender" required>
          <option value="">선택</option>
          <option value="남">남</option>
          <option value="여">여</option>
        </select>
      </label>
    </div>

    <div class="row">
      <label>생년월일
        
        <input type="date" name="birth" required />
      </label>
      <label>휴대폰
        <input type="text" name="phone" maxlength="20" placeholder="010-1234-5678" />
      </label>
    </div>

    <label>닉네임
      <input type="text" name="nickname" maxlength="255" />
    </label>

    <label>주소
      <input type="text" name="address" maxlength="255" />
    </label>

    <div class="actions">
      <button type="submit">가입하기</button>
    </div>
  </form>

  <p style="margin-top:12px;">
    이미 계정이 있나요? <a href="<%= request.getContextPath() %>/login">로그인</a>
  </p>
</body>
</html>
