package dto;

public class BurgerDetailsDTO {
    private int id;
    private int burgerId;
    private int calories;
    private int carbohydrates;
    private int protein;
    private int fat;
    private int sodium;
    private int sugar;
    private String allergyInfo;

    public BurgerDetailsDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBurgerId() { return burgerId; }
    public void setBurgerId(int burgerId) { this.burgerId = burgerId; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public int getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(int carbohydrates) { this.carbohydrates = carbohydrates; }

    public int getProtein() { return protein; }
    public void setProtein(int protein) { this.protein = protein; }

    public int getFat() { return fat; }
    public void setFat(int fat) { this.fat = fat; }

    public int getSodium() { return sodium; }
    public void setSodium(int sodium) { this.sodium = sodium; }

    public int getSugar() { return sugar; }
    public void setSugar(int sugar) { this.sugar = sugar; }

    public String getAllergyInfo() { return allergyInfo; }
    public void setAllergyInfo(String allergyInfo) { this.allergyInfo = allergyInfo; }
}
