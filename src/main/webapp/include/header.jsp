<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String ctx = request.getContextPath();
  Object uidObj = (session == null) ? null : session.getAttribute("LOGIN_UID");
  String userNickName = (session == null) ? null : (String) session.getAttribute("LOGIN_NICKNAME");
  String userRole = (session == null) ? null : (String) session.getAttribute("LOGIN_ROLE");
  boolean isAdmin = "ADMIN".equalsIgnoreCase(userRole);
  boolean loggedIn = (uidObj != null);
%>
<style>
#site-header .header-user-area {
  gap: 8px;
  position: relative;
}

/* 공통 버튼 */
#site-header .header-user-area .btn {
  font-weight: 500;
  padding: 6px 12px;
  border-radius: 4px;
  border: 1px solid rgba(0, 0, 0, 0.12);
  background-color: transparent;
  color: #333;
  transition: all 0.18s ease-in-out;
}
#site-header .header-user-area .btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

/* 로그인 버튼만 */
#site-header .login-btn {
  border: 1px solid rgba(0, 0, 0, 0.12);
  background-color: rgba(255, 255, 255, 0.5);
  color: #222;
  backdrop-filter: blur(4px);
}
#site-header .login-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

/* 닉네임 */
#site-header .user-greeting {
  font-weight: 500;
  color: #3a2f28;
  letter-spacing: -0.2px;
  max-width: 120px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 드롭다운 버튼 (아이콘 버튼) */
#site-header .dropdown-btn {
  border: none;
  background: transparent;
  font-size: 1rem;
  color: #444;
  padding: 4px 6px;
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  transition: color 0.15s ease-in-out;
}

/* hover 시 색만 살짝 진해짐 (배경 제거) */
#site-header .dropdown-btn:hover {
  color: #000;
  background: transparent !important;
}

/* 아이콘 옆의 chevron */
#site-header .dropdown-btn .chevron {
  transition: transform 0.25s ease;
}

/* 열릴 때 chevron 회전 */
#site-header .dropdown-btn.active .chevron {
  transform: rotate(180deg);
}
#site-header .dropdown-btn .menu-label {
  white-space: nowrap !important;
  font-weight: 500;
  margin-left:1px;
  color: inherit;           /* 부모 버튼 컬러 따라감 */
  font-size: 0.8rem;
}
/* ===================================================== */
/* 🎨 드롭다운: 헤더와 일체감 있게 조화된 감각형 */
/* ===================================================== */
#site-header .dropdown-menu-list {
  position: absolute;
  top: calc(100% - 2px);
  right: 0;
  display: flex;
  flex-direction: column;
  visibility: hidden;
  opacity: 0;
  transform: translateY(-3px);
  transition: all 0.25s cubic-bezier(0.25, 0.1, 0.25, 1);

  /* ✨ 핵심 디자인 */
  background: rgba(255, 255, 255, 0.88);
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-top: none;
  border-radius: 0 0 3px 3px;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.06);
  min-width: 170px;
  padding: 6px 0;
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  z-index: 999;
}

/* 활성화 */
#site-header .dropdown-menu-list.show {
  visibility: visible;
  opacity: 1;
  transform: translateY(0);
  animation: dropdownFadeIn 0.25s ease-out;
}

/* 메뉴 항목 */
#site-header .dropdown-menu-list a,
#site-header .dropdown-menu-list button {
  padding: 11px 18px;
  font-size: 0.9rem;
  color: #2f2f2f;
  background: transparent;
  border: none;
  text-align: left;
  width: 100%;
  letter-spacing: -0.2px;
  transition: background-color 0.15s ease-in-out, color 0.15s ease-in-out;
}
#site-header .dropdown-menu-list a:hover,
#site-header .dropdown-menu-list button:hover {
  background: rgba(0, 0, 0, 0.05);
  color: #111;
}

/* 항목 간 경계선 */
#site-header .dropdown-menu-list a:not(:last-child),
#site-header .dropdown-menu-list button:not(:last-child) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.035);
}

/* 애니메이션 */
@keyframes dropdownFadeIn {
  from {
    opacity: 0;
    transform: translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 닫힐 때 부드럽게 사라지기 */
#site-header .dropdown-menu-list {
  transition:
    opacity 0.25s ease,
    transform 0.25s ease;
}
#site-header .dropdown-menu-list.hide {
  opacity: 0;
  transform: translateY(-5px);
}

/* 아이콘 버튼과 메뉴 간 살짝 여백 */
#site-header .user-dropdown,
#site-header .admin-dropdown {
  position: relative;
  margin-right: 2px;
}
#site-header .dropdown-menu-list a {
  text-decoration: none !important;
}
#site-header .dropdown-menu-list a:hover {
  text-decoration: none !important;
}
#site-header .search-form-modern {
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 6px;
  padding: 4px 10px 4px 12px;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  transition: all 0.2s ease-in-out;
  height: 38px;
  min-width: 220px;
}

#site-header .search-form-modern:hover,
#site-header .search-form-modern:focus-within {
  border-color: rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.78);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}

/* 인풋 */
#site-header .search-input-modern {
  border: none;
  background: transparent;
  outline: none;
  flex: 1;
  color: #2f2f2f;
  font-size: 0.9rem;
  letter-spacing: -0.1px;
}

#site-header .search-input-modern::placeholder {
  color: rgba(0, 0, 0, 0.35);
}

/* 버튼 */
#site-header .search-btn-modern {
  border: none;
  background: transparent;
  color: #444;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px 6px;
  border-radius: 4px;
}

#site-header .search-btn-modern:hover {
  background: rgba(0, 0, 0, 0.05);
  color: #000;
}

#site-header .search-btn-modern i {
  pointer-events: none;
}
@media (max-width: 990px) {
  #site-header .search-form-modern {
    width: 100%;
    margin-top: 8px;
  }
  #site-header .header-user-area {
	gap: 8px;
	position: relative;
	margin-top: 8px
  }
}
/* 반응형 */
@media (max-width: 600px) {
  #site-header .search-form-modern {
    width: 100%;
    margin-top: 8px;
  }
}
/* 반응형 */
@media (max-width: 600px) {
  #site-header .header-user-area {
    flex-direction: column;
    align-items: center;
  }
}
</style>

<link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet">

<div id="site-header">
<nav class="navbar navbar-expand-lg shadow-sm py-3" style="background:#fff8e6;">
  <div class="container">

    <!-- 브랜드 링크: 필요 시 /main.jsp 유지 -->
    <a class="navbar-brand fw-bold fs-3 text-brown" href="<%=ctx%>/main" style="color:#b35a00;">
      🍔 BurgerHub
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navMenu">

      <!-- ⛔ 아래 메뉴는 요청대로 수정하지 않음 -->
      <ul class="navbar-nav me-auto mb-2 mb-lg-0 fw-semibold">
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/burger/menu">메뉴</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/event.jsp">이벤트</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/board/list">공지사항</a></li>
        <li class="nav-item"><a class="nav-link text-dark" href="<%=ctx%>/findStore">매장찾기</a></li>
      </ul>

      <!-- 검색 폼 -->
	<form action="<%=ctx%>/burger/menu" method="get" class="search-form-modern d-flex align-items-center me-3">
	  <input 
	    class="search-input-modern"
	    type="text" 
	    name="keyword" 
	    placeholder="버거 검색..." 
	    aria-label="버거 검색">
	  <button class="search-btn-modern" type="submit">
	    <i class="bi bi-search"></i>
	  </button>
	</form>

     
     
     
<div class="d-flex align-items-center gap-2 header-user-area">
  <% if (!loggedIn) { %>
    <!-- 🟢 비로그인 상태 -->
    <a href="<%=ctx%>/user/login.jsp" class="btn login-btn">로그인</a>
    <a href="<%=ctx%>/user/register.jsp" class="btn login-btn">회원가입</a>

  <% } else if (isAdmin) { %>
    <!-- 🟣 관리자 -->
		<span class="me-2 user-greeting text-nowrap">
		  <span class="text-primary">관리자</span><%= (userNickName == null ? "" : userNickName) %>
		</span>

    <!-- 마이페이지 / 로그아웃 드롭다운 -->
    <div class="user-dropdown position-relative">
      <button type="button" class="btn dropdown-btn" id="userMenuBtn">
        <i class="bi bi-person-circle"></i>
        <span class="menu-label">계정</span>
        <span class="chevron">▾</span>
      </button>
      <div class="dropdown-menu-list" id="userMenu">
        <a href="<%=ctx%>/user/mypage">마이페이지</a>
        <form method="post" action="<%=ctx%>/logout">
          <button type="submit">로그아웃</button>
        </form>
      </div>
    </div>

    <!-- 관리자 전용 관리 드롭다운 -->
    <div class="admin-dropdown position-relative">
      <button type="button" class="btn dropdown-btn" id="adminMenuBtn">
        <i class="bi bi-gear"></i>
        <span class="menu-label"> 관리</span>
        <span class="chevron">▾</span>
      </button>
      <div class="dropdown-menu-list" id="adminMenu">
        <a href="<%=ctx%>/burger/list">버거 관리</a>
        <a href="<%=ctx%>/user/management">회원 관리</a>
      </div>
    </div>

  <% } else { %>
    <!-- 🔵 일반 사용자 -->
    <span class="me-2 user-greeting text-nowrap">
      <%= (userNickName == null ? "회원" : userNickName) %>
    </span>

    <!-- 마이페이지 / 로그아웃 드롭다운 -->
    <div class="user-dropdown position-relative">
      <button type="button" class="btn dropdown-btn" id="userMenuBtn">
        <i class="bi bi-person-circle"></i>
        <span class="menu-label">계정</span>
        <span class="chevron">▾</span>
      </button>
      <div class="dropdown-menu-list" id="userMenu">
        <a href="<%=ctx%>/user/mypage">마이페이지</a>
        <form method="post" action="<%=ctx%>/logout">
          <button type="submit">로그아웃</button>
        </form>
      </div>
    </div>
  <% } %>
</div>





    </div>
  </div>
</nav>
</div>	
<script>
document.addEventListener("DOMContentLoaded", function() {
	  const userBtn = document.getElementById("userMenuBtn");
	  const adminBtn = document.getElementById("adminMenuBtn");
	  const userMenu = document.getElementById("userMenu");
	  const adminMenu = document.getElementById("adminMenu");

	  const toggleMenu = (btn, menu, otherBtn, otherMenu) => {
	    btn?.addEventListener("click", (e) => {
	      e.stopPropagation();

	      otherMenu?.classList.remove("show");
	      otherBtn?.classList.remove("active");

	      menu?.classList.toggle("show");
	      btn?.classList.toggle("active");
	    });
	  };

	  toggleMenu(userBtn, userMenu, adminBtn, adminMenu);
	  toggleMenu(adminBtn, adminMenu, userBtn, userMenu);

	  document.addEventListener("click", () => {
	    userMenu?.classList.remove("show");
	    adminMenu?.classList.remove("show");
	    userBtn?.classList.remove("active");
	    adminBtn?.classList.remove("active");
	  });
	});
</script>
