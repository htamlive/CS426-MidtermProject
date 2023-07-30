package com.example.a21125060.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.*;
import model.PurchaseManager;
import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class StoreFragment extends LoyaltyCupFragment {
    RecyclerView recyclerCoffeeBrief;
    PurchaseManager purchaseManager;
    ActivityResultLauncher<Intent> coffeeDetailResultLauncher;
    Button btnProfile;
    TextView tvWelcome;

    @Override
    protected int flWindowID() {
        return R.id.flStoreWindow;
    }

    public StoreFragment() {
        super();
        purchaseManager = new PurchaseManager(getContext());

        coffeeDetailResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        purchaseManager.onFinishPurchase(result);

                        ((HomeActivity) requireActivity()).moveToOrderFragment();

                    }

                }
        );
        purchaseManager.setCoffeeDetailResultLauncher(coffeeDetailResultLauncher);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        purchaseManager.createCoffeeDetailList(getContext());

        FrameLayout flLoyaltyCups = view.findViewById(R.id.flLoyaltyCups);
        tvLoyaltyCupCount = view.findViewById(R.id.tvLoyaltyCupCount);

        tvWelcome = view.findViewById(R.id.tvName);

        initLoyaltyCupRecyclerView(flLoyaltyCups);

        initRecyclerCoffeeBrief(view);

        initStoreCart(view);

        initProfileButton(view);

        updateCustomerName();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateCustomerName();
    }

    private void updateCustomerName() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("profile", MODE_PRIVATE);
        String username = preferences.getString("fullName", "User");

        tvWelcome.setText(username);
    }

    private void initProfileButton(@NotNull View view) {
        btnProfile = view.findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void initStoreCart(View view) {
        Button btnStoreCart = view.findViewById(R.id.btnStoreCart);
        btnStoreCart.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyCartActivity.class);
            coffeeDetailResultLauncher.launch(intent);
        });
    }


    private void initRecyclerCoffeeBrief(@NotNull View view) {
        recyclerCoffeeBrief = view.findViewById(R.id.rvCoffeeBrief);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerCoffeeBrief.setLayoutManager(gridLayoutManager);

        purchaseManager.updateRecyclerCoffeeDetailAdapter(recyclerCoffeeBrief, requireContext());

    }

    public static void updateFragmentTransform(View view, float position, float direction){

    }

    public void onFinishPurchase(Intent data) {

    }

}