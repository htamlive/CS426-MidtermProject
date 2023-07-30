package com.example.a21125060;

import adapter.RecyclerPurchaseDetailAdapter;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import database.DBHelper;
import model.CoffeeCupOrder;

public class DetailPurchaseActivity extends AppCompatActivity {

    TextView tvPurchaseShopAddr, tvPurchaseTotalPrice, tvOrderDateTime;
    Button btnPurchaseDetailBack;
    RecyclerView rvPurchaseDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_purchase);

        Intent intent = getIntent();
        int purchaseId = intent.getIntExtra("purchaseId", CoffeeCupOrder.UNPAID);
        String formattedDate = intent.getStringExtra("formattedDate");
        String formattedPrice = intent.getStringExtra("formattedPrice");
        String shopAddress = intent.getStringExtra("shopAddress");

        tvPurchaseShopAddr = findViewById(R.id.tvPurchaseShopAddr);
        tvPurchaseTotalPrice = findViewById(R.id.tvPurchaseTotalPrice);
        tvOrderDateTime = findViewById(R.id.tvOrderDateTime);

        tvPurchaseShopAddr.setText(shopAddress);
        tvPurchaseTotalPrice.setText(formattedPrice);
        tvOrderDateTime.setText(formattedDate);

        rvPurchaseDetail = findViewById(R.id.rvPurchaseDetail);
        btnPurchaseDetailBack = findViewById(R.id.btnPurchaseDetailBack);


        DBHelper dbHelper = new DBHelper(this);
        rvPurchaseDetail.setAdapter(new RecyclerPurchaseDetailAdapter(dbHelper.getAllOrderFromPurchaseId(purchaseId)));
        rvPurchaseDetail.setLayoutManager(new LinearLayoutManager(this));
        dbHelper.close();

        btnPurchaseDetailBack.setOnClickListener(v -> {
            finish();
        });
    }
}