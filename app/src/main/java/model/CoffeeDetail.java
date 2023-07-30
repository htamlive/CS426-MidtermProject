package model;

public class CoffeeDetail {
    private int ID;
    private String name;
    private String description;
    private float defaultPrice;
    private int imageResourceId;

    public CoffeeDetail(int ID, String name, String description, int imageResourceId) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.defaultPrice = 0.f;
    }

    public CoffeeDetail(int ID, String name, String description, int imageResourceId, float defaultPrice) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.defaultPrice = defaultPrice;
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public float getDefaultPrice() {
        return defaultPrice;
    }

    public int getId() {
        return ID;
    }
}
