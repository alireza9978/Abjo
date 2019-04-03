package coleo.com.abjo.activity.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coleo.com.abjo.R;
import coleo.com.abjo.adapter.LeaderBoardAdapter;
import coleo.com.abjo.data_class.LeaderBoardData;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoard extends Fragment implements Serializable {

    private RecyclerView leaderBoardList;
    private LeaderBoardAdapter adapter;
    private ArrayList<LeaderBoardData> arrayList;


    public LeaderBoard() {
        arrayList = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        leaderBoardList = view.findViewById(R.id.leader_board_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        leaderBoardList.setLayoutManager(mLayoutManager);
        for (int i = 0; i < 5; i++) {
            arrayList.add(new LeaderBoardData());
        }
        arrayList.add(new LeaderBoardData());
        arrayList.get(5).setMine(true);
        for (int i = 0; i < 5; i++) {
            arrayList.add(new LeaderBoardData());
        }
        adapter = new LeaderBoardAdapter(arrayList, getContext());
        leaderBoardList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    public void update() {
        adapter.notifyDataSetChanged();
    }

}
