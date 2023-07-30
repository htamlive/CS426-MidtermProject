package model;

import android.database.Cursor;

public class HistoryReward {
    private String coffeeName;
    // store date
    private String dateTime;
    private Integer points;

    public HistoryReward(String coffeeName, String dateTime, Integer points) {
        this.coffeeName = coffeeName;
        this.dateTime = dateTime;
        this.points = points;
    }

    public HistoryReward(Cursor cursor) {
        this.coffeeName = cursor.getString(0);
        this.dateTime = cursor.getString(1);
        this.points = cursor.getInt(2);

    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Integer getPoints() {
        return points;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
