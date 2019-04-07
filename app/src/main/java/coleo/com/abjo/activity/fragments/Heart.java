package coleo.com.abjo.activity.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.ColorOfProgress;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

import java.io.Serializable;

import androidx.fragment.app.Fragment;
import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;

public class Heart extends Fragment implements Serializable {

    private static final String TAG = "menu";
    private boolean walk;

    private TextView name;
    private TextView level;
    private TextView point;
    private TextView coin;
    private TextView hour;
    public Heart() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_operation, container, false);
        BootstrapProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setBootstrapBrand(new ColorOfProgress());
        name = view.findViewById(R.id.user_name_text_view_id);
        coin = view.findViewById(R.id.coin_of_activity_text);
        point = view.findViewById(R.id.point_text_id);
        level = view.findViewById(R.id.level_text_id);
        hour = view.findViewById(R.id.hour_of_activity_text);

        Button start = view.findViewById(R.id.start_button_id);
        Switch switchUI = view.findViewById(R.id.switch_id);
        switchUI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                walk = isChecked;
            }
        });
        switchUI.setChecked(true);
        walk = true;
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) container.getContext()).showAfterStart(walk);
            }
        });
        return view;
    }





}
