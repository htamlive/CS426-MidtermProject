package database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.a21125060.R;
import model.CoffeeDetail;

public class CoffeeTable extends DatabaseTable {


    public CoffeeTable(String tableName) {
        super(tableName);

    }

    @Override
    public void createSampleData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "insert into coffee(name,defaultprice,imageId,description) values" +
                        "('Espresso',2.5,"+ R.drawable.espresso +",'Espresso is a coffee-brewing method of Italian origin, in which a small amount of nearly boiling water is forced under pressure through finely-ground coffee beans.')," +
                        "('Americano',3.5,"+ R.drawable.americano +",'Caffè Americano or Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavor from, traditionally brewed coffee.')," +
                        "('Cappuccino',4.5,"+ R.drawable.cappuccino +",'A cappuccino is an espresso-based coffee drink that originated in Italy, and is traditionally prepared with steamed milk foam.')," +
                        "('Latte',4.5,"+ R.drawable.latte +",'Caffè latte is a coffee drink made with espresso and steamed milk.')," +
                        "('Mocha',5.5,"+ R.drawable.mocha +",'Caffè mocha, in its most basic formulation, can also be referred to as hot chocolate with (e.g., a shot of) espresso added.')," +
                        "('Flat White',5.5,"+ R.drawable.flat_white +",'A flat white is a coffee drink consisting of espresso with microfoam (steamed milk with small, fine bubbles and a glossy or velvety consistency).')"
        );
    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table coffee(" +
                        "id integer primary key autoincrement," +
                        "name text," +
                        "defaultprice real," +
                        "imageId integer," +
                        "description text," +
                        "check (defaultprice >= 0)" +
                        ")"
        );
    }

    public CoffeeDetail getCoffeeDetailFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount() == 0) return null;
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        float defaultPrice = cursor.getFloat(2);
        int imageId = cursor.getInt(3);
        String description = cursor.getString(4);
//        System.out.println("id: " + id + " name: " + name + " defaultPrice: " + defaultPrice + " imageId: " + imageId + " description: " + description);
        return new CoffeeDetail(id, name, description, imageId, defaultPrice);
    }
}
