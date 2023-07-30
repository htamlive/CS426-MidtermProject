package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class RecyclerCoffeeCartAdapter extends RecyclerView.Adapter<RecyclerCoffeeCartAdapter.ViewHolder> {
    private final List<CoffeeCupOrder> orderCoffeeList;
    private final Context context;
    private final int layoutId;
    public RecyclerCoffeeCartAdapter(List<CoffeeCupOrder> orderCoffeeList, Context context, int layoutId) {
        this.orderCoffeeList = orderCoffeeList;
        this.context = context;
        this.layoutId = layoutId;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        CoffeeCupOrder coffeeCupOrder = orderCoffeeList.get(position);
        if(coffeeCupOrder == null)
            return;

        holder.tvCartCoffeeName.setText(coffeeCupOrder.getCoffeeName());
        holder.tvCartCoffeeOrderDescription.setText(coffeeCupOrder.getCoffeeDescription());
        holder.ivCartCoffee.setImageResource(coffeeCupOrder.getCoffeeImageId());

        @SuppressLint("DefaultLocale") String formattedQuantity = String.format("x %d", coffeeCupOrder.getQuantity());

        holder.tvCartCoffeeQuantity.setText(formattedQuantity);

        @SuppressLint("DefaultLocale") String formattedPrice = String.format("$%.2f", coffeeCupOrder.getCurrentTotalPrice());
        if(coffeeCupOrder.getCurrentTotalPrice() == 0)
            formattedPrice = "Free";
        holder.tvCartCoffeePrice.setText(formattedPrice);

    }

    @Override
    public int getItemCount() {
        if (orderCoffeeList == null)
            return 0;

        return orderCoffeeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCartCoffee;
        TextView tvCartCoffeeName, tvCartCoffeeOrderDescription, tvCartCoffeeQuantity, tvCartCoffeePrice;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivCartCoffee = itemView.findViewById(R.id.ivCartCoffee);
            tvCartCoffeeName = itemView.findViewById(R.id.tvCartCoffeeName);
            tvCartCoffeeOrderDescription = itemView.findViewById(R.id.tvCartCoffeeDescription);
            tvCartCoffeeQuantity = itemView.findViewById(R.id.tvCartCoffeeQuantity);
            tvCartCoffeePrice = itemView.findViewById(R.id.tvCartCoffeePrice);

        }
    }
}
