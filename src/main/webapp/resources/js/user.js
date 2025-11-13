/**
 * 
 */

function changeUser(userId){
	const select = document.querySelector(`#roleSelect-${userId}`);
	const selectBtn = document.querySelector(`#roleController-${userId}`);
	
	if (!select || !selectBtn) return;

    // select 활성화
    select.disabled = false;

    // a태그 버튼 활성화
    selectBtn.classList.remove('disabled');
    selectBtn.setAttribute('aria-disabled', 'false');
}

/*유저 삭제 시 관리자 확인필*/
function checkDelete(e){
	const result = confirm("삭제하시겠습니까?");
	
	if(result){
		return true;
	}else {
		return false;
	}	
}