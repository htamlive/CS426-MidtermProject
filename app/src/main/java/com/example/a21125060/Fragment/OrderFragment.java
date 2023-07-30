package com.example.a21125060.Fragment;

import adapter.OrderHistoryTabViewPagerAdapter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.example.a21125060.R;
import com.google.android.material.tabs.TabLayout;
import contract.ViewPagerContract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class OrderFragment extends Fragment implements ViewPagerContract {

    private TabLayout tlOrder;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 vpOrderHistory = view.findViewById(R.id.vpOrderHistory);
        vpOrderHistory.setAdapter(new OrderHistoryTabViewPagerAdapter(this, this));

        tlOrder = view.findViewById(R.id.tlOrder);
        // set on tab selected listener
        tlOrder.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpOrderHistory.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        vpOrderHistory.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tlOrder.getTabAt(position)).select();
                System.out.println("position = " + position);
            }
        });
    }




    public static void updateFragmentTransform(View view, float position, float direction) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 1) {
            fragment = new HistoryTabFragment();
            //set theme
            requireActivity().setTheme(R.style.Theme_21125060_PastOrder);
        } else {
            fragment = new OnGoingTabFragment();
            // set theme
            requireActivity().setTheme(R.style.Theme_21125060_OngoingOrder);
        }
        return fragment;
    }
}