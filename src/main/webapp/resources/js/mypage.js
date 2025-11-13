document.addEventListener("DOMContentLoaded", () => {
  const deleteBtn = document.getElementById('confirmDeleteBtn');
  const msgBox = document.getElementById('deleteMsg');

  deleteBtn.addEventListener('click', () => {

    const inputId = document.getElementById("deleteInputId").value.trim();
    // 🔥 빈값 체크
    if (!inputId) {
        msgBox.textContent = "아이디를 입력하세요.";
        return;
    }

    fetch(`${contextPath}/user/delete`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: "inputId=" + encodeURIComponent(inputId)
    })
      .then(res => res.text())
      .then(result => {
        if (result === "SUCCESS") {
          alert("회원 탈퇴가 완료되었습니다.");
          window.location.href = `${contextPath}/main`;
        } else {
          msgBox.textContent = "아이디가 일치하지 않습니다.";
        }
      });
  });
});
