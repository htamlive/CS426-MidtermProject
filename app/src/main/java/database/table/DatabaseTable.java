package database.table;

import android.database.sqlite.SQLiteDatabase;

public abstract class DatabaseTable {
    public abstract void createSampleData(SQLiteDatabase sqLiteDatabase);
    public abstract void createTable(SQLiteDatabase sqLiteDatabase);

    String tableName;

    public DatabaseTable(String tableName) {
        this.tableName = tableName;
    }


}
