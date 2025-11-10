/**
 * 
 */

function checkUploadNewImage(){
	// 해당 버튼 클릭시 기존 이미지로 등록하겠다
	document.getElementById("imageCheck").value = "true";
	alert("기존 이미지를 유지하도록 설정되었습니다.");
}

function openUpdateModal(event, reviewId, content, rating, burgerId) {
  event.preventDefault();
 
  // 모달 요소
  const modalEl = document.getElementById('reviewModal');
  const modal = new bootstrap.Modal(modalEl);

  // 폼 요소
  const form = document.querySelector('.comment-form');
  const title = document.getElementById('reviewModalLabel');
  const submitBtn = form.querySelector('button[type="submit"]');

  // 기존 내용 채우기
  document.getElementById('content').value = content;
  document.getElementById('rating').value = rating;
  
  // 제목 및 버튼 변경
  title.textContent = "리뷰 수정";
  submitBtn.textContent = "수정 완료";

  // form action 변경 (수정용)
  form.action = `${pageContext.request.contextPath}/review/update`;
  
  // 기존 reviewId hidden이 있다면 제거 후 다시 추가 (중복 방지)
  // 기존 reviewImage 수량도 같이 넘기기
  const oldHidden = form.querySelector('input[name="reviewId"]');
  if (oldHidden) oldHidden.remove();

  // 새로운 reviewId hidden input 추가
  const hiddenInput = document.createElement('input');
  hiddenInput.type = 'hidden';
  hiddenInput.name = 'reviewId';
  hiddenInput.value = reviewId;
  form.appendChild(hiddenInput);

  // 모달 표시
  modal.show();
}

// ✅ 모달 닫힐 때 등록 모드로 초기화
document.addEventListener('DOMContentLoaded', () => {
  const reviewModal = document.getElementById('reviewModal');
  reviewModal.addEventListener('hidden.bs.modal', () => {
    const form = document.querySelector('.comment-form');
    form.reset();
    form.action = `${pageContext.request.contextPath}/review/add?userId=1`;
    document.getElementById('reviewId').value = "";
    document.getElementById('reviewModalLabel').textContent = "리뷰 등록";
    form.querySelector('button[type="submit"]').textContent = "등록";
  });
});
const isLoggedIn = "${sessionScope.LOGIN_UID}" !== "";
document.addEventListener('DOMContentLoaded', () => {
  const openReviewBtn = document.getElementById('openReviewBtn');

  // ✅ JSP에서 data로 전달한 값 읽기
  const isLoggedIn = openReviewBtn.dataset.isLoggedIn === "true";
  const contextPath = openReviewBtn.dataset.ctx || "";

  openReviewBtn.addEventListener('click', (e) => {
    if (!isLoggedIn) {
      e.preventDefault();
      alert("로그인을 해주세요.");
      sessionStorage.setItem("preventModal", "true");
      location.href = `${contextPath}/user/login.jsp`;
    }
  });
});


window.addEventListener("pageshow", function (event) {
	  const modalEl = document.getElementById("reviewModal");
	  const modal = bootstrap.Modal.getInstance(modalEl);
	  
	  if (modal) {
	    modal.hide(); // 모달 강제 닫기
	  }
	});

document.addEventListener("DOMContentLoaded", () => {
	  // 뒤로가기 복원 방지용
	  const modalEl = document.getElementById("reviewModal");
	  const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);

	  // ✅ sessionStorage에 표시값이 있으면 모달 닫고 제거
	  if (sessionStorage.getItem("preventModal") === "true") {
	    modal.hide();
	    sessionStorage.removeItem("preventModal");
	  }
	});	
