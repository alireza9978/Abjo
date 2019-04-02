package coleo.com.abjo.activity.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

import androidx.fragment.app.Fragment;
import coleo.com.abjo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        BootstrapProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setBootstrapBrand(new TempBrand());
        return view;
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