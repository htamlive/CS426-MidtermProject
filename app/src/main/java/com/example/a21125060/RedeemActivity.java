package com.example.a21125060;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import contract.RedeemContract;
import model.CoffeeCupOrder;
import model.PurchaseManager;

public class RedeemActivity extends AppCompatActivity implements RedeemContract {

    RecyclerView rvRedeem;
    PurchaseManager purchaseManager;

    Button btnRedeemDetailBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        rvRedeem = findViewById(R.id.rvRedeem);
        purchaseManager = new PurchaseManager(this);
        purchaseManager.createRedeemCoffeeList(this);
        purchaseManager.updateRecyclerRedeemAdapter(rvRedeem, this);

        btnRedeemDetailBack = findViewById(R.id.btnRedeemDetailBack);
        btnRedeemDetailBack.setOnClickListener(v -> finish());

    }

    @Override
    public void onRedeemClick(int position) {
        System.out.println("user click redeem coffee at position " + position);
        purchaseManager.useRedeemCoffee(position, this);
    }
}