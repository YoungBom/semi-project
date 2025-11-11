document.addEventListener('DOMContentLoaded', () => {
  const fileInput = document.getElementById('fileInput');
  const changeBtn = document.getElementById('changeBtn');
  const profileForm = document.getElementById('profileForm');

  if (!fileInput || !changeBtn || !profileForm) return;

  // 버튼 클릭 → 파일 선택창 열기
  changeBtn.addEventListener('click', () => fileInput.click());

  // 파일 선택 → 자동 업로드
  fileInput.addEventListener('change', () => {
    if (fileInput.files.length > 0) {
      profileForm.submit();
    }
  });
});