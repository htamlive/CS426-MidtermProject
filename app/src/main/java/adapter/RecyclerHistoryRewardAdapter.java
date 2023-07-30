package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21125060.R;
import model.HistoryReward;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

public class RecyclerHistoryRewardAdapter extends RecyclerView.Adapter<RecyclerHistoryRewardAdapter.ViewHolder> {
    List<HistoryReward> historyRewardList;

    public RecyclerHistoryRewardAdapter(List<HistoryReward> historyRewardList) {
        this.historyRewardList = historyRewardList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.history_reward_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        HistoryReward historyReward = historyRewardList.get(position);
        holder.tvCoffeeTitle.setText(historyReward.getCoffeeName());
        String displayDate = convertDate(historyReward.getDateTime());
        holder.tvDate.setText(displayDate);
        String displayScore = "+ " + historyReward.getPoints() + " Pts";
        holder.tvScore.setText(displayScore);


    }

    private String convertDate(String dateTimeString) {
        // current string has format ISO 8601 format YYYY-MM-DD HH:MM:SS
        // convert to Jan, Feb, ...

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateTimeString.substring(0,4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateTimeString.substring(5,7)) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateTimeString.substring(8,10)));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateTimeString.substring(11,13)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(dateTimeString.substring(14,16)));
        calendar.set(Calendar.SECOND, Integer.parseInt(dateTimeString.substring(17,19)));
        Date date = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return dateFormat.format(date);

    }

    @Override
    public int getItemCount() {
        if(historyRewardList == null){
            return 0;
        }
        return historyRewardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCoffeeTitle;
        private final TextView tvDate;
        private final TextView tvScore;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCoffeeTitle = itemView.findViewById(R.id.tvCoffeeTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvScore = itemView.findViewById(R.id.tvScore);

        }
    }


}
