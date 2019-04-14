package coleo.com.abjo.activity.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coleo.com.abjo.R;
import coleo.com.abjo.adapter.HistoryAdapter;
import coleo.com.abjo.data_class.DateAction;
import coleo.com.abjo.data_class.History;
import coleo.com.abjo.data_class.Transition;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements Serializable {

    private RecyclerView list;

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
        list = view.findViewById(R.id.history_and_transition_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        list.setLayoutManager(mLayoutManager);

        historyArrayList = new ArrayList<>();
        transitionArrayList = new ArrayList<>();


        adapter = new HistoryAdapter(historyArrayList, transitionArrayList, getContext());
        list.setAdapter(adapter);

        return view;
    }

    public void update(ArrayList<DateAction> dateActions) {
        historyArrayList.clear();
        transitionArrayList.clear();

        for (DateAction temp : dateActions) {
            if (temp instanceof History) {
                historyArrayList.add((History) temp);
            }
            if (temp instanceof Transition) {
                transitionArrayList.add((Transition) temp);
            }
//            if (temp instanceof History) {
//                historyArrayList.add((History) temp);
//            }//todo with other kind
        }

        adapter.notifyDataSetChanged();
    }


}
