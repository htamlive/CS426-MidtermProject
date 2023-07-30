package model;

import adapter.LoyaltyCupAdapter;
import adapter.RecyclerLoyaltyCupAdapter;
import android.content.Context;
import android.widget.ListView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyCupManager {
    private int count;
    private final List<LoyaltyCup> loyaltyCups;

    // create context
    private final Context context;

    public LoyaltyCupManager(Context context, int maxLoyalCup, int count) {
        if(count > maxLoyalCup)
            throw new IllegalArgumentException("count cannot be greater than maxLoyalCup");
        this.count = count;

        loyaltyCups = new ArrayList<>();
        for (int i = 0; i < maxLoyalCup; i++) {
            loyaltyCups.add(new LoyaltyCup(false));
        }


        this.context = context;
    }

    public int updateLoyalCupAdapter(ListView listView) {

        updateLoyaltyCupArray();

        LoyaltyCupAdapter adapter = new LoyaltyCupAdapter(context, R.layout.loyalty_cup_item, loyaltyCups);

        listView.setAdapter(adapter);

        return count;
    }

    public int updateLoyalCupRecyclerAdapter(RecyclerView recyclerView) {


        RecyclerLoyaltyCupAdapter adapter = new RecyclerLoyaltyCupAdapter(loyaltyCups, R.layout.loyalty_cup_item);

        recyclerView.setAdapter(adapter);

        return count;
    }



    private void updateLoyaltyCupArray() {
        for (int i = 0; i < loyaltyCups.size(); i++) {
            loyaltyCups.get(i).setLoyaltyCup(i < count);
        }
    }

    public int getCount() {
        return count;
    }

    public void addLoyalCup() {
        count++;
    }

    public void resetLoyalCup() {
        count = 0;
    }

    public void setCount(int count) {

        this.count = count;
        updateLoyaltyCupArray();
    }

    public int getLoyaltyCupCountMax() {
        return loyaltyCups.size();
    }
}
