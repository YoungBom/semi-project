package dto;

public class ReviewImageDTO {
    private int id;
    private int reviewId;
    private String imagePath;

    public ReviewImageDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
