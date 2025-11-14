
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

function handleRoleChange(userId) {
    const select = document.querySelector(`#roleSelect-${userId}`);
    const oldRole = select.getAttribute("data-old");
    const newRole = select.value;
}

// 수정 버튼 클릭 시 select/버튼 활성화
function changeUser(userId) {
    const select = document.querySelector(`#roleSelect-${userId}`);
    const btn = document.querySelector(`#roleController-${userId}`);

    if (!select || !btn) return;

    select.disabled = false;
    btn.classList.remove('disabled');
    btn.removeAttribute('aria-disabled');
}

function checkPosition(userId) {
    const select = document.querySelector(`#roleSelect-${userId}`);
    const oldRole = select.getAttribute("data-old"); // USER only
    const newRole = select.value;                    // USER or ADMIN

    // 변경 없으면 submit 안 함
    if (oldRole === newRole) return false;

    // user → admin만 처리하면 됨
    const ok = confirm("관리자로 전환하시겠습니까?");
    if (!ok) {
        select.value = oldRole; // 복구
        return false;
    }

    select.setAttribute("data-old", newRole);
    return true;
}



// 모든 행을 비활성화하는 함수
function disableAllRows() {
    const selects = document.querySelectorAll(".userRole");
    const controllers = document.querySelectorAll(".roleController");

    selects.forEach(s => s.disabled = true);
    controllers.forEach(b => {
        b.classList.add("disabled");
        b.setAttribute("aria-disabled", "true");
    });
}

// 특정 userId만 활성화하는 함수
function changeUser(userId) {
    // 1) 먼저 전체 비활성화
    disableAllRows();

    // 2) 선택한 row 활성화
    const select = document.querySelector(`#roleSelect-${userId}`);
    const btn = document.querySelector(`#roleController-${userId}`);

    if (!select || !btn) return;

    select.disabled = false;
    btn.classList.remove("disabled");
    btn.removeAttribute("aria-disabled");
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