package database.table;

import android.database.sqlite.SQLiteDatabase;

public class LoyaltyScore extends DatabaseTable {
    public LoyaltyScore(String tableName) {
        super(tableName);
    }

    @Override
    public void createSampleData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "insert into loyaltyScore(loyaltyScore,purchaseID,cummulativeGift) values " +
                        "(12,1,0)," +
                        "(24,2,0)," +
                        "(35,3,0)," +
                        "(46,4,0)," +
                        "(53,5,0)," +
                        "(120,6,0)," +
                        "(240,null,0)," +
                        "(250,null,8)," +
                        "(200,null,0)," +
                        "(230,7,0)," +
                        "(300,null,0)," +
                        "(310,8,0)," +
                        "(320,null,8)," +
                        "(352,9,0)"

        );

    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + tableName + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "loyaltyScore INTEGER," +
                "purchaseID INTEGER," +
                "cummulativeGift INTEGER," +
                "check(purchaseID is null or cummulativeGift = 0)," +
                "check(cummulativeGift >= 0)," +
                "check(loyaltyScore >= 0)," +
                "FOREIGN KEY(purchaseID) REFERENCES purchase(id)" +
                ")"
        );
    }

    // create trigger that check if the new row has cummulativeGift = 1, then the previous row must have loyaltyScore <= loyaltyScore of the new row
    public void createTrigger(SQLiteDatabase sqLiteDatabase){
//        sqLiteDatabase.execSQL(
//                "create trigger loyaltyScoreTrigger after insert on loyaltyScore " +
//                "begin " +
//                "if (select cummulativeGift from loyaltyScore where ID = new.ID) = 1 then " +
//                "update loyaltyScore set cummulativeGift = 1 where loyaltyScore <= (select loyaltyScore from loyaltyScore where ID = new.ID) and cummulativeGift = 0; " +
//                "end if; " +
//                "end;");
    }
}
