(function(){
  const $ = (sel, root=document) => root.querySelector(sel);
  const $$ = (sel, root=document) => Array.from(root.querySelectorAll(sel));

  const modal = $('#errorModal');
  const msgBox = $('#errMsg');

  function openModal(message){
    if(!modal) return;
    msgBox.textContent = message || '아이디 또는 비밀번호가 올바르지 않습니다.';
    modal.classList.add('show');
    modal.setAttribute('aria-hidden', 'false');
    const closeBtn = modal.querySelector('[data-close]');
    closeBtn?.focus();
  }
  function closeModal(){
    if(!modal) return;
    modal.classList.remove('show');
    modal.setAttribute('aria-hidden', 'true');
  }

  $$('.modal [data-close]').forEach(el => el.addEventListener('click', closeModal));
  $('.modal-backdrop')?.addEventListener('click', closeModal);
  document.addEventListener('keydown', (e)=>{ if(e.key === 'Escape') closeModal(); });

  const serverError = $('#serverError')?.dataset.msg || $('#serverErrorParam')?.dataset.msg;
  if(serverError && serverError.trim()) openModal(serverError);
})();
