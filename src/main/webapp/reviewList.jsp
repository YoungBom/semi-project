<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="container py-4">
	   <!-- Comment list card -->
		<div class="card comment-card shadow-sm">
			<div class="card-body">
			    <!-- Comment item (repeatable) -->
				<div class="d-flex mb-3">
					<img src="https://i.pravatar.cc/48?img=5" alt="avatar" class="comment-avatar me-3">
					<div class="flex-grow-1">
						<div class="d-flex justify-content-between align-items-start">
							<div>
				              <strong>사용자A</strong> <span class="badge bg-secondary">작성자</span>
				              <div class="meta">2025-10-30 · 2시간 전</div>
							</div>
							<div class="text-end">
							  <div class="btn-group btn-group-sm" role="group" aria-label="actions">
							    <button type="button" class="btn btn-outline-secondary" title="좋아요"><i class="bi bi-hand-thumbs-up"></i> 12</button>
							  </div>
							</div>
						</div>
					
						<div class="mt-2 comment-content">이 포스트 정말 도움이 됐습니다. 설명이 명확해서 바로 적용했어요.</div>
					</div>
				</div>
			</div>
		</div>
</body>
</html>