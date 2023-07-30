package model;

import android.database.Cursor;

public class RedeemItem {
    private  int id;
    private String coffeeName;
    private int imageId;
    private int point;
    private String dateTime;

    public RedeemItem(String coffeeName, int imageId, int point, String dateTime) {
        this.coffeeName = coffeeName;
        this.imageId = imageId;
        this.point = point;
        this.dateTime = dateTime;
    }

    public RedeemItem(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.coffeeName = cursor.getString(1);
        this.dateTime = cursor.getString(2);
        this.point = cursor.getInt(3);
        this.imageId = cursor.getInt(4);


    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }



    public int getCoffeeId() {
        return id;
    }
}
