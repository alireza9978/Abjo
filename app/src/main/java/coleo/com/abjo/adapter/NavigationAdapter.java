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
import coleo.com.abjo.data_class.NavigationDrawerItem;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavigationItem> {

    private ArrayList<NavigationDrawerItem> list;
    private Context context;

    public NavigationAdapter(ArrayList<NavigationDrawerItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NavigationItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.drawer_item_layout, parent, false);
        return new NavigationItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationItem holder, int position) {
        NavigationDrawerItem temp = list.get(position);
        holder.description.setText(temp.getName());
        holder.icon.setImageResource(temp.getIcon());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NavigationItem extends RecyclerView.ViewHolder {
        private TextView description;
        private ImageView icon;

        public NavigationItem(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image_navigation_icon);
            description = itemView.findViewById(R.id.navigation_text_view_id);
        }



    }

}
