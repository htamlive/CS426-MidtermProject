package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.a21125060.Fragment.OrderFragment;
import contract.ViewPagerContract;
import org.jetbrains.annotations.NotNull;

public class OrderHistoryTabViewPagerAdapter extends FragmentStateAdapter {

    ViewPagerContract viewPagerContract;
    public OrderHistoryTabViewPagerAdapter(@NonNull @NotNull OrderFragment fragmentActivity, ViewPagerContract viewPagerContract) {
        super(fragmentActivity);
        this.viewPagerContract = viewPagerContract;
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return viewPagerContract.createFragment(position);
    }
}
