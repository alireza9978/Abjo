package coleo.com.abjo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import coleo.com.abjo.R;
import coleo.com.abjo.data_class.LeaderBoardData;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardItem> {

    private ArrayList<LeaderBoardData> list;
    private Context context;

    public LeaderBoardAdapter(ArrayList<LeaderBoardData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LeaderBoardItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.leader_board_item_mine, parent, false);
            return new LeaderBoardItem(view);
        } else {
            view = inflater.inflate(R.layout.leader_board_item, parent, false);
            return new LeaderBoardItem(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardItem holder, int position) {
        LeaderBoardData temp = list.get(position);
        holder.getName().setText(temp.getUser().getFullName());
        holder.getNumber().setText(" " + temp.getRank());
        holder.getPoint().setText(" " + temp.getPoint());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isMine()) {
            return 0;
        } else {
            return 1;
        }
    }

    class LeaderBoardItem extends RecyclerView.ViewHolder {
        private TextView point;
        private TextView name;
        private TextView number;

        public LeaderBoardItem(@NonNull View itemView) {
            super(itemView);
            point = itemView.findViewById(R.id.point);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.rank);
        }

        public TextView getPoint() {
            return point;
        }

        public TextView getName() {
            return name;
        }

        public TextView getNumber() {
            return number;
        }
    }

}
