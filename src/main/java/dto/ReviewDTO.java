package dto;

import java.sql.Timestamp;

public class ReviewDTO {
    private int id;
    private int burgerId;
    private int userId;
    private double rating;
    private String content;
    private String nickname;
    private Timestamp createdAt;
    private String writeCreatedAtTime; // 출력용 변수
    private Timestamp updatedAt;
    private String writeUpdatedAtTime; // 출력용 변수
    private String imagePath;


	public ReviewDTO() {}



	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

    public int getBurgerId() { return burgerId; }
    public void setBurgerId(int burgerId) { this.burgerId = burgerId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    
    // TIME(작성일) 출력용
    public String getWriteCreatedAtTime() { return writeCreatedAtTime; }
    public void setWriteCreatedAtTime(String writeCreatedAtTime) { this.writeCreatedAtTime = writeCreatedAtTime; }
        
    public String getWriteUpdatedAtTime() { return writeUpdatedAtTime; }
    public void setWriteUpdatedAtTime(String writeUpdatedAtTime) { this.writeUpdatedAtTime = writeUpdatedAtTime; }
    
    
    
    
}
