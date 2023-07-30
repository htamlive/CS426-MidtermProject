package com.example.a21125060.Fragment;

import adapter.RecyclerOrderAdapter;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.DetailPurchaseActivity;
import com.example.a21125060.R;
import contract.DetailPurchaseContract;
import database.DBHelper;
import database.table.PurchaseTable;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class OnGoingTabFragment extends Fragment implements DetailPurchaseContract {

    RecyclerView rvOnGoingOrder;
    RecyclerOrderAdapter recyclerOrderAdapter;
    List<PurchaseTable.PurchaseBrief> onGoingPurchaseList;
    TextView tvOrderTabEmptyTitle;


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set theme
        requireActivity().setTheme(R.style.Theme_21125060_OngoingOrder);
        rvOnGoingOrder = view.findViewById(R.id.rvOnGoingOrder);
        tvOrderTabEmptyTitle = view.findViewById(R.id.tvOrderTabEmptyTitle);

        queryOnGoingPurchase();

        rvOnGoingOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvOnGoingOrder);

        // when remove item from recycler view, update the status

    }

    private void updateOnGoingOrderStatus() {
        if (onGoingPurchaseList.size() == 0) {
            tvOrderTabEmptyTitle.setVisibility(View.VISIBLE);
        } else {
            tvOrderTabEmptyTitle.setVisibility(View.GONE);
        }

    }

    private void queryOnGoingPurchase() {
        DBHelper dbHelper = new DBHelper(requireActivity());
        onGoingPurchaseList = dbHelper.getPurchaseWithTotalPrice(0);
        recyclerOrderAdapter = new RecyclerOrderAdapter(onGoingPurchaseList, this);

        rvOnGoingOrder.setAdapter(recyclerOrderAdapter);
        dbHelper.close();
        updateOnGoingOrderStatus();

    }

    @Override
    public void onResume() {
        super.onResume();
        queryOnGoingPurchase();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            onGoingPurchaseList.add(toPosition, onGoingPurchaseList.remove(fromPosition));
            recyclerOrderAdapter.notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            DBHelper dbHelper = new DBHelper(requireActivity());
            dbHelper.updatePurchaseStatusFinished(onGoingPurchaseList.get(position).getPurchaseId());
            dbHelper.close();

            onGoingPurchaseList.remove(position);
            recyclerOrderAdapter.notifyItemRemoved(position);
            updateOnGoingOrderStatus();
        }

        @Override
        public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_tab, container, false);
    }

    @Override
    public void onViewPurchaseDetail(View v, int purchaseId, String formattedDate, String formattedPrice, String shopAddress) {
        Intent intent = new Intent(getActivity(), DetailPurchaseActivity.class);
        intent.putExtra("purchaseId", purchaseId);
        intent.putExtra("formattedDate", formattedDate);
        intent.putExtra("formattedPrice", formattedPrice);
        intent.putExtra("shopAddress", shopAddress);
        startActivity(intent);
    }
}