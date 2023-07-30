package adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import contract.RedeemContract;
import model.RedeemItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerRedeemAdapter extends RecyclerView.Adapter<RecyclerRedeemAdapter.ViewHolder>{

    List<RedeemItem> redeemItemList;
    RedeemContract redeemContract;

    public  RecyclerRedeemAdapter(List<RedeemItem> redeemItemList, RedeemContract redeemContract){
        this.redeemItemList = redeemItemList;
        this.redeemContract = redeemContract;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.redeem_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        RedeemItem redeemItem = redeemItemList.get(position);
        holder.ivRedeemCoffee.setImageResource(redeemItem.getImageId());
        holder.tvCartCoffeeName.setText(redeemItem.getCoffeeName());
        @SuppressLint("DefaultLocale") String formattedPoint = String.format("%d pts", redeemItem.getPoint());
        holder.btnRedeemCoffeePoint.setText(formattedPoint);
        holder.btnRedeemCoffeePoint.setOnClickListener(v -> redeemContract.onRedeemClick(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        if (redeemItemList == null) return 0;
        return redeemItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRedeemCoffee;
        TextView tvCartCoffeeName;
        Button btnRedeemCoffeePoint;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivRedeemCoffee = itemView.findViewById(R.id.ivRedeemCoffee);
            tvCartCoffeeName = itemView.findViewById(R.id.tvCartCoffeeName);
            btnRedeemCoffeePoint = itemView.findViewById(R.id.btnRedeemCoffeePoint);
        }
    }
}
