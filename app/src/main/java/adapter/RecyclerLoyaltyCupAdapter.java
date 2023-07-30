package adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import model.LoyaltyCup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerLoyaltyCupAdapter extends RecyclerView.Adapter<RecyclerLoyaltyCupAdapter.LoyalCupViewHolder> {
    private final List<LoyaltyCup> loyaltyCups;
    private final int layoutResourceId;

    public RecyclerLoyaltyCupAdapter(List<LoyaltyCup> loyaltyCups, int layoutResourceId) {
        this.loyaltyCups = loyaltyCups;
        this.layoutResourceId = layoutResourceId;
    }

    @NonNull
    @NotNull
    @Override
    public LoyalCupViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), layoutResourceId, null);
        return new LoyalCupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LoyalCupViewHolder holder, int position) {
        LoyaltyCup loyaltyCup = loyaltyCups.get(position);
        if (loyaltyCup != null) {
            holder.ivLoyaltyCup.setImageResource(loyaltyCup.isLoyaltyCup() ? R.drawable.loyalty_cup : R.drawable.loyalty_cup_gray);
        }

    }

    @Override
    public int getItemCount() {
        if(loyaltyCups == null){
            return 0;
        }
        return loyaltyCups.size();
    }

    public static class LoyalCupViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivLoyaltyCup;
        public LoyalCupViewHolder(View view) {
            super(view);
            ivLoyaltyCup = view.findViewById(R.id.ivLoyaltyCup);

        }
    }
}
