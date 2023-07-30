package com.example.a21125060.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import com.google.android.material.button.MaterialButton;
import component.RecyclerTouchListener;
import database.DBHelper;
import model.LoyaltyCupManager;
import org.jetbrains.annotations.NotNull;

public abstract class LoyaltyCupFragment extends Fragment {

    protected final LoyaltyCupManager loyaltyCupManager;

    protected TextView tvLoyaltyCupCount;

    protected int loyaltyCupCount;

    protected RecyclerView recyclerLoyaltyCups;
    protected int bonusLoyaltyScore;

    private int accumulativeGiftCupCount;
    View loyaltyCard;
    protected abstract int flWindowID();
    public static FragmentManager fragmentManager;



    public LoyaltyCupFragment() {
        this.loyaltyCupManager = new LoyaltyCupManager(getContext(), 10,2);
        bonusLoyaltyScore = 12;
    }

    protected void initLoyaltyCupRecyclerView(FrameLayout flLoyaltyCups) {
        recyclerLoyaltyCups = new RecyclerView(requireContext());
        recyclerLoyaltyCups.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        flLoyaltyCups.addView(recyclerLoyaltyCups);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) recyclerLoyaltyCups.getLayoutParams();
        layoutParams.gravity = 17;

        int padding = (int) (10 * getResources().getDisplayMetrics().density);
        flLoyaltyCups.setPadding(padding, padding, padding, padding);

        // set recycler view center and wrap content
        layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;


        recyclerLoyaltyCups.setLayoutParams(layoutParams);
        recyclerLoyaltyCups.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));







        syncLoyaltyCup();

        // check if a loyalty cup is clicked
        recyclerLoyaltyCups.addOnItemTouchListener(new RecyclerTouchListener(requireContext(), recyclerLoyaltyCups, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(onEarnAccumulativeGift()){
                    syncLoyaltyCup();
                    celebrationNotificationResult("You have earned " + bonusLoyaltyScore + " pts");

                    celebrate();
                    new Handler(
                            Looper.getMainLooper()).postDelayed(() -> closeCelebrate(),
                            1000);
                } else {
                    celebrationNotificationResult("You do not have enough loyalty cup");
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBHelper dbHelper = new DBHelper(requireContext());
        accumulativeGiftCupCount = dbHelper.getAccumulativeGiftCount();
        dbHelper.close();


        loyaltyCard = view.findViewWithTag("loyaltyCard");
        fragmentManager = requireActivity().getSupportFragmentManager();

    }

    @Override
    public void onPause() {
        super.onPause();
        closeCelebrate();
    }

    void celebrate(){
        // show dialog fragment CelebrateFragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment celebrateFragment = fragmentManager.findFragmentById(flWindowID());
        if(celebrateFragment == null){
            fragmentTransaction.add(flWindowID(), new CelebrateFragment());
            fragmentTransaction.commit();
        }
    }

    @SuppressLint("SetTextI18n")
    private void celebrationNotificationResult(String result) {

       // use dialogFragment to display the alert dialog
        @SuppressLint("InflateParams") View alertDialog = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertDialog);

        TextView tvDialogMessage = alertDialog.findViewById(R.id.tvDialogMessage);
        MaterialButton mbDialogOK = alertDialog.findViewById(R.id.mbDialogOK);

        AlertDialog dialog = builder.create();
        // set outside touch to false
        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        dialog.getWindow().setDimAmount(0.2f);


        dialog.show();

        mbDialogOK.setOnClickListener(v -> dialog.dismiss());


        tvDialogMessage.setText(result);

    }

    void closeCelebrate(){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment celebrateFragment = fragmentManager.findFragmentById(flWindowID());
        if(celebrateFragment != null){
            fragmentTransaction.remove(celebrateFragment);
            fragmentTransaction.commit();
        }
    }

    protected void blinkRewardAnimation(){
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.reward_notify);
        recyclerLoyaltyCups.startAnimation(animation);
    }

    // spin the cup and return back to

    @Override
    public void onResume() {
        super.onResume();
        closeCelebrate();
        syncLoyaltyCup();
    }

    protected void syncLoyaltyCup(){
        DBHelper dbHelper = new DBHelper(requireContext());
        loyaltyCupCount = dbHelper.getLoyaltyCupCount();
        accumulativeGiftCupCount = dbHelper.getAccumulativeGiftCount();
        loyaltyCupCount -= accumulativeGiftCupCount;
        dbHelper.close();

        System.out.println("Loyalty cup count: " + loyaltyCupCount);
        System.out.println("Accumulative gift count: " + accumulativeGiftCupCount);

        loyaltyCupManager.setCount(loyaltyCupCount);
        loyaltyCupManager.updateLoyalCupRecyclerAdapter(recyclerLoyaltyCups);

        displayLoyaltyCup();

        if(loyaltyCupCount >= loyaltyCupManager.getLoyaltyCupCountMax()){
            blinkRewardAnimation();
        } else {
            clearRewardAnimation();
        }

    }

    private void clearRewardAnimation() {
        recyclerLoyaltyCups.clearAnimation();
    }

    protected void displayLoyaltyCup(){
        String displayString = loyaltyCupCount + " /" + loyaltyCupManager.getLoyaltyCupCountMax();
        tvLoyaltyCupCount.setText(displayString);
    }

    protected boolean onEarnAccumulativeGift(){
        final int loyaltyCupToUse = loyaltyCupManager.getLoyaltyCupCountMax();
        DBHelper dbHelper = new DBHelper(requireContext());
        boolean result = dbHelper.transactLoyaltyScore(loyaltyCupCount, loyaltyCupToUse, bonusLoyaltyScore);
        dbHelper.close();
        return result;
    }

}
