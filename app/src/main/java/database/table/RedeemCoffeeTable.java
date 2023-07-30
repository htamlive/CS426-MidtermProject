package database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.RedeemItem;

public class RedeemCoffeeTable extends DatabaseTable{
    public RedeemCoffeeTable(String tableName) {
        super(tableName);
    }

    @Override
    public void createSampleData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("insert into " + tableName + "(CoffeeID, untilDate, points) values " +
                "(1, '2024-05-01 08:00:00', 200)," +
                "(2, '2024-05-02 08:00:00', 900)," +
                "(3, '2024-05-03 08:00:00', 800)," +
                "(4, '2024-05-04 08:00:00', 150)," +
                "(5, '2024-05-05 08:00:00', 500)," +
                "(6, '2024-05-06 08:00:00', 1000)"
        );

    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + tableName + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CoffeeID INTEGER," +
                "untilDate TEXT," +
                "points INTEGER," +
                "FOREIGN KEY(CoffeeID) REFERENCES coffee(id)" +
                "check (untilDate like '____-__-__ __:__:__')," +
                // check untilDate numeric
                "check (substr(untilDate,1,4) between '0000' and '9999')," +
                "check (substr(untilDate,6,2) between '01' and '12')," +
                "check (substr(untilDate,9,2) between '01' and '31')," +
                "check (substr(untilDate,12,2) between '00' and '23')," +
                "check (substr(untilDate,15,2) between '00' and '59')," +
                "check (substr(untilDate,18,2) between '00' and '59')," +
                //check year month day
                "check (substr(untilDate,6,2) in ('01','03','05','07','08','10','12') or (substr(untilDate,6,2) in ('04','06','09','11') and substr(untilDate,9,2) <= '30') or (substr(untilDate,6,2) = '02' and substr(untilDate,9,2) <= '28'))" +
                ")");

    }


    public RedeemItem getRedeemCoffeeFromCursor(Cursor cursor) {
        return null;
    }
}
