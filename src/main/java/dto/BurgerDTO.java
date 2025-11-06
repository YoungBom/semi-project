package dto;

public class BurgerDTO {
    private int id;
    private int userId;         // FK → user.id
    private String name;
    private int price;
    private String imagePath;
    private String brand;
    private String pattyType;   // ENUM('치킨','비프','기타')
    private boolean newBurger;
    private double avgRating;
    
    private BurgerDetailsDTO details;
    
    public BurgerDTO() {}

    
    public BurgerDTO(String name, int price, String imagePath, String brand, String pattyType) {
		super();
		this.name = name;
		this.price = price;
		this.imagePath = imagePath;
		this.brand = brand;
		this.pattyType = pattyType;
	}


	public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getPattyType() { return pattyType; }
    public void setPattyType(String pattyType) { this.pattyType = pattyType; }
    
    public BurgerDetailsDTO getDetails() { return details; }
    public void setDetails(BurgerDetailsDTO details) { this.details = details; }
    
    public boolean isNewBurger() { return newBurger; }
    public void setNewBurger(boolean newBurger) { this.newBurger  = newBurger; }
    
    public double getAvgRating() { return avgRating; }
    public void setAvgRating(double avgRating) { this.avgRating = avgRating; }
    
    
    
}
