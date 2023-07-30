package contract;

import android.view.View;

public interface DetailPurchaseContract {
    void onViewPurchaseDetail(View v, int purchaseId, String formattedDate, String formattedPrice, String shopAddress);
}
