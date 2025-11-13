/**
 * 
 */

function changeUser(userId){
	const select = document.querySelector(`#roleSelect-${userId}`);
	const selectBtn = document.querySelector('.roleController');
	
	select.disabled = false;
}