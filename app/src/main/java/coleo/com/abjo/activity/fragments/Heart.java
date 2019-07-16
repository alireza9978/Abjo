package coleo.com.abjo.activity.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.ColorOfProgress;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import java.io.Serializable;

import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.data_class.ProfileData;
import coleo.com.abjo.server_class.ServerClass;

public class Heart extends Fragment implements Serializable {

    private static final String TAG = "menu";
    private boolean walk;

    private TextView name;
    private TextView level;
    private TextView point;
    private TextView coin;
    private TextView funnyText;
    private TextView hour;

    private ProfileData data;

    private BootstrapProgressBar progressBar;
    public Heart() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inside_of_drawer_layout, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setBootstrapBrand(new ColorOfProgress());
        name = view.findViewById(R.id.user_name_text_view_id);
        coin = view.findViewById(R.id.coin_of_activity_text);
        point = view.findViewById(R.id.point_text_id);
        level = view.findViewById(R.id.level_text_id);
        hour = view.findViewById(R.id.hour_of_activity_text);
        funnyText = view.findViewById(R.id.funnyText_id);

        Button start = view.findViewById(R.id.start_button_id);
        BubbleNavigationLinearView switchUI = view.findViewById(R.id.bottom_navigation_view_linear);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.big_main_font);
        switchUI.setTypeface(Typeface.create(typeface, Typeface.BOLD));

        switchUI.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                walk = position == 1;
            }
        });
        walk = false;
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainActivity) container.getContext()).checkFullPermission()) {
                    ServerClass.openNewSession(container.getContext(),walk, data);
                }
            }
        });

        return view;
    }


    public void updateProfile(ProfileData data) {
        this.data = data;
        name.setText(data.getUser().getFullName());
        coin.setText(data.getCoinsText());
        hour.setText(data.getHoursText());
        progressBar.setMaxProgress(data.getLevel().getLevelMaxPoint());
        progressBar.setProgress(data.getLevel().getPoint());
        point.setText(" " + data.getLevel().getPoint() + "  امتیاز  ");
        level.setText(" سطح " + data.getLevel().getLevel() + " ");
        funnyText.setText(data.getNote());
        if (data.getLevel().getPoint() < 10) {
            point.setTextColor(getResources().getColor(R.color.login_submit_gradient_left));
        }
        ((MainActivity) getContext()).closeNavigation();
    }

}
