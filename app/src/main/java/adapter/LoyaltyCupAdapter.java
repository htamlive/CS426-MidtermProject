package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.example.a21125060.R;
import model.LoyaltyCup;

import java.util.List;

public class LoyaltyCupAdapter extends ArrayAdapter<LoyaltyCup> {
    List<LoyaltyCup> loyaltyCups;
    int layoutResourceId;
    public LoyaltyCupAdapter(@NonNull Context context, int resource, @NonNull List<LoyaltyCup> objects) {
        super(context, resource, objects);
        this.loyaltyCups = objects;
        this.layoutResourceId = resource;
    }

    public static class LoyalCupViewHolder {
        private final ImageView ivLoyaltyCup;
        public LoyalCupViewHolder(View view) {
            ivLoyaltyCup = view.findViewById(R.id.ivLoyaltyCup);

        }
    }

    @Override
    public int getCount() {
        if(loyaltyCups == null)
            return 0;
        return loyaltyCups.size();
    }

    @Override
    public LoyaltyCup getItem(int position) {
        return loyaltyCups.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LoyalCupViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
            holder = new LoyalCupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (LoyalCupViewHolder) convertView.getTag();
        }
        LoyaltyCup loyaltyCup = getItem(position);
        if (loyaltyCup != null) {
            holder.ivLoyaltyCup.setImageResource(loyaltyCup.isLoyaltyCup() ? R.drawable.loyalty_cup : R.drawable.loyalty_cup_gray);
        }

        return convertView;

    }

}
