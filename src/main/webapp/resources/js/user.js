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