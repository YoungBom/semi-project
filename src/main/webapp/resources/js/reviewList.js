
// 리뷰 수정하기 버튼 클릭시 모달창 띄우기
function openUpdateModal(event, reviewId, burgerId, content, rating, imageList) {
  event.preventDefault();
 
  // 모달 요소
  const modalEl = document.getElementById('reviewModal');
  const modal = new bootstrap.Modal(modalEl);

  // 폼 요소
  const form = document.querySelector('.comment-form');
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
  
  // body에서 context path 가져오기(js파일 나누면 기존의 EL태그가 오류나서 body의 data 속성을 이용해 값 전달)
  const contextPath = document.body.dataset.ctx;
  
  // 기존 reviewId hidden이 있다면 제거 후 다시 추가 (중복 방지)
  const oldHidden = form.querySelector('input[name="reviewId"]');
  const oldHidden1 = form.querySelector('input[name="burgerId"]');
  if (oldHidden) oldHidden.remove();
  if (oldHidden1) oldHidden.remove();

  // 새로운 reviewId hidden input 추가
  const hiddenInput = document.createElement('input');
  hiddenInput.type = 'hidden';
  hiddenInput.name = 'reviewId';
  hiddenInput.value = reviewId;
  form.appendChild(hiddenInput);
  // 새로운 burgerId hidden input 추가
  const hiddenInput1 = document.createElement('input');
  hiddenInput1.type = 'hidden';
  hiddenInput1.name = 'burgerId';
  hiddenInput1.value = burgerId;
  form.appendChild(hiddenInput1);

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

// 이미지 클릭시 모달창으로 크게 보여주기
function showImageModal(imageUrl) {
  const modalImg = document.getElementById('modalImage');
  modalImg.src = imageUrl;

  const modal = new bootstrap.Modal(document.getElementById('imageModal'));
  modal.show();
}

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
