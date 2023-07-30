package adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import model.CoffeeCupOrder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerPurchaseDetailAdapter extends RecyclerView.Adapter<RecyclerPurchaseDetailAdapter.ViewHolder> {

    List<CoffeeCupOrder> coffeeCupOrderList;
    public RecyclerPurchaseDetailAdapter(List<CoffeeCupOrder> coffeeCupOrderList) {
        this.coffeeCupOrderList = coffeeCupOrderList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerPurchaseDetailAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerPurchaseDetailAdapter.ViewHolder holder, int position) {
        CoffeeCupOrder coffeeCupOrder = coffeeCupOrderList.get(position);
        if(coffeeCupOrder == null)
            return;

        holder.tvPurchaseDetailCoffeeName.setText(coffeeCupOrder.getCoffeeName());
        holder.tvPurchaseDetailCoffeeOrderDescription.setText(coffeeCupOrder.getCoffeeDescription());
        holder.ivPurchaseDetailCoffee.setImageResource(coffeeCupOrder.getCoffeeImageId());

        @SuppressLint("DefaultLocale") String formattedQuantity = String.format("x %d", coffeeCupOrder.getQuantity());

        holder.tvPurchaseDetailCoffeeQuantity.setText(formattedQuantity);

        @SuppressLint("DefaultLocale") String formattedPrice = String.format("$%.2f", coffeeCupOrder.getCurrentTotalPrice());
        if(coffeeCupOrder.getCurrentTotalPrice() == 0)
            formattedPrice = "Free";
        holder.tvPurchaseDetailCoffeePrice.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        if (coffeeCupOrderList == null)
            return 0;

        return coffeeCupOrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPurchaseDetailCoffee;
        TextView tvPurchaseDetailCoffeeName, tvPurchaseDetailCoffeeOrderDescription, tvPurchaseDetailCoffeeQuantity, tvPurchaseDetailCoffeePrice;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivPurchaseDetailCoffee = itemView.findViewById(R.id.ivPurchaseDetailCoffee);
            tvPurchaseDetailCoffeeName = itemView.findViewById(R.id.tvPurchaseDetailCoffeeName);
            tvPurchaseDetailCoffeeOrderDescription = itemView.findViewById(R.id.tvPurchaseDetailCoffeeDescription);
            tvPurchaseDetailCoffeeQuantity = itemView.findViewById(R.id.tvPurchaseDetailCoffeeQuantity);
            tvPurchaseDetailCoffeePrice = itemView.findViewById(R.id.tvPurchaseDetailCoffeePrice);

        }
    }
}
