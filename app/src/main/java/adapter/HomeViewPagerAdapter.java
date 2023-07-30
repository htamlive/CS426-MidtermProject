package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import contract.ViewPagerContract;
import org.jetbrains.annotations.NotNull;

public class HomeViewPagerAdapter extends FragmentStateAdapter {
    ViewPagerContract viewPagerContract;

    public HomeViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, ViewPagerContract viewPagerContract) {
        super(fragmentActivity);
        this.viewPagerContract = viewPagerContract;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return viewPagerContract.createFragment(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
