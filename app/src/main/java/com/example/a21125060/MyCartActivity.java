package com.example.a21125060;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.button.MaterialButton;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import it.xabaras.android.recyclerview.swipedecorator.SwipeRecyclerViewContract;
import model.PurchaseManager;
import org.jetbrains.annotations.NotNull;

public class MyCartActivity extends AppCompatActivity implements SwipeRecyclerViewContract {
    PurchaseManager purchaseManager;
    RecyclerView rvCartCoffee;
    TextView tvCartEmptyTitle;
    ActivityResultLauncher<Intent> orderResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        purchaseManager = new PurchaseManager(this);
        purchaseManager.createOrderOnCart(this);

        rvCartCoffee = findViewById(R.id.rvCartCoffee);

        purchaseManager.updateRecyclerOrderOnCartAdapter(rvCartCoffee, this);
        initCartBack();
        initCheckout();

        displayTotalPrice();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvCartCoffee);

        tvCartEmptyTitle = findViewById(R.id.tvCartEmptyTitle);
        updateCartStatus();
    }

    private void updateCartStatus() {
        if(purchaseManager.isCartEmpty()){
            tvCartEmptyTitle.setVisibility(View.VISIBLE);
        }else{
            tvCartEmptyTitle.setVisibility(View.GONE);
        }
    }



    private void initCheckout() {
        orderResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
//                        purchaseManager.onFinishPurchase(result);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );

        Button mbCheckout = findViewById(R.id.mbCheckout);
        mbCheckout.setOnClickListener(v -> {
            // check if cart is empty

            if(purchaseManager.isCartEmpty()){
                showAlertEmpty();
                return;
            }
            purchaseManager.onCheckout(this);
            Intent intent = new Intent(this, PurchaseResultActivity.class);
            orderResultLauncher.launch(intent);
        });
    }



    private void showAlertEmpty() {
        String message = "Cart is empty";

        @SuppressLint("InflateParams") View alertDialog = LayoutInflater.from(MyCartActivity.this).inflate(R.layout.custom_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MyCartActivity.this);
        builder.setView(alertDialog);
        final AlertDialog dialog = builder.create();

        TextView tvDialogMessage = alertDialog.findViewById(R.id.tvDialogMessage);
        MaterialButton mbDialogOK = alertDialog.findViewById(R.id.mbDialogOK);

        tvDialogMessage.setText(message);

        mbDialogOK.setOnClickListener(v -> {
            // close dialog
            dialog.cancel();

        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            purchaseManager.moveCoffeeOnCart(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            purchaseManager.onSwipedCoffeeOnCart(viewHolder, direction, MyCartActivity.this);
            updateCartStatus();
            displayTotalPrice();

        }

        @Override
        public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .setSwipeRecyclerViewContract(MyCartActivity.this)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(recyclerView.getContext(),R.color.light_red))
                    .setActionIconTint(ContextCompat.getColor(recyclerView.getContext(), android.R.color.holo_red_light))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .addSwipeRightActionIcon(R.drawable.baseline_delete_24)
                    .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP,20)
//                    .addPadding(TypedValue.COMPLEX_UNIT_DIP, 5, 5, 5)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(recyclerView.getContext(),R.color.light_red))

                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    };

    private void initCartBack() {
        Button btnCoffeeDetailBack = findViewById(R.id.btnPurchaseDetailBack);
        btnCoffeeDetailBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void displayTotalPrice() {
        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
        @SuppressLint("DefaultLocale") String formattedTotalPrice = String.format("%.2f", purchaseManager.getTotalPrice());
        tvTotalPrice.setText(formattedTotalPrice);
    }

    @Override
    public RotateDrawable iconTransformOnSwipeLeft(RotateDrawable icon, float dx) {
        // spin, scale and increase opacity of icon base on dx
        icon.setAlpha((int) (255 * (-dx / 200)));

        // convert dx to deg
        System.out.println("dx: " + dx);
        float deg = dx * 360 / 200;

        return rotateIcon(icon, deg);
    }

    private RotateDrawable rotateIcon(RotateDrawable icon, float deg) {
        RotateDrawable rotateDrawable = new RotateDrawable();
        rotateDrawable.setDrawable(icon);
        rotateDrawable.setFromDegrees(90f);
        rotateDrawable.setToDegrees(90f);
        rotateDrawable.setPivotX(0.5f);
        rotateDrawable.setPivotY(0.5f);
        return rotateDrawable;
    }

    @Override
    public RotateDrawable iconTransformOnSwipeRight(RotateDrawable icon, float recyclerViewSwipeDecorator) {
        // set opacity of icon base on dx
        icon.setAlpha((int) (255 * (recyclerViewSwipeDecorator / 200)));

        // convert dx to deg
        float deg = recyclerViewSwipeDecorator * 360 / 200;

        return rotateIcon(icon, deg);
    }

    @Override
    public boolean enableAdjacentCornerRadii() {
        return true;
    }
}