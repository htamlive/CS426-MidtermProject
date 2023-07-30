package com.example.a21125060;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.elegantnumberbutton.ElegantNumberButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import database.DBHelper;
import model.CoffeeCupOrder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CoffeeDetailActivity extends AppCompatActivity implements ElegantNumberButton.OnValueChangeListener {

    ChipGroup cgShot;
    ImageView ivCoffeeDetail;
    TextView tvCoffeeDetailName, tvCoffeeDetailDescription, tvCoffeeDetailPrice;
    RadioGroup rgIce, rgSize, rgSelect;
    private List<Integer> rbIceIds, rbSizeIds, rbSelectedIds;
    float coffeeDefaultPrice;
    int coffeeId;
    ScrollView svCoffeeDetailOption;
    ActivityResultLauncher<Intent> coffeeDetailCurrentBuy;
    private CoffeeCupOrder currentCoffeeCupOrder;
    void updatePrice() {
        float currentPrice = currentCoffeeCupOrder.syncCurrentTotalPrice(coffeeDefaultPrice);
        @SuppressLint("DefaultLocale") String displayPrice = String.format("$%.2f", currentPrice);
        tvCoffeeDetailPrice.setText(displayPrice);
    }


    public  static final int MAX_COFFEE_COUNT = 50;
    public  static final int MIN_COFFEE_COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_detail);


        ivCoffeeDetail = findViewById(R.id.ivCoffeeDetail);
        tvCoffeeDetailName = findViewById(R.id.tvCoffeeDetailName);
        tvCoffeeDetailDescription = findViewById(R.id.tvCoffeeDetailDescription);
        tvCoffeeDetailPrice = findViewById(R.id.tvCoffeeDetailPrice);
        svCoffeeDetailOption = findViewById(R.id.svCoffeeDetailOption);

        // set scrollable
        svCoffeeDetailOption.setSmoothScrollingEnabled(true);

        rgIce = findViewById(R.id.rgIce);
        rgSize = findViewById(R.id.rgSize);
        rgSelect = findViewById(R.id.rgSelect);

        initDataFromCoffeeBrief();

        initElegantNumberButton();

        //get Children
        rbIceIds = initIds(rgIce);
        rbSizeIds = initIds(rgSize);
        rbSelectedIds = initIds(rgSelect);

        initChipShot();



        initAddToCartButton();
        initCoffeeDetailBack();

        initCallingCoffeeDetailCurrentBuy();
        updatePrice();

    }

    private void initCoffeeDetailBack() {
        Button btnCoffeeDetailBack = findViewById(R.id.btnPurchaseDetailBack);
        btnCoffeeDetailBack.setOnClickListener(v -> finish());
    }

    private void initCallingCoffeeDetailCurrentBuy() {
        coffeeDetailCurrentBuy = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
        );

        Button btnCoffeeDetailCurrentBuy = findViewById(R.id.btnCoffeeDetailCurrentBuy);

        btnCoffeeDetailCurrentBuy.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyCartActivity.class);
            coffeeDetailCurrentBuy.launch(intent);
        });
    }

    private void initAddToCartButton() {
        MaterialButton mbCoffeeDetailCart = findViewById(R.id.mbCoffeeDetailCart);
        mbCoffeeDetailCart.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(this);
            boolean addToCart = dbHelper.addToCart(currentCoffeeCupOrder);
            dbHelper.close();
            showNotification(addToCart);


        });
    }

    private void showNotification(boolean addToCart) {
        String message = "Add to cart failed";
        if (addToCart) {
            message = "Add to cart successfully";
        }

        @SuppressLint("InflateParams") View alertDialog = LayoutInflater.from(CoffeeDetailActivity.this).inflate(R.layout.custom_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(CoffeeDetailActivity.this);
        builder.setView(alertDialog);

        final AlertDialog dialog = builder.create();

        TextView tvDialogMessage = alertDialog.findViewById(R.id.tvDialogMessage);
        MaterialButton mbDialogOK = alertDialog.findViewById(R.id.mbDialogOK);

        tvDialogMessage.setText(message);

        mbDialogOK.setOnClickListener(v -> {
            dialog.cancel();
        });


        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // set click outside cancelable false
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void initChipShot() {
        cgShot = findViewById(R.id.cgShot);

//        cgShot.check(R.id.chipSingle);
        cgShot.setOnCheckedStateChangeListener((group, checkedId) -> {
            int id = group.getCheckedChipId();

            if (id == R.id.chipSingle) {
                currentCoffeeCupOrder.setCurrentCoffeeShot(1);
            } else if (id == R.id.chipDouble) {
                currentCoffeeCupOrder.setCurrentCoffeeShot(2);
            } else {
                currentCoffeeCupOrder.setCurrentCoffeeShot(0);
            }
            updatePrice();
        });
    }

    private List<Integer> initIds(RadioGroup rg) {
        ArrayList<Integer> rbIds = new ArrayList<>();
        for (int i = 0; i < rg.getChildCount(); i++) {
            // add into rbIceIds
            View child = rg.getChildAt(i);
            rbIds.add(child.getId());
        }
        return rbIds;
    }


    private void initElegantNumberButton() {

        ElegantNumberButton enbCoffeeCount = findViewById(R.id.enbCoffeeCount);

        enbCoffeeCount.setNumber(String.valueOf(MIN_COFFEE_COUNT));
        enbCoffeeCount.setOnValueChangeListener(this);
        // set range
        enbCoffeeCount.setRange(MIN_COFFEE_COUNT, MAX_COFFEE_COUNT);
    }

    private void initDataFromCoffeeBrief() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String coffeeName = extras.getString("coffeeName");
            String coffeeDescription = extras.getString("coffeeDescription");
            int coffeeImageResourceId = extras.getInt("coffeeImageResourceId");
            coffeeId = extras.getInt("coffeeId");

            coffeeDefaultPrice = extras.getFloat("coffeeDefaultPrice");
            @SuppressLint("DefaultLocale") String displayPrice = String.format("$%.2f", coffeeDefaultPrice);
            tvCoffeeDetailPrice.setText(displayPrice);
            tvCoffeeDetailName.setText(coffeeName);
            ivCoffeeDetail.setImageResource(coffeeImageResourceId);
            tvCoffeeDetailDescription.setText(coffeeDescription);

            currentCoffeeCupOrder = new CoffeeCupOrder(coffeeId);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onSizeSelected(View view) {
        Integer checkedId = ChangeRadioButtonTheme(rgSize, rbSizeIds);
        //update currentCoffeeSize
        switch (checkedId) {
            case R.id.rbSmall:
                currentCoffeeCupOrder.setCurrentCoffeeSize(0);
                break;
            case R.id.rbMedium:
                currentCoffeeCupOrder.setCurrentCoffeeSize(1);
                break;
            case R.id.rbLarge:
                currentCoffeeCupOrder.setCurrentCoffeeSize(2);
                break;
        }

        // update price
        updatePrice();

    }



    @SuppressLint("NonConstantResourceId")
    public void onIceSelected(View view) {

        Integer checkedId = ChangeRadioButtonTheme(rgIce, rbIceIds);
        //update currentCoffeeIce
        switch (checkedId) {
            case R.id.rbLess:
                currentCoffeeCupOrder.setCurrentCoffeeIce(0);
                break;
            case R.id.rbNormal:
                currentCoffeeCupOrder.setCurrentCoffeeIce(1);
                break;
            case R.id.rbFull:
                currentCoffeeCupOrder.setCurrentCoffeeIce(2);
                break;
        }
        updatePrice();
    }

    @NotNull
    private Integer ChangeRadioButtonTheme(RadioGroup rgIce, List<Integer> rbIceIds) {
        Integer checkedId = rgIce.getCheckedRadioButtonId();
        // set button tint to silver if id is not checkId
        for (Integer id : rbIceIds) {
            RadioButton rb = findViewById(id);
            if (!Objects.equals(id, checkedId)) {
                rb.setButtonTintList(getResources().getColorStateList(R.color.silver, getTheme()));
            } else {
                rb.setButtonTintList(getResources().getColorStateList(R.color.cello, getTheme()));
            }
        }
        return checkedId;
    }

    @Override
    public void onValueChangeByClicking(ElegantNumberButton view, int oldValue, int newValue) {
        checkCoffeeCountInRange(view, newValue);
    }

    @Override
    public void onValueChange(ElegantNumberButton elegantNumberButton, int lastNumber, int currentNumber) {

    }

    @Override
    public void onValueChangeByTyping(ElegantNumberButton elegantNumberButton, int lastNumber, int currentNumber) {
        checkCoffeeCountInRange(elegantNumberButton, currentNumber);
    }

    private void checkCoffeeCountInRange(ElegantNumberButton elegantNumberButton, int currentNumber) {
        if (currentNumber > MAX_COFFEE_COUNT) {
            elegantNumberButton.setNumber("50");
            Toast.makeText(this, "Max " + MAX_COFFEE_COUNT + " cups", Toast.LENGTH_SHORT).show();
        }
        if (currentNumber < MIN_COFFEE_COUNT) {
            elegantNumberButton.setNumber("1");
            Toast.makeText(this, "Min " + MIN_COFFEE_COUNT + " cup", Toast.LENGTH_SHORT).show();
        }
        currentCoffeeCupOrder.setCurrentCoffeeCount(currentNumber);
        System.out.println("currentCoffeeCount = " + currentCoffeeCupOrder.getCurrentCoffeeCount());
        updatePrice();
    }

    @SuppressLint("NonConstantResourceId")
    public void onSelectDrinkPlace(View view) {
        Integer checkedId = ChangeRadioButtonTheme(rgSelect, rbSelectedIds);

        switch (checkedId) {
            case R.id.rbTakeAway:
                currentCoffeeCupOrder.setCurrentCoffeePlace(0);
                Toast.makeText(this, "Take Away", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rbHere:
                currentCoffeeCupOrder.setCurrentCoffeePlace(1);
                Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}