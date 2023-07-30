package com.example.a21125060.Fragment;

import adapter.RecyclerOrderAdapter;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.DetailPurchaseActivity;
import com.example.a21125060.R;
import contract.DetailPurchaseContract;
import database.DBHelper;
import org.jetbrains.annotations.NotNull;


public class HistoryTabFragment extends Fragment implements DetailPurchaseContract {

    RecyclerView rvHistoryOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set theme
        requireActivity().setTheme(R.style.Theme_21125060_PastOrder);


        rvHistoryOrder = view.findViewById(R.id.rvHistoryOrder);


        queryAndRenderHistoryOrder();

        rvHistoryOrder.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    @Override
    public void onResume() {
        super.onResume();
        queryAndRenderHistoryOrder();
    }

    private void queryAndRenderHistoryOrder() {
        DBHelper dbHelper = new DBHelper(requireActivity());
        rvHistoryOrder.setAdapter(new RecyclerOrderAdapter(dbHelper.getPurchaseWithTotalPrice(1), this));
        dbHelper.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_tab, container, false);
    }

    @Override
    public void onViewPurchaseDetail(View v, int purchaseId, String formattedDate, String formattedPrice, String shopAddress) {
        Intent intent = new Intent(getActivity(), DetailPurchaseActivity.class);


        String FormattedPaidPrice = formattedPrice + " (Paid)";
        intent.putExtra("purchaseId", purchaseId);
        intent.putExtra("formattedDate", formattedDate);
        intent.putExtra("formattedPrice", FormattedPaidPrice);
        intent.putExtra("shopAddress", shopAddress);
        startActivity(intent);

    }
}