package model;

import android.content.Intent;
import org.jetbrains.annotations.NotNull;

public class CoffeeCupOrder {
    public static final int MAX_COFFEE_COUNT = 50;
    public static final int MIN_COFFEE_COUNT = 1;
    public static final int LARGE_COFFEE_SIZE = 2;
    public static final int MEDIUM_COFFEE_SIZE = 1;
    public static final int SMALL_COFFEE_SIZE = 0;
    public static final int ICE_NO = 0;
    public static final int ICE_LESS = 1;
    public static final int ICE_NORMAL = 2;
    public static final int ICE_MORE = 3;
    public static final int SHOT_NONE = 0;
    public static final int SHOT_SINGLE = 1;
    public static final int SHOT_DOUBLE = 2;
    public static final int PLACE_HERE = 0;
    public static final int PLACE_TAKE_AWAY = 1;
    public static final int UNPAID = -1;
    public static final int ON_GOING = 0;
    public static final int FINISHED = 1;
    public static final int CANCELLED = -1;
    int currentCoffeeCount = 1, currentCoffeeSize = 0, currentCoffeeIce = 0, currentCoffeeShot = 0, currentCoffeePlace = 0;
    int orderId, coffeeId, purchaseId;
    float currentTotalPrice = 0;
    String coffeeName;
    int imgId;
    public CoffeeCupOrder(int orderId, int coffeeId, String coffeeName, int quantity, String size, String ice, int shot, int useAtPlace, float price, int imageId) {
        this.orderId = orderId;
        this.coffeeId = coffeeId;
        this.coffeeName = coffeeName;
        this.currentCoffeeCount = quantity;
        this.currentCoffeeSize = size.equals("S") ? SMALL_COFFEE_SIZE : size.equals("M") ? MEDIUM_COFFEE_SIZE : LARGE_COFFEE_SIZE;
        this.currentCoffeeIce = ice.equals("no") ? ICE_NO : ice.equals("less") ? ICE_LESS : ice.equals("medium") ? ICE_NORMAL : ICE_MORE;
        this.currentCoffeeShot = shot;
        this.currentCoffeePlace = useAtPlace;
        this.currentTotalPrice = price;
        this.imgId = imageId;
        this.purchaseId = UNPAID;
    }

    public CoffeeCupOrder(int orderId, int coffeeId, int purchaseId, String coffeeName, int quantity, String size, String ice, int shot, int useAtPlace, float price, int imageId) {
        this.orderId = orderId;
        this.coffeeId = coffeeId;
        this.coffeeName = coffeeName;
        this.currentCoffeeCount = quantity;
        this.currentCoffeeSize = size.equals("S") ? SMALL_COFFEE_SIZE : size.equals("M") ? MEDIUM_COFFEE_SIZE : LARGE_COFFEE_SIZE;
        this.currentCoffeeIce = ice.equals("no") ? ICE_NO : ice.equals("less") ? ICE_LESS : ice.equals("medium") ? ICE_NORMAL : ICE_MORE;
        this.currentCoffeeShot = shot;
        this.currentCoffeePlace = useAtPlace;
        this.currentTotalPrice = price;
        this.imgId = imageId;
        this.purchaseId = purchaseId;
    }

    public CoffeeCupOrder(int coffeeId) {

        this.coffeeId = coffeeId;
        this.purchaseId = UNPAID;
    }


    public float syncCurrentTotalPrice(float coffeeDefaultPrice) {
        currentTotalPrice = coffeeDefaultPrice;
        currentTotalPrice += currentCoffeeSize * 0.5;
        currentTotalPrice += currentCoffeeIce * 0.5;
        currentTotalPrice += currentCoffeeShot * 0.5;
        currentTotalPrice *= currentCoffeeCount;
        return currentTotalPrice;
    }
    

    public int getCoffeeID(){
        return coffeeId;
    }
    public int getCurrentCoffeeCount() {
        return currentCoffeeCount;
    }
    
    public void setCurrentCoffeeCount(int currentCoffeeCount) {
        this.currentCoffeeCount = currentCoffeeCount;
    }
    
    public int getCurrentCoffeeSize() {
        return currentCoffeeSize;
    }

    public  String getConvertedSize(){
        if(currentCoffeeSize == 0){
            return "S";
        }else if(currentCoffeeSize == 1){
            return "M";
        }else{
            return "L";
        }
    }

    public String getConvertedIce(){
        if(currentCoffeeIce == 0){
            return "no";
        }else if(currentCoffeeIce == 1){
            return "less";
        }else if(currentCoffeeIce == 2){
            return "medium";
        }else{
            return "full";
        }
    }
    
    public void setCurrentCoffeeSize(int currentCoffeeSize) {
        this.currentCoffeeSize = currentCoffeeSize;
    }
    
    public int getCurrentCoffeeIce() {
        return currentCoffeeIce;
    }
    
    public void setCurrentCoffeeIce(int currentCoffeeIce) {
        this.currentCoffeeIce = currentCoffeeIce;
    }
    
    public int getCurrentCoffeeShot() {
        return currentCoffeeShot;
    }
    
    public void setCurrentCoffeeShot(int currentCoffeeShot) {
        this.currentCoffeeShot = currentCoffeeShot;
    }
    
    public int getCurrentCoffeePlace() {
        return currentCoffeePlace;
    }
    
    public void setCurrentCoffeePlace(int currentCoffeePlace) {
        this.currentCoffeePlace = currentCoffeePlace;
    }



    public void transferOrderDataTo(Intent intent) {
//        @SuppressLint("DefaultLocale") String formattedName = String.format("%s x %d", coffeeName, getCurrentCoffeeCount());
        String formattedOption = FormattingOption("");
        intent.putExtra("coffeeOption", formattedOption);
        intent.putExtra("coffeeCount", getCurrentCoffeeCount());
        intent.putExtra("coffeePrice", currentTotalPrice);
        intent.putExtra("coffeeSize", getCurrentCoffeeSize());
        intent.putExtra("coffeeIce", getCurrentCoffeeIce());
        intent.putExtra("coffeeShot", getCurrentCoffeeShot());
        intent.putExtra("coffeePlace", getCurrentCoffeePlace());
    }

    @NotNull
    private String FormattingOption(String formattedOption) {
        formattedOption = displaySize(formattedOption);
        formattedOption = displayIce(formattedOption);
        formattedOption = displayShot(formattedOption);
        return formattedOption;
    }

    private String displayShot(String formattedOption) {
        int currentCoffeeShot = this.getCurrentCoffeeShot();
        if (currentCoffeeShot == 1) {
            formattedOption += ", Single Shot";
        } else if (currentCoffeeShot == 2) {
            formattedOption += ", Double Shot";
        }
        return formattedOption;
    }

    @NotNull
    private String displayIce(String formattedOption) {
        int currentCoffeeIce = this.getCurrentCoffeeIce();
        if (currentCoffeeIce == 0) {
            formattedOption += ", Less Ice";
        } else if (currentCoffeeIce == 1) {
            formattedOption += ", Half Ice";
        } else {
            formattedOption += ", Full Ice";
        }
        return formattedOption;
    }

    @NotNull
    private String displaySize(String formattedOption) {
        int currentCoffeeSize = this.getCurrentCoffeeSize();
        if (currentCoffeeSize == 0) {
            formattedOption += "Small";
        } else if (currentCoffeeSize == 1) {
            formattedOption += "Medium";
        } else {
            formattedOption += "Large";
        }
        return formattedOption;
    }


    public String getCoffeeName() {
        return coffeeName;
    }

    public int getCoffeeImageId() {
        return imgId;
    }

    public String getCoffeeDescription() {
        return FormattingOption("");
    }

    public int getQuantity() {
        return currentCoffeeCount;
    }

    public float getCurrentTotalPrice() {
        return currentTotalPrice;
    }


    public int getOrderId() {
        return orderId;
    }
}
