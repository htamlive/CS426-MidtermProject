package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import contract.CoffeeBriefContract;
import model.CoffeeDetail;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerCoffeeBriefAdapter extends RecyclerView.Adapter<RecyclerCoffeeBriefAdapter.CoffeeBriefHolder>{
    private List<CoffeeDetail> coffeeDetailList;
    private Context context;
    private final CoffeeBriefContract coffeeBriefContract;

    public RecyclerCoffeeBriefAdapter(List<CoffeeDetail> coffeeDetailList, Context context,CoffeeBriefContract coffeeBriefContract) {
        this.coffeeDetailList = coffeeDetailList;
        this.context = context;
        this.coffeeBriefContract = coffeeBriefContract;
    }




    @NonNull
    @NotNull
    @Override
    public CoffeeBriefHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_item, parent, false);
        return new CoffeeBriefHolder(view, coffeeBriefContract);
    }



    @Override
    public void onBindViewHolder(@NonNull @NotNull CoffeeBriefHolder holder, int position) {
        CoffeeDetail coffeeDetail = coffeeDetailList.get(position);
        holder.tvCoffeeName.setText(coffeeDetail.getName());
        holder.ivCoffee.setImageResource(coffeeDetail.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        if(coffeeDetailList == null) return 0;
        return coffeeDetailList.size();
    }

    public static class CoffeeBriefHolder extends RecyclerView.ViewHolder{
        private final ImageView ivCoffee;
        private final TextView tvCoffeeName;
        private TextView tvCoffeeDescription;

        public CoffeeBriefHolder(@NonNull @NotNull View itemView, CoffeeBriefContract coffeeBriefContract) {
            super(itemView);
            ivCoffee = itemView.findViewById(R.id.ivCoffee);
            tvCoffeeName = itemView.findViewById(R.id.tvCoffeeName);

            itemView.setOnClickListener(v -> {
                if(coffeeBriefContract != null){
                    coffeeBriefContract.onCoffeeBriefClick(v, getAdapterPosition());
                }
            });
        }
    }
}
