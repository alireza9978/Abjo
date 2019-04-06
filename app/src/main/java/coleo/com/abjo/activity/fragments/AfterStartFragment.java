package coleo.com.abjo.activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;


public class AfterStartFragment extends Fragment {

    public AfterStartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_after_start, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        String lastAction = Constants.getLastAction(Constants.context);

        if (!lastAction.equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            getView().findViewById(R.id.start_bycicle_button_id).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.start_step_button_id).setVisibility(View.INVISIBLE);
//            getView().findViewById(R.id.continue_last_action_id).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.start_bycicle_button_id).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.start_step_button_id).setVisibility(View.VISIBLE);
//            getView().findViewById(R.id.continue_last_action_id).setVisibility(View.INVISIBLE);
        }
    }

}
