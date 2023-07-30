package com.example.a21125060;

import adapter.HomeViewPagerAdapter;
import android.annotation.SuppressLint;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.a21125060.Fragment.GiftFragment;
import com.example.a21125060.Fragment.OrderFragment;
import com.example.a21125060.Fragment.StoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import contract.PageTransformerContract;
import contract.ViewPagerContract;
import transformation.PageTransformer;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ViewPagerContract, PageTransformerContract {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private List<Integer> pageIds;
    public int swipingState;
    public static int SWIPING_NA = 0;
    public static int SWIPING_FORWARD = 1;
    public static int SWIPING_BACKWARD = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViewPager2();
        initPages();

        initBottomNav();
    }



    private void initPages() {
        pageIds = new ArrayList<>();
        pageIds.add(R.id.flStore);
        pageIds.add(R.id.flGift);
        pageIds.add(R.id.flOrder);
    }

    @SuppressLint("NonConstantResourceId")
    private void initBottomNav() {
        bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.btnStore:
                    viewPager2.setCurrentItem(0);
                    break;
                case R.id.btnGift:
                    viewPager2.setCurrentItem(1);
                    break;
                case R.id.btnOrder:
                    viewPager2.setCurrentItem(2);
                    break;
            }
            return true;
        });
    }

    private void initViewPager2() {
        viewPager2 = findViewById(R.id.viewPager);

        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(this, this);
        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(1);
//        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        viewPager2.setPageTransformer(new PageTransformer(this));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.btnStore).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.btnGift).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.btnOrder).setChecked(true);
                        break;
                }

            }
        });
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position){
            case 1:
                fragment = new GiftFragment();
                break;
            case 2:
                fragment = new OrderFragment();
                break;
            default:
                fragment = new StoreFragment();
                break;
        }
        return fragment;
    }

    @Override
    public void onPageTransform(View view, float position) {
        int currentPosition = viewPager2.getCurrentItem();
        float direction = 0;
        if(view != null){
            for (Integer pageId : pageIds) {
                if (isReadingCurrentPos(view, currentPosition, pageId)) {
                    direction = position;
                    break;
                }
            }
        }
        updateSwipingStatus(direction);
        debugSwiping(swipingState);
        updateFragment(view, position, direction);

    }

    private void updateFragment(View view, float position, float direction) {
        int currentPosition = viewPager2.getCurrentItem();
        int roundedDirection = RoundingDirection(direction);
        int alternativePosition = currentPosition - roundedDirection;

        StoreFragment.updateFragmentTransform(view, position, direction);
        GiftFragment.updateFragmentTransform(view, position, direction);
        OrderFragment.updateFragmentTransform(view, position, direction);
    }

    private int RoundingDirection(float direction) {
        int roundedDirection;
        if(direction < 0){
            roundedDirection = -1;
        } else if(direction > 0){
            roundedDirection = 1;
        } else {
            roundedDirection = 0;
        }
        return roundedDirection;
    }

    private void debugSwiping(int swipingState) {
        int currentPosition = viewPager2.getCurrentItem();
        if(swipingState == SWIPING_BACKWARD){

            System.out.println(currentPosition + " backward");
        }
        if(swipingState == SWIPING_FORWARD){
            System.out.println(currentPosition + " forward");
        }
    }

    private void updateSwipingStatus(float direction) {
        float eps =  0.0f;
        if(direction < -eps){
         swipingState = SWIPING_BACKWARD;
        } else if(direction < eps) {
         swipingState = SWIPING_NA;
        } else {
         swipingState = SWIPING_FORWARD;
        }
    }
    private boolean isReadingCurrentPos(View view, int currentPosition, int id) {
        return view.findViewById(id) != null && pageIds.get(currentPosition) == id;
    }

    public void moveToOrderFragment() {
        viewPager2.setCurrentItem(3);
    }
}