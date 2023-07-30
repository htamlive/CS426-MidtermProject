package contract;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface ViewPagerContract {
    public abstract Fragment createFragment(int position);
}

