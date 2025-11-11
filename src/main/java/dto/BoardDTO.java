package dto;

import java.time.LocalDateTime;

public class BoardDTO {
	private int boardId;
	private String title;
	private String content;
	private String writerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private int viewCount;
	private String category;
	private String writerNickname;
	
	public BoardDTO() {
	}

	public BoardDTO(int boardId, String title, String content, String writerId, LocalDateTime createdAt,
			LocalDateTime updatedAt, int viewCount, String category, String writerNickname) {
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.writerId = writerId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.viewCount = viewCount;
		this.category = category;
		this.writerNickname = writerNickname;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriterId() {
		return writerId;
	}

	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWriterNickname() {
		return writerNickname;
	}

	public void setWriterNickname(String writerNickname) {
		this.writerNickname = writerNickname;
	}

}
