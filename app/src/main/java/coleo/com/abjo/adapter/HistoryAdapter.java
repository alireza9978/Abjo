package coleo.com.abjo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import coleo.com.abjo.R;
import coleo.com.abjo.data_class.History;
import coleo.com.abjo.data_class.Transition;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItem> {

    private ArrayList<History> histories;
    private ArrayList<Transition> transitions;
    private Context context;

    public HistoryAdapter(ArrayList<History> histories, ArrayList<Transition> transitions, Context context) {
        this.histories = histories;
        this.transitions = transitions;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.history_item_layout, parent, false);
            return new HistoryItem(view, 0);
        } else {
            view = inflater.inflate(R.layout.transition_history_item_layout, parent, false);
            return new HistoryItem(view, 1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItem holder, int position) {

    }

    @Override
    public int getItemCount() {
        return histories.size() + transitions.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position > histories.size()) {
            return 0;
        } else {
            return 1;
        }
    }

    class HistoryItem extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView coin;
        private TextView level;
        private TextView distance_transitionName;
        private ImageView bike_step_tran;
        private int type;

        public HistoryItem(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;
            bike_step_tran = itemView.findViewById(R.id.bike_or_step_icon);
            date = itemView.findViewById(R.id.bike_time_text);
            coin = itemView.findViewById(R.id.point_text);
            if (type == 0) {
                level = itemView.findViewById(R.id.score_text);
                distance_transitionName = itemView.findViewById(R.id.distant_text);
            } else {
                distance_transitionName = itemView.findViewById(R.id.transition_name_id);
            }
        }

        public HistoryItem(@NonNull View itemView) {
            super(itemView);
        }

        public TextView getDate() {
            return date;
        }

        public TextView getCoin() {
            return coin;
        }

        public TextView getLevel() {
            return level;
        }

        public TextView getDistance_transitionName() {
            return distance_transitionName;
        }

        public ImageView getBike_step_tran() {
            return bike_step_tran;
        }

        public int getType() {
            return type;
        }
    }

}
