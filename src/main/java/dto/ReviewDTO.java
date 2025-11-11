package dto;

import java.sql.Timestamp;
import java.util.List;

public class ReviewDTO {
    private int id;
    private int burgerId;
    private int userId;
    private double rating;
    private String content;
    private String nickname;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String imagePath;
    private List<String> imageList;
    private String brand;
    private String burgerName;
    private String userName;
    
    public ReviewDTO() {}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getBrand() { return brand; }
	public void setBrand(String brand) {this.brand = brand; }

	public int getBurgerId() { return burgerId; }
    public void setBurgerId(int burgerId) { this.burgerId = burgerId; }

    public String getburgerName() { return burgerName; }
    public void setburgerName(String brandName) { this.burgerName = brandName; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

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
    
    public List<String> getImageList() { return imageList; }
    public void setImageList(List<String> imageList) { this.imageList = imageList; }
    
    
}
