package com.example.a21125060;

import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.a21125060.Fragment.PurchaseSuccessFragment;
import com.example.a21125060.Fragment.WaitingFragment;

public class PurchaseResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_order_result);


        // add waiting fragment to fragment container view for 2 seconds
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new WaitingFragment())
                .commit();

        // change to PurchaseSuccessFragment after 2 seconds by using looper
        new Handler(Looper.getMainLooper()).postDelayed(() -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new PurchaseSuccessFragment())
                .commit(), 2000);
    }
}