package model;

import adapter.RecyclerCoffeeBriefAdapter;
import adapter.RecyclerCoffeeCartAdapter;
import adapter.RecyclerRedeemAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.*;
import com.example.a21125060.CoffeeDetailActivity;
import com.example.a21125060.MyCartActivity;
import com.example.a21125060.R;
import com.example.a21125060.RedeemActivity;
import contract.CoffeeBriefContract;
import database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class PurchaseManager implements CoffeeBriefContract {
    private List<CoffeeDetail> coffeeDetailList;
    private List<CoffeeCupOrder> orderOnCartList;
    private List<RedeemItem> redeemItemList;
    ActivityResultLauncher<Intent> coffeeDetailResultLauncher;
    RecyclerCoffeeCartAdapter recyclerCoffeeBriefAdapter;
    public PurchaseManager(Context context) {

    }
    public void setCoffeeDetailResultLauncher(ActivityResultLauncher<Intent> coffeeDetailResultLauncher) {
        this.coffeeDetailResultLauncher = coffeeDetailResultLauncher;
    }
    private void initSampleCoffeeDetailList() {
        coffeeDetailList = new ArrayList<>();

        coffeeDetailList.add(new CoffeeDetail(1,"Cappuccino", "A shot of espresso with a dash of steamed milk and a layer of foam on top.", R.drawable.cappuccino, 3.5f));
        coffeeDetailList.add(new CoffeeDetail(2,"Espresso", "A shot of espresso with a dash of steamed milk and a layer of foam on top.", R.drawable.espresso, 2.5f));
        coffeeDetailList.add(new CoffeeDetail(3,"Latte", "A shot of espresso with a dash of steamed milk and a layer of foam on top.", R.drawable.latte, 3.5f));
        coffeeDetailList.add(new CoffeeDetail(4,"Mocha", "A shot of espresso with a dash of steamed milk and a layer of foam on top.", R.drawable.mocha, 4.5f));
        coffeeDetailList.add(new CoffeeDetail(5,"Americano", "A shot of espresso with a dash of steamed milk and a layer of foam on top.", R.drawable.americano, 3.5f));
        coffeeDetailList.add(new CoffeeDetail(6,"Flat White", "A shot of espresso with a dash of steamed milk and a layer of foam on top.", R.drawable.flat_white, 3.5f));
    }
    public int updateRecyclerCoffeeDetailAdapter(RecyclerView recyclerView, Context context){
        if(coffeeDetailList == null){
//            initSampleCoffeeDetailList();
        }
        RecyclerCoffeeBriefAdapter recyclerCoffeeBriefAdapter = new RecyclerCoffeeBriefAdapter(coffeeDetailList, context, this);
        recyclerView.setAdapter(recyclerCoffeeBriefAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        return coffeeDetailList.size();
    }

    public int updateRecyclerOrderOnCartAdapter(RecyclerView recyclerView, Context context) {
        if (orderOnCartList == null) {
            // TODO: nothing
        }

        recyclerCoffeeBriefAdapter = new RecyclerCoffeeCartAdapter(orderOnCartList, context, R.layout.coffee_on_cart_item);
        recyclerView.setAdapter(recyclerCoffeeBriefAdapter);

        // set linear layout manager vertically
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        return orderOnCartList.size();
    }
    @Override
    public void onCoffeeBriefClick(View view, int position) {
        Context context = view.getContext();
        CoffeeDetail coffeeDetail = coffeeDetailList.get(position);
        Intent intent = new Intent(context, CoffeeDetailActivity.class);
        intent.putExtra("coffeeId", coffeeDetail.getId());
        intent.putExtra("coffeeName", coffeeDetail.getName());
        intent.putExtra("coffeeDescription", coffeeDetail.getDescription());
        intent.putExtra("coffeeImageResourceId", coffeeDetail.getImageResourceId());
        intent.putExtra("coffeeDefaultPrice", coffeeDetail.getDefaultPrice());
        coffeeDetailResultLauncher.launch(intent);
    }
    public void onFinishPurchase(ActivityResult result) {

    }
    public void createOrderOnCart(Context context){
        DBHelper dbHelper = new DBHelper(context);
        orderOnCartList = dbHelper.getAllOrderOnCart();
        dbHelper.close();
    }
    public void createCoffeeDetailList(Context context){
        DBHelper dbHelper = new DBHelper(context);
        coffeeDetailList = dbHelper.getAllCoffee();
        dbHelper.close();
    }

    public void createRedeemCoffeeList(Context context){
        DBHelper dbHelper = new DBHelper(context);
        redeemItemList = dbHelper.getAllRedeemCoffee();
        dbHelper.close();
    }

    public void useRedeemCoffee(int position, Context context){
        String shopAddress = "123 ABC Street";
        int delivery = 0;

        DBHelper dbHelper = new DBHelper(context);
        int coffeeId = redeemItemList.get(position).getCoffeeId();
        CoffeeCupOrder coffeeCupOrder = new CoffeeCupOrder(coffeeId);
        int point = redeemItemList.get(position).getPoint();
        boolean isSuccess = dbHelper.useRedeem(coffeeCupOrder, shopAddress, delivery, point);
        dbHelper.close();
        if(!isSuccess){
            Toast.makeText(context, "Not enough point", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Redeem success", Toast.LENGTH_SHORT).show();
        }


    }


    public float getTotalPrice() {
        float totalPrice = 0;
        for (CoffeeCupOrder coffeeCupOrder : orderOnCartList) {
            totalPrice += coffeeCupOrder.getCurrentTotalPrice();
        }
        return totalPrice;
    }


    public void moveCoffeeOnCart(int fromPosition, int toPosition) {
        CoffeeCupOrder fromCoffee = orderOnCartList.get(fromPosition);
        CoffeeCupOrder toCoffee = orderOnCartList.get(toPosition);

        orderOnCartList.set(fromPosition, toCoffee);
        orderOnCartList.set(toPosition, fromCoffee);

        recyclerCoffeeBriefAdapter.notifyItemMoved(fromPosition, toPosition);

    }

    public void onSwipedCoffeeOnCart(RecyclerView.ViewHolder viewHolder, int direction, Context context) {
        int position = viewHolder.getAdapterPosition();

        switch (direction) {
            case ItemTouchHelper.LEFT:
            case ItemTouchHelper.RIGHT:
//                String movieName = moviesList.get(position);
//                archivedMovies.add(movieName);
                removeOrderOnCart(position, context);
                break;
//                Snackbar.make(recyclerView, movieName + "archived.", Snackbar.LENGTH_LONG)
//                        .setAction("Undo", v -> {
//                            archivedMovies.remove(archivedMovies.lastIndexOf(movieName));
//                            moviesList.add(position, movieName);
//                            recyclerAdapter.notifyItemInserted(position);
//                        }).show();

        }
    }
    private void removeOrderOnCart(int position, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.deleteOrderOnCart(orderOnCartList.get(position).getOrderId());
        dbHelper.close();
        orderOnCartList.remove(position);
        recyclerCoffeeBriefAdapter.notifyItemRemoved(position);

    }
    public void onCheckout(MyCartActivity context) {
        DBHelper dbHelper = new DBHelper(context);
        String shopAddress = "123 ABC Street";
        int delivery = 0;
        dbHelper.payAllOrderOnCart(shopAddress, delivery);
        dbHelper.close();
    }

    public void updateRecyclerRedeemAdapter(RecyclerView rvRedeem, RedeemActivity context) {
        if(redeemItemList == null){
//            initSampleCoffeeDetailList();
        }
        RecyclerRedeemAdapter recyclerRedeemAdapter = new RecyclerRedeemAdapter(redeemItemList, context);
        rvRedeem.setAdapter(recyclerRedeemAdapter);
        // set linear layout manager vertically
        rvRedeem.setLayoutManager(new LinearLayoutManager(context));
    }

    public boolean isCartEmpty() {
        return orderOnCartList.size() == 0;
    }
}
