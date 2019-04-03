package coleo.com.abjo.activity.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coleo.com.abjo.R;
import coleo.com.abjo.adapter.HistoryAdapter;
import coleo.com.abjo.data_class.History;
import coleo.com.abjo.data_class.Transition;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements Serializable {

    private RecyclerView list;
    private TextView name;
    private TextView level;
    private TextView point;
    private TextView coin;
    private TextView hour;
    private BootstrapProgressBar progressBar;
    private HistoryAdapter adapter;
    private ArrayList<History> historyArrayList;
    private ArrayList<Transition> transitionArrayList;



    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        BootstrapProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setBootstrapBrand(new TempBrand());
        list = view.findViewById(R.id.history_and_transition_list);
        name = view.findViewById(R.id.user_name_text_view_id);
        coin = view.findViewById(R.id.coin_of_activity_text);
        point = view.findViewById(R.id.point_text_id);
        level = view.findViewById(R.id.level_text_id);
        hour = view.findViewById(R.id.hour_of_activity_text);
        progressBar = view.findViewById(R.id.progress_bar);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list.setLayoutManager(mLayoutManager);

        historyArrayList = new ArrayList<>();
        transitionArrayList = new ArrayList<>();
        historyArrayList.add(new History());
        historyArrayList.add(new History());
        transitionArrayList.add(new Transition());

        adapter = new HistoryAdapter(historyArrayList, transitionArrayList, getContext());
        list.setAdapter(adapter);
        update();

        return view;
    }

    public void update() {
        adapter.notifyDataSetChanged();
    }

    class TempBrand implements BootstrapBrand {


        @Override
        public int defaultFill(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return context.getColor(R.color.login_submit_gradient_right);
            }
            return Color.rgb(190, 215, 59);
        }

        @Override
        public int defaultEdge(Context context) {
            return Color.rgb(242, 242, 243);
        }

        @Override
        public int defaultTextColor(Context context) {
            return Color.rgb(242, 242, 243);
        }

        @Override
        public int activeFill(Context context) {
            return Color.rgb(242, 242, 243);
        }

        @Override
        public int activeEdge(Context context) {
            return Color.rgb(242, 242, 243);
        }

        @Override
        public int activeTextColor(Context context) {
            return Color.rgb(242, 242, 243);
        }

        @Override
        public int disabledFill(Context context) {
            return Color.rgb(242, 242, 243);
        }

        @Override
        public int disabledEdge(Context context) {
            return Color.rgb(242, 242, 243);

        }

        @Override
        public int disabledTextColor(Context context) {
            return Color.rgb(242, 242, 243);
        }

        @Override
        public int getColor() {
            return Color.rgb(242, 242, 243);
        }


    }

}
