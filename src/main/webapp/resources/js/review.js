
// 리뷰 수정하기 버튼 클릭시 모달창 띄우기
function openUpdateModal(event, reviewId, content, rating, burgerId, imageList) {
  event.preventDefault();
 
  // 모달 요소
  const modalEl = document.getElementById('reviewModal');
  const modal = new bootstrap.Modal(modalEl);

  // 폼 요소
  const form = document.querySelector('.comment-form');
  const title = document.getElementById('reviewModalLabel');
  const submitBtn = form.querySelector('button[type="submit"]');
  const oldImageBtn = document.getElementById('oldImageButtonContainer');
  const imageCheck = document.getElementById('imageCheck');
  const oldImageInput = document.getElementById("oldImageName");
  

  // 기존 내용 채우기
  document.getElementById('content').value = content;
  document.getElementById('rating').value = rating;
  
  // 리뷰 등록시 이미지를 저장하였을때만 버튼 활성화
  if (imageList.length > 2) {
    // 기존이미지 기능 버튼 활성화
    oldImageBtn.style.display = 'inline-block';
  }
  
  // 제목 및 버튼 변경
  title.textContent = "리뷰 수정";
  submitBtn.textContent = "수정 완료";
  
  // body에서 context path 가져오기(js파일 나누면 기존의 EL태그가 오류나서 body의 data 속성을 이용해 값 전달)
  const contextPath = document.body.dataset.ctx;
  // form action 변경 (수정용)
  form.action = `${contextPath}/review/update`;
  
  // 기존 reviewId hidden이 있다면 제거 후 다시 추가 (중복 방지)
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

// 기존이미지 등록할건지 체크
function checkImg() {
  const oldImageBtn = document.getElementById('oldImageButtonContainer');
  const imageCheck = document.getElementById('imageCheck');

  // 기존이미지 버튼 토글 기능
  const isActive =  oldImageBtn.classList.toggle('active');
  if (isActive) {
	alert("기존이미지로 등록되었습니다.")
    imageCheck.value = 'true';
  } else {
	alert("이미지를 삭제합니다.")
    imageCheck.value = 'false';
  }

}

// ✅ 모달 닫힐 때 등록 모드로 초기화
document.addEventListener('DOMContentLoaded', () => {
  const reviewModal = document.getElementById('reviewModal');
  // body에서 context path 가져오기(js파일 나누면 기존의 EL태그가 오류나서 body의 data 속성을 이용해 값 전달)
  const contextPath = document.body.dataset.ctx;
  const form = document.querySelector('.comment-form');
  const oldImageBtn = document.getElementById('oldImageButtonContainer');
	
  reviewModal.addEventListener('hidden.bs.modal', () => {
	// 폼 초기화
    form.reset();
    form.action = `${contextPath}/review/add`;
    document.getElementById('reviewModalLabel').textContent = "리뷰 등록";
    form.querySelector('button[type="submit"]').textContent = "등록";
	// 기존이미지등록 버튼 초기화
	imageCheck.value = 'false';         // 초기화
	oldImageBtn.style.display = 'none'; // 숨김
	oldImageBtn.classList.remove('active');
  });
});

// 이미지 클릭시 모달창으로 크게 보여주기
function showImageModal(imageUrl) {
  const modalImg = document.getElementById('modalImage');
  modalImg.src = imageUrl;

  const modal = new bootstrap.Modal(document.getElementById('imageModal'));
  modal.show();
}

// 리뷰 등록 버튼은 유저ID값이 있을때만 작동되게끔 
document.addEventListener('DOMContentLoaded', () => {
  const openReviewBtn = document.getElementById('openReviewBtn');
  if (!openReviewBtn) return; // 버튼 없으면 코드 종료

  // JSP에서 data로 전달한 값 읽기 body에 data 속성으로 전달한 값 가져오기
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

	function checkForm(e) {
	  e.preventDefault();

	  const form = document.querySelector(".comment-form");
	  const ratingInput = form.querySelector("#rating");
	  const contentInput = form.querySelector("#content");

	  const ratingValue = Number(ratingInput.value.trim());
	  const content = contentInput.value.trim();

	  // 별점 검사
	  if (!isFinite(ratingValue) || ratingValue < 0 || ratingValue > 5) {
	    alert("🚫 별점은 0.0 ~ 5.0 사이로 입력해주세요.");
	    ratingInput.focus();
	    return false;
	  }

	  // 내용 검사
	  if (content.length === 0) {
	    alert("✏️ 리뷰 내용을 입력해주세요.");
	    contentInput.focus();
	    return false;
	  }

	  if (content.length > 100) {
	    alert("⚠️ 내용이 너무 깁니다. (최대 100자)");
	    contentInput.focus();
	    return false;
	  }

	  form.submit();
	  return true;
	}
