package database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PurchaseTable extends DatabaseTable {

    public PurchaseTable(String tableName) {
        super(tableName);
    }

    @Override
    public void createSampleData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "INSERT INTO " + tableName + "(buyDate, finished, shopAddress, delivery) VALUES " +
                        "('2020-01-01 08:00:00', 1, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-02 08:00:00', 1, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-03 08:00:00', 1, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-04 08:00:00', 1, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-05 08:00:00', 1, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-06 08:00:00', 1, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-07 08:00:00', 1, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-08 08:00:00', 0, '3 Addersion Court Chino Hills, HO56824, United State',0)," +
                        "('2020-01-09 08:00:00', 0, '3 Addersion Court Chino Hills, HO56824, United State',0)"
        );
    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + tableName + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "buyDate TEXT," +
                "finished INTEGER," +
                "shopAddress TEXT," +
                "delivery INTEGER," +
                "check(finished in (-1,0,1))," +
                "check(delivery in (0,1))," +
                "check (buyDate like '____-__-__ __:__:__')," +
                // check buyDate numeric
                "check (substr(buyDate,1,4) between '0000' and '9999')," +
                "check (substr(buyDate,6,2) between '01' and '12')," +
                "check (substr(buyDate,9,2) between '01' and '31')," +
                "check (substr(buyDate,12,2) between '00' and '23')," +
                "check (substr(buyDate,15,2) between '00' and '59')," +
                "check (substr(buyDate,18,2) between '00' and '59')," +
                //check year month day
                "check (substr(buyDate,6,2) in ('01','03','05','07','08','10','12') or (substr(buyDate,6,2) in ('04','06','09','11') and substr(buyDate,9,2) <= '30') or (substr(buyDate,6,2) = '02' and substr(buyDate,9,2) <= '28'))" +
                ")");

        // add trigger for checking buy date is not in the future
        sqLiteDatabase.execSQL("CREATE TRIGGER check_buy_date BEFORE INSERT ON " + tableName + " FOR EACH ROW BEGIN SELECT CASE WHEN (datetime('now') < NEW.buyDate) THEN RAISE(ABORT, 'Buy date cannot be in the future') END; END;");
    }

    public static class PurchaseBrief {
        public int ID;
        public String buyDate;
        public String shopAddress;
        public int delivery;
        public float totalPrice;

        public PurchaseBrief(int ID, String buyDate, String shopAddress, int delivery, float totalPrice) {
            this.ID = ID;
            this.buyDate = buyDate;
            this.shopAddress = shopAddress;
            this.delivery = delivery;
            this.totalPrice = totalPrice;
        }

        public PurchaseBrief(Cursor cursor){
            this.ID = cursor.getInt(0);
            this.buyDate = cursor.getString(1);
            this.shopAddress = cursor.getString(2);
            this.delivery = cursor.getInt(3);
            this.totalPrice = cursor.getFloat(4);
        }


        public int getPurchaseId() {
            return ID;

        }
    }
}
