package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import database.table.*;
import model.CoffeeCupOrder;
import model.CoffeeDetail;
import model.HistoryReward;
import model.RedeemItem;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "coffee.db";

    private final CoffeeTable coffeeTable;
    private final PurchaseTable purchaseTable;
    private final LoyaltyScore loyaltyScore;
    private final PurchaseDetailTable purchaseDetailTable;
    private final RedeemCoffeeTable redeemCoffeeTable;
    public static final String TABLE_COFFEE = "coffee";
    public static final String TABLE_PURCHASE = "purchase";
    public static final String TABLE_LOYALTY_SCORE = "loyaltyScore";
    public static final String TABLE_PURCHASE_DETAIL = "purchaseDetail";
    public static final String TABLE_REDEEM_COFFEE = "redeemCoffee";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        coffeeTable = new CoffeeTable(TABLE_COFFEE);
        purchaseTable = new PurchaseTable(TABLE_PURCHASE);
        loyaltyScore = new LoyaltyScore(TABLE_LOYALTY_SCORE);
        purchaseDetailTable = new PurchaseDetailTable(TABLE_PURCHASE_DETAIL);
        redeemCoffeeTable = new RedeemCoffeeTable(TABLE_REDEEM_COFFEE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        coffeeTable.createTable(sqLiteDatabase);
        coffeeTable.createSampleData(sqLiteDatabase);

        purchaseTable.createTable(sqLiteDatabase);
        purchaseTable.createSampleData(sqLiteDatabase);

        purchaseDetailTable.createTable(sqLiteDatabase);
        purchaseDetailTable.createSampleData(sqLiteDatabase);

        loyaltyScore.createTable(sqLiteDatabase);
        loyaltyScore.createSampleData(sqLiteDatabase);

        redeemCoffeeTable.createTable(sqLiteDatabase);
        redeemCoffeeTable.createSampleData(sqLiteDatabase);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // drop table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COFFEE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_PURCHASE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_LOYALTY_SCORE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_PURCHASE_DETAIL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_REDEEM_COFFEE);
        onCreate(sqLiteDatabase);

    }

    public List<CoffeeDetail> getAllCoffee() {
        List<CoffeeDetail> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_COFFEE, null);
//        System.out.println("cursor.getCount() = " + cursor.getCount());

        while (cursor.moveToNext()) {
            result.add(coffeeTable.getCoffeeDetailFromCursor(cursor));
        }
//        System.out.println("result.size() = " + result.size());
        cursor.close();
        return result;
    }


    public boolean addToCart(CoffeeCupOrder currentCoffeeCupOrder) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        // use transaction
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL("insert into "+TABLE_PURCHASE_DETAIL+"(coffeeID,quantity,price,ice,shot,size) values(" +
                    currentCoffeeCupOrder.getCoffeeID() + "," +
                    currentCoffeeCupOrder.getCurrentCoffeeCount() + "," +
                    currentCoffeeCupOrder.getCurrentTotalPrice() + "," +
                    "'" + currentCoffeeCupOrder.getConvertedIce() + "'" + "," +
                    currentCoffeeCupOrder.getCurrentCoffeeShot() + "," +
                    "'" + currentCoffeeCupOrder.getConvertedSize() + "'" + ")");
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return true;
    }

    public List<CoffeeCupOrder> getAllOrderOnCart() {
        List<CoffeeCupOrder> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select " +
                        TABLE_COFFEE + ".name, " +
                        TABLE_PURCHASE_DETAIL + ".coffeeId, " +
                        TABLE_PURCHASE_DETAIL + ".quantity, " +
                        TABLE_PURCHASE_DETAIL + ".price, " +
                        TABLE_PURCHASE_DETAIL + ".purchaseId, " +
                        TABLE_PURCHASE_DETAIL + ".shot, " +
                        TABLE_PURCHASE_DETAIL + ".size, " +
                        TABLE_PURCHASE_DETAIL + ".ice, " +
                        TABLE_PURCHASE_DETAIL + ".useAtPlace, " +
                        TABLE_COFFEE + ".imageId, " +
                        TABLE_PURCHASE_DETAIL + ".ID " +

                        "from " + TABLE_PURCHASE_DETAIL +
                        " join " + TABLE_COFFEE +
                        " on " + TABLE_PURCHASE_DETAIL + ".coffeeId = " + TABLE_COFFEE + ".ID" +
                        " where purchaseID is null"
                , null);

        while (cursor.moveToNext()) {
            CoffeeCupOrder coffeeCupOrder = purchaseDetailTable.getCoffeeCupOrderFromCursor(cursor, false);
            result.add(coffeeCupOrder);
            // debug
            System.out.println("coffeeCupOrder = " + coffeeCupOrder);
        }
        cursor.close();
        return result;
    }

    public List<CoffeeCupOrder> getAllOrderFromPurchaseId(int purchaseId){
        List<CoffeeCupOrder> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select " +
                        TABLE_COFFEE + ".name, " +
                        TABLE_PURCHASE_DETAIL + ".coffeeId, " +
                        TABLE_PURCHASE_DETAIL + ".quantity, " +
                        TABLE_PURCHASE_DETAIL + ".price, " +
                        TABLE_PURCHASE_DETAIL + ".purchaseId, " +
                        TABLE_PURCHASE_DETAIL + ".shot, " +
                        TABLE_PURCHASE_DETAIL + ".size, " +
                        TABLE_PURCHASE_DETAIL + ".ice, " +
                        TABLE_PURCHASE_DETAIL + ".useAtPlace, " +
                        TABLE_COFFEE + ".imageId, " +
                        TABLE_PURCHASE_DETAIL + ".ID " +

                        "from " + TABLE_PURCHASE_DETAIL +
                        " join " + TABLE_COFFEE +
                        " on " + TABLE_PURCHASE_DETAIL + ".coffeeId = " + TABLE_COFFEE + ".ID" +
                        " where purchaseID = " + purchaseId
                , null);

        while (cursor.moveToNext()) {
            CoffeeCupOrder coffeeCupOrder = purchaseDetailTable.getCoffeeCupOrderFromCursor(cursor, true);
            result.add(coffeeCupOrder);
            // debug
            System.out.println("coffeeCupOrder = " + coffeeCupOrder);
        }
        cursor.close();
        return result;
    }

    public int getCurrentLoyaltyScore(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // get the value of the last row
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select " +
                        TABLE_LOYALTY_SCORE + ".loyaltyScore " +
                        "from " + TABLE_LOYALTY_SCORE +
                        " order by " + TABLE_LOYALTY_SCORE + ".ID desc ",
                null);
        cursor.moveToFirst();
        int loyaltyScore = cursor.getInt(0);
        cursor.close();
        return loyaltyScore;
    }

    public void payAllOrderOnCart(String shopAddress, int delivery) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            // get current date as format ISO8601 YYYY-MM-DD HH:MM:SS
            String currentDate = getCurrentDate();

            createPurchase(shopAddress, delivery, sqLiteDatabase, currentDate);

            Cursor cursor = sqLiteDatabase.rawQuery("select last_insert_rowid()", null);
            cursor.moveToFirst();
            int purchaseId = cursor.getInt(0);
            cursor.close();
            sqLiteDatabase.execSQL("update "+TABLE_PURCHASE_DETAIL+" set purchaseId = " + purchaseId + " where purchaseId is null");
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private static void createPurchase(String shopAddress, int delivery, SQLiteDatabase sqLiteDatabase, String currentDate) {
        sqLiteDatabase.execSQL("insert into "+TABLE_PURCHASE+"(buyDate,finished,shopAddress,delivery) values(" +
                "'" + currentDate + "'" + "," +
                "0," +
                "'" + shopAddress + "'" + "," +
                delivery + ")");
    }

    private String getCurrentDate() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select datetime('now')", null);
        cursor.moveToFirst();
        String currentDate = cursor.getString(0);
        cursor.close();
        return currentDate;
    }

    public void deleteOrderOnCart(int orderId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+TABLE_PURCHASE_DETAIL+" where ID = " + orderId);
    }

    public List<PurchaseTable.PurchaseBrief> getPurchaseWithTotalPrice(int finished) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select " +
                        TABLE_PURCHASE + ".ID," +
                        TABLE_PURCHASE + ".buyDate," +
                        TABLE_PURCHASE + ".shopAddress," +
                        TABLE_PURCHASE + ".delivery," +
                        "sum(" + TABLE_PURCHASE_DETAIL + ".price) as totalPrice" +
                " from " + TABLE_PURCHASE +
                " join " + TABLE_PURCHASE_DETAIL +
                " on " + TABLE_PURCHASE + ".ID = " + TABLE_PURCHASE_DETAIL + ".purchaseId" +
                " group by " +
                        TABLE_PURCHASE + ".ID," +
                        TABLE_PURCHASE + ".buyDate," +
                        TABLE_PURCHASE + ".shopAddress," +
                        TABLE_PURCHASE + ".delivery," +
                        TABLE_PURCHASE + ".finished" +
                " having " + TABLE_PURCHASE + ".finished = " + finished +
                " order by " + TABLE_PURCHASE + ".buyDate desc"

                , null);


        List<PurchaseTable.PurchaseBrief> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(new PurchaseTable.PurchaseBrief(cursor));

        }
        System.out.println("result.size() = " + result.size());
        cursor.close();
        return result;
    }

    public void updatePurchaseStatusFinished(int purchaseId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("update "+TABLE_PURCHASE+" set finished = 1 where ID = " + purchaseId);

    }

    public List<RedeemItem> getAllRedeemCoffee() {
        List<RedeemItem> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select " +
                        TABLE_REDEEM_COFFEE + ".ID, " +
                        TABLE_COFFEE + ".name, " +
                        TABLE_REDEEM_COFFEE + ".untilDate, " +
                        TABLE_REDEEM_COFFEE + ".points, " +
                        TABLE_COFFEE + ".imageId " +
                "from " + TABLE_REDEEM_COFFEE +" " +
                "join " + TABLE_COFFEE + " " +
                "on " + TABLE_REDEEM_COFFEE + ".coffeeId = " + TABLE_COFFEE + ".ID " +
                "where untilDate > datetime('now')", null);

        while (cursor.moveToNext()) {
            result.add(new RedeemItem(cursor));
        }
        cursor.close();
        return result;
    }


    public boolean useRedeem(CoffeeCupOrder coffeeCupOrder, String shopAddress, int delivery, int loyaltyScore) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            String currentDate = getCurrentDate();
            int currentLoyaltyScore = getCurrentLoyaltyScore();

            if (currentLoyaltyScore < loyaltyScore) {
                return false;
            }

            transactLoyaltyScore(sqLiteDatabase, currentLoyaltyScore - loyaltyScore, 0);

            createPurchase(shopAddress, delivery, sqLiteDatabase, currentDate);
            Cursor cursor = sqLiteDatabase.rawQuery("select last_insert_rowid()", null);
            cursor.moveToFirst();
            int purchaseId = cursor.getInt(0);
            System.out.println("purchaseId = " + purchaseId);
            System.out.println("coffeeId = " + coffeeCupOrder.getCoffeeID());
            System.out.println("quantity = " + coffeeCupOrder.getCurrentCoffeeCount());
            System.out.println("price = " + coffeeCupOrder.getCurrentTotalPrice());
            System.out.println("ice = " + coffeeCupOrder.getConvertedIce());
            System.out.println("shot = " + coffeeCupOrder.getCurrentCoffeeShot());
            System.out.println("size = " + coffeeCupOrder.getConvertedSize());
            System.out.println("purchaseId = " + purchaseId);
            cursor.close();

            sqLiteDatabase.execSQL("insert into "+TABLE_PURCHASE_DETAIL+"(coffeeID,quantity,price,ice,shot,size,purchaseId) values(" +
                    coffeeCupOrder.getCoffeeID() + "," +
                    coffeeCupOrder.getCurrentCoffeeCount() + "," +
                    coffeeCupOrder.getCurrentTotalPrice() + "," +
                    "'" + coffeeCupOrder.getConvertedIce() + "'" + "," +
                    coffeeCupOrder.getCurrentCoffeeShot() + "," +
                    "'" + coffeeCupOrder.getConvertedSize() + "'" + "," +
                    purchaseId + ")");
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            sqLiteDatabase.endTransaction();
        }
        return true;

    }
    private static void transactLoyaltyScore(SQLiteDatabase sqLiteDatabase, int newLoyaltyScore, int cumulativeGift) {
        sqLiteDatabase.execSQL(
                "insert into "+TABLE_LOYALTY_SCORE+"(loyaltyScore,purchaseID,cummulativeGift) values(" +
                        newLoyaltyScore + "," +
                        "null" + "," +
                        cumulativeGift +
                        ")"
        );
    }

    public boolean transactLoyaltyScore(int currentLoyaltyCup, int loyaltyCupToUse, int bonusLoyaltyScore) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        int currentLoyaltyScore = getCurrentLoyaltyScore();
        int newLoyaltyScore = currentLoyaltyScore + bonusLoyaltyScore;

        int delta = currentLoyaltyCup - loyaltyCupToUse;

        if(delta < 0)
            return false;
        transactLoyaltyScore(sqLiteDatabase, newLoyaltyScore, loyaltyCupToUse);
        return true;
    }

    public int getLoyaltyCupCount() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select sum(quantity) " +
                        "from " + TABLE_PURCHASE_DETAIL +
                        " where purchaseID is not null"
                , null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getAccumulativeGiftCount() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select sum(cummulativeGift) " +
                        "from " + TABLE_LOYALTY_SCORE
                , null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public List<HistoryReward> getHistoryRewards() {
        List<HistoryReward> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select " +
                        TABLE_COFFEE + ".name, " +
                        TABLE_PURCHASE + ".buyDate, " +
                        TABLE_PURCHASE_DETAIL + ".quantity " +
                        "from " + TABLE_PURCHASE +
                        " join " + TABLE_PURCHASE_DETAIL +
                        " on " + TABLE_PURCHASE + ".ID = " + TABLE_PURCHASE_DETAIL + ".purchaseId" +
                        " join " + TABLE_COFFEE +
                        " on " + TABLE_PURCHASE_DETAIL + ".coffeeId = " + TABLE_COFFEE + ".ID" +
                        " where " + TABLE_PURCHASE + ".finished = 1" +
                        " order by " + TABLE_PURCHASE + ".buyDate desc"
                , null);

        while (cursor.moveToNext()) {
            result.add(new HistoryReward(cursor));
        }
        cursor.close();
        return result;
    }


}
