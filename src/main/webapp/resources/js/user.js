
function changeUser(userId){
	const select = document.querySelector(`#roleSelect-${userId}`);
	const selectBtn = document.querySelector(`#roleController-${userId}`);
	const selectRow = document.querySelector(`#selectUser-${userId}`);
	
	console.log({select, selectBtn, selectRow});
	  
	  if (!select || !selectBtn || !selectRow) return;
	
	// 기존의 선택된 select / 버튼 정보 가져와서 비활성화 처리하기  
    document.querySelectorAll("tbody tr").forEach(row => {
	  // 배경색 초기화
      row.querySelectorAll("td").forEach(td => {
        td.style.removeProperty("background-color");
      });
	  // 각 행의 select / 버튼 비활성화
	      const s = row.querySelector(".userRole");
	      const b = row.querySelector(".roleController");
	      if (s) s.disabled = true;
	      if (b) {
	        b.classList.add("disabled");
	        b.setAttribute("aria-disabled", "true");
	      }
    });

    // 선택한 select 활성화
    select.disabled = false;

    // 선택한 버튼(a태그) 버튼 활성화
    selectBtn.classList.remove('disabled');
    selectBtn.setAttribute('aria-disabled', 'false');
	
	// bootstrap은 tr이 아니라 td에 배경색을 그리기때문에 td에 색을 지정해줘야한다.
	// 각각의 td에 색을 주려면 하기와 같이 작성해야함
	selectRow.querySelectorAll("td").forEach(td => {
	    td.style.setProperty("background-color", "lightGray", "important");
	});
}

/*유저 관리자 권한 부여 시 관리자 확인필*/
function checkPosition(e) {
	const result = confirm("관리자로 전환 하시겠습니까?");
	
	if(result){
			return true;
		}else {
			return false;
		}
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