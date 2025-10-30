<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<style>


  <meta charset="UTF-8" />
  <title>회원가입</title>
  
</style>
</head>
<body>
  <h1>회원가입</h1>

  <!-- 간단 안내/에러 메세지 자리 -->
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
