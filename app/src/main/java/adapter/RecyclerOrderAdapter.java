package adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import contract.DetailPurchaseContract;
import database.table.PurchaseTable;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class RecyclerOrderAdapter extends RecyclerView.Adapter<RecyclerOrderAdapter.ViewHolder>{
    private final List<PurchaseTable.PurchaseBrief> purchases;
    private final DetailPurchaseContract detailPurchaseContract;

    public RecyclerOrderAdapter(List<PurchaseTable.PurchaseBrief> purchases, DetailPurchaseContract detailPurchaseContract) {
        this.purchases = purchases;
        this.detailPurchaseContract = detailPurchaseContract;

    }

    @NonNull
    @NotNull
    @Override
    public RecyclerOrderAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_order_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerOrderAdapter.ViewHolder holder, int position) {
        PurchaseTable.PurchaseBrief purchase = purchases.get(position);

        int purchaseId = purchase.ID;

        Calendar calendar = Calendar.getInstance();
        String retrievedDate = purchase.buyDate;
        String formattedDate = FormattingDateTime(holder, calendar, retrievedDate);
        holder.tvOrderDateTime.setText(formattedDate);

        //format price as $3.53, 2 decimal places
        float totalPrice = purchase.totalPrice;
        @SuppressLint("DefaultLocale") String formattedPrice = String.format("$%.2f", totalPrice);

        if (totalPrice == 0){
            formattedPrice = "Free";
        }

        holder.tvOrderTotalPrice.setText(formattedPrice);

        String shopAddress = purchase.shopAddress;
        holder.tvShopAddr.setText(shopAddress);

        String finalFormattedPrice = formattedPrice;
        holder.cvOrderedOrderItem.setOnClickListener(v -> {
            detailPurchaseContract.onViewPurchaseDetail(v, purchaseId, formattedDate, finalFormattedPrice, shopAddress);

        });

    }

    @NotNull
    private static String FormattingDateTime(@NotNull ViewHolder holder, Calendar calendar, String retrievedDate) {
        // Date is currently format with YYYY-MM-DD HH:MM:SS
        String[] date = retrievedDate.split(" ");
        String[] dateSplit = date[0].split("-");
        String[] timeSplit = date[1].split(":");
        calendar.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]), Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]));
        // DD MMM | HH:MM AM/PM
        @SuppressLint("DefaultLocale") String formattedDate =
                String.format("%d %s | %d:%02d %s", calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.getDisplayName(Calendar.MONTH,
                                Calendar.SHORT, holder.itemView.getResources().getConfiguration().getLocales().get(0)),
                        calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),
                        calendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT,
                                holder.itemView.getResources().getConfiguration().getLocales().get(0)));
        return formattedDate;
    }

    @Override
    public int getItemCount() {
        if (purchases != null){
            return purchases.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderDateTime, tvOrderTotalPrice, tvShopAddr;
        CardView cvOrderedOrderItem;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvOrderDateTime = itemView.findViewById(R.id.tvOrderDateTime);
            tvOrderTotalPrice = itemView.findViewById(R.id.tvOrderTotalPrice);
            tvShopAddr = itemView.findViewById(R.id.tvPurchaseShopAddr);
            cvOrderedOrderItem = itemView.findViewById(R.id.cvOrderedOrderItem);
        }
    }
}
