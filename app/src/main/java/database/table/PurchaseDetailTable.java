package database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.CoffeeCupOrder;

public class PurchaseDetailTable extends DatabaseTable {


    public PurchaseDetailTable(String tableName) {
        super(tableName);
    }

    @Override
    public void createSampleData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "insert into purchaseDetail(coffeeId,quantity,price,purchaseId,shot,size,ice,useAtPlace) values" +
                        "(1,1,2.1,1,1,'S','no',1)," +
                        "(2,1,3.1,1,1,'M','full',0)," +
                        "(3,2,4.5,1,2,'S','full',1)," +
                        "(4,1,3.2,2,1,'M','medium',0)," +
                        "(5,2,4  ,2,2,'S','full',1)," +
                        "(6,1,3.2,2,1,'M','full',0)," +
                        "(1,2,4,2,2,'S','full',1)," +
                        "(2,1,3.2,3,1,'M','medium',1)," +
                        "(3,2,4  ,3,2,'S','no',1)," +
                        "(4,1,3.3,3,1,'M','full',1)," +
                        "(5,2,4  ,3,2,'S','full',1)," +
                        "(6,1,3.3,4,1,'M','medium',1)," +
                        "(2,2,4  ,4,2,'S','full',1)," +
                        "(1,1,3.4,5,1,'M','full',1)," +
                        "(2,2,4  ,5,2,'S','no',1)," +
                        "(2,1,3.5,6,1,'M','full',0)," +
                        "(1,2,4  ,7,2,'S','full',0)," +
                        "(5,1,3.5,8,1,'M','full',1)," +
                        "(1,2,4  ,8,2,'S','medium',0)," +
                        "(2,1,3.6,8,1,'M','full',1)," +
                        "(2,2,4  ,8,2,'S','full',1)," +
                        "(2,1,3.8,9,1,'M','full',0)," +
                        "(3,2,4  ,9,2,'S','medium',0)"
        );

    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + tableName + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "coffeeId INTEGER," +
                "quantity INTEGER," +
                "price REAL," +
                "purchaseId INTEGER," +
                "shot INTEGER," +
                "size TEXT," +
                "ice TEXT," +
                "useAtPlace INTEGER," +
                "check(shot in (0,1,2))," +
                "check(size in ('S','M','L'))," +
                "check(ice in ('no','less','medium','full'))," +
                "FOREIGN KEY(coffeeId) REFERENCES coffee(id)," +
                "FOREIGN KEY(purchaseId) REFERENCES purchase(ID)" +
                ")"
        );

    }

    public CoffeeCupOrder getCoffeeCupOrderFromCursor(Cursor cursor, boolean isPaid) {
        String coffeeName = cursor.getString(0);
        int coffeeId = cursor.getInt(1);
        int quantity = cursor.getInt(2);
        float price = cursor.getFloat(3);
        int purchaseId = cursor.getInt(4);
        int shot = cursor.getInt(5);
        String size = cursor.getString(6);
        String ice = cursor.getString(7);
        int useAtPlace = cursor.getInt(8);
        int imageId = cursor.getInt(9);
        int orderId = cursor.getInt(10);
        if(isPaid){
            return new CoffeeCupOrder(orderId, coffeeId, purchaseId, coffeeName, quantity, size, ice, shot, useAtPlace, price, imageId);
        }
        return new CoffeeCupOrder(orderId, coffeeId, coffeeName, quantity, size, ice, shot, useAtPlace, price, imageId);
    }
}
