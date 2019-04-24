package coleo.com.abjo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import coleo.com.abjo.R;
import coleo.com.abjo.data_class.ActivityKind;
import coleo.com.abjo.data_class.DateAction;
import coleo.com.abjo.data_class.History;
import coleo.com.abjo.data_class.Introduce;
import coleo.com.abjo.data_class.Transition;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItem> {

    private ArrayList<DateAction> actions;
    private Context context;

    public HistoryAdapter(ArrayList<DateAction> actions, Context context) {
        this.context = context;
        this.actions = actions;
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
        if (getItemViewType(position) == 0) {
            History history = (History) actions.get(position);
            if (history.getKind() == ActivityKind.bike) {
                holder.bike_step_tran.setImageResource(R.mipmap.bike);
            } else {
                holder.bike_step_tran.setImageResource(R.mipmap.step);
            }
            holder.coin.setText("" + history.getCoin());
            holder.getPoint().setText("" + history.getPoint());
            holder.distance_transitionName.setText("" + history.getDistance());
            holder.date.setText(history.getString());
        } else {
            if (getItemViewType(position) == 1) {
                Transition transition = (Transition) actions.get(position);
                holder.bike_step_tran.setImageResource(R.mipmap.transition_big_icon);
                holder.date.setText(transition.getString());
                holder.coin.setText("" + transition.getCoin());
                holder.distance_transitionName.setText(transition.getTitle());
            } else {
                Introduce introduce = (Introduce) actions.get(position);
                holder.bike_step_tran.setImageResource(R.mipmap.intoduce);
                holder.date.setText(introduce.getString());
                holder.coin.setText("" + introduce.getCoin());
                holder.distance_transitionName.setText(introduce.getTitle());
            }
        }
        holder.bike_step_tran.setColorFilter(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    @Override
    public int getItemViewType(int position) {
        DateAction action = actions.get(position);
        if (action instanceof History) {
            return 0;
        } else {
            if (action instanceof Transition)
                return 1;
            else
                return 2;
        }
    }

    class HistoryItem extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView coin;
        private TextView point;
        private TextView distance_transitionName;
        private ImageView bike_step_tran;
        private int type;

        public HistoryItem(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;
            bike_step_tran = itemView.findViewById(R.id.bike_or_step_icon);
            date = itemView.findViewById(R.id.bike_time_text);
            coin = itemView.findViewById(R.id.coin_text);
            if (type == 0) {
                point = itemView.findViewById(R.id.coin_text);
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

        public TextView getPoint() {
            return point;
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
