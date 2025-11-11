/**
 * 
 */


// mypage.js

document.addEventListener("DOMContentLoaded", () => {
  const deleteBtn = document.getElementById('confirmDeleteBtn');
  const pwInput = document.getElementById('deletePw');
  const msgBox = document.getElementById('deleteMsg');

  if (!deleteBtn) return; // 안전장치

  deleteBtn.addEventListener('click', () => {
    const pw = pwInput.value.trim();

    if (!pw) {
      msgBox.textContent = "비밀번호를 입력해주세요.";
      return;
    }

    fetch(`${contextPath}/user/delete`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({ password: pw })
    })
      .then(res => res.text())
      .then(result => {
        if (result === "INVALID") {
          msgBox.textContent = "비밀번호가 일치하지 않습니다.";
        } else if (result === "SUCCESS") {
          alert("회원 탈퇴가 완료되었습니다.");
          window.location.href = `${contextPath}/main.jsp`;
        } else {
          msgBox.textContent = "탈퇴 중 오류가 발생했습니다.";
        }
      })
      .catch(() => {
        msgBox.textContent = "서버 오류가 발생했습니다.";
      });
  });
});
