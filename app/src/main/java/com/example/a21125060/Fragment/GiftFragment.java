package com.example.a21125060.Fragment;

import adapter.RecyclerHistoryRewardAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import com.example.a21125060.RedeemActivity;
import database.DBHelper;
import model.HistoryReward;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class GiftFragment extends LoyaltyCupFragment {

    private List<HistoryReward> historyRewardList;
    Button btnRedeem;
    FrameLayout flLoyaltyPointCard;
    ActivityResultLauncher<Intent> redeemActivityLauncher;
    TextView tvLoyaltyPointCount, tvHistoryRewards;
    RecyclerView recyclerHistoryReward;

    boolean isRedeemBoardDown = true;

    final static int offsetSlide = 395;

    @Override
    protected int flWindowID() {
        return R.id.flGiftWindow;
    }

    public  GiftFragment(){
        super();
//        initSampleHistoryRewardList();
    }

    public static void updateFragmentTransform(View view, float position, float direction) {
        FrameLayout flLoyaltyPointCard = view.findViewById(R.id.flLoyaltyPointCard);
        if(flLoyaltyPointCard != null){
            RecyclerView rvHistoryRewards = view.findViewById(R.id.rvHistoryRewards);
            if(-1 <= position && position <= 1){

                flLoyaltyPointCard.setTranslationY(-Math.abs(position) * offsetSlide);
                flLoyaltyPointCard.setZ(-1);




                if(position == 0){
                    rvHistoryRewards.setVisibility(View.VISIBLE);
                }
                else rvHistoryRewards.setVisibility(View.GONE);
            }

        }

    }

    private void initSampleHistoryRewardList() {
        historyRewardList = new ArrayList<>();
        historyRewardList.add(new HistoryReward("Americano", "2021-06-24 12:30:00", 12));
        historyRewardList.add(new HistoryReward("Cafe Latte", "2021-06-22 08:30:00", 12));
        historyRewardList.add(new HistoryReward("Green Tea Latte", "2021-06-16 10:48:00", 12));
        historyRewardList.add(new HistoryReward("Flat White", "2021-05-12 11:25:00", 12));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_gift, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryHistoryRewards();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout flLoyaltyCups = view.findViewById(R.id.flLoyaltyCups);



        flLoyaltyPointCard = view.findViewById(R.id.flLoyaltyPointCard);
        tvLoyaltyCupCount = view.findViewById(R.id.tvLoyaltyCupCount);
        tvLoyaltyPointCount = view.findViewById(R.id.tvLoyaltyPointCount);
        tvHistoryRewards = view.findViewById(R.id.tvHistoryRewards);
        recyclerHistoryReward = view.findViewById(R.id.rvHistoryRewards);


        queryHistoryRewards();
//        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.moving_score_anim);
//        animation.setInterpolator(getContext(), android.R.interpolator.accelerate_decelerate);
//        flLoyaltyCups.startAnimation(animation);


        initLoyaltyCupRecyclerView(flLoyaltyCups);

        initHistoryRewardRecyclerView();


        redeemActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
//                    slideRedeemBoard(true);
                }
        );

        btnRedeem = view.findViewById(R.id.btnRedeem);
        btnRedeem.setOnClickListener(v -> {
//            slideRedeemBoard( false);
            Intent intent = new Intent(requireContext(), RedeemActivity.class);
            redeemActivityLauncher.launch(intent);
        });

        updateLoyaltyScore();

        // toggle history rewards and slide redeem board


    }

    private void queryHistoryRewards() {
        DBHelper dbHelper = new DBHelper(requireContext());
        historyRewardList = dbHelper.getHistoryRewards();
        dbHelper.close();
        updateAdapterHistoryReward();

    }


    @Override
    protected void syncLoyaltyCup() {
        super.syncLoyaltyCup();
        updateLoyaltyScore();
    }

    private void updateLoyaltyScore() {
        DBHelper dbHelper = new DBHelper(requireContext());
        int point = dbHelper.getCurrentLoyaltyScore();
        String displayPoint = point + " pts";
        tvLoyaltyPointCount.setText(displayPoint);
        dbHelper.close();
    }

    private void initHistoryRewardRecyclerView() {

        recyclerHistoryReward.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        //set divider
        recyclerHistoryReward.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        queryHistoryRewards();
        updateAdapterHistoryReward();
    }

    private void updateAdapterHistoryReward() {
        RecyclerHistoryRewardAdapter adapter = new RecyclerHistoryRewardAdapter(historyRewardList);
        recyclerHistoryReward.setAdapter(adapter);
    }


}