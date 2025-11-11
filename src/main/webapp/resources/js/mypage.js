document.addEventListener("DOMContentLoaded", () => {
  const deleteBtn = document.getElementById('confirmDeleteBtn');
  const msgBox = document.getElementById('deleteMsg');
  if (!deleteBtn) return;

  deleteBtn.addEventListener('click', () => {
    if (!confirm("정말 회원 탈퇴를 진행하시겠습니까?")) return;

    fetch(`${contextPath}/user/delete`, { method: "POST" })
      .then(res => res.text())
      .then(result => {
        if (result === "SUCCESS") {
          alert("회원 탈퇴가 완료되었습니다.");
          window.location.href = `${contextPath}/main`;
        } else {
          msgBox.textContent = "탈퇴 중 오류가 발생했습니다. (코드: " + result + ")";
        }
      })
      .catch(() => { msgBox.textContent = "서버 오류가 발생했습니다."; });
  });
});
