/**
 * 
 */

function checkForm(e) {
		const ratingValue = Number(document.reviewForm.rating.value); 
		if (isNaN(ratingValue) || ratingValue < 0 || ratingValue > 5) {
			alert("별점은 0~5점으로 입력해주세요");
			document.reviewForm.rating.select();
			e.preventDefault();
			return;
		}
		
		const content = document.reviewForm.content.value;
		if (content.length > 100){
			alert("내용이 너무 많습니다.");
			document.reviewForm.content.select();
			e.preventDefault();
			return;
		}
		
	}
	
	document.getElementById('reviewModal').addEventListener('hidden.bs.modal', function () {
	  this.querySelector('form').reset();
	});

