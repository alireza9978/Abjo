package coleo.com.abjo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import coleo.com.abjo.R;
import coleo.com.abjo.activity.fragments.AfterStartFragment;
import coleo.com.abjo.activity.fragments.Heart;
import coleo.com.abjo.activity.fragments.LeaderBoard;
import coleo.com.abjo.activity.fragments.Profile;
import coleo.com.abjo.constants.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    final Fragment fragment1 = new Profile();
    final Fragment fragment2 = new Heart();
    final Fragment fragment3 = new LeaderBoard();
    final Fragment fragment4 = new AfterStartFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment2;
    int mainFragmentNumber = 0;
    Context context = this;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();
        Constants.context = this;

        fm.beginTransaction().add(R.id.main_container, fragment1, "1").hide(fragment1).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").commit();
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit();

        tabLayout = findViewById(R.id.navigation);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int icon = 0;
                switch (tab.getPosition()) {
                    case 0: {
                        icon = R.drawable.leader_board_selected;
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        break;
                    }
                    case 1: {
                        icon = R.drawable.heart_selected;
                        if (mainFragmentNumber == 0) {
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                        } else {
                            if (mainFragmentNumber == 1) {
                                fm.beginTransaction().hide(active).show(fragment4).commit();
                                active = fragment4;
                            }
                        }
                        break;
                    }
                    case 2: {
                        icon = R.drawable.profile_selected;
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        break;
                    }
                }
                tab.setIcon(icon);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int icon = 0;
                switch (tab.getPosition()) {
                    case 0: {
                        icon = R.drawable.leader_board;
                        break;
                    }
                    case 1: {
                        icon = R.drawable.heart;
                        break;
                    }
                    case 2: {
                        icon = R.drawable.profile;
                        break;
                    }
                }
                tab.setIcon(icon);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).setText("");
        tabLayout.getTabAt(0).setIcon(R.drawable.leader_board_selected);
        tabLayout.getTabAt(1).setText("");
        tabLayout.getTabAt(1).setIcon(R.drawable.heart);
        tabLayout.getTabAt(2).setText("");
        tabLayout.getTabAt(2).setIcon(R.drawable.profile);

        Constants.checkPermission(this);
        Constants.checkLocation(this);

        tabLayout.selectTab(tabLayout.getTabAt(1));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.context = this;
    }

    public void noPermission() {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Do not call super class method here.
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_step_button_id: {
                Intent intent = new Intent(context, CountActivity.class);
                intent.putExtra(Constants.STEP_OR_BIKE, true);
                startActivity(intent);
                ((Activity) context).finish();
                break;
            }
            case R.id.start_bycicle_button_id: {
                Intent intent = new Intent(context, CountActivity.class);
                intent.putExtra(Constants.STEP_OR_BIKE, false);
                startActivity(intent);
                ((Activity) context).finish();
                break;
            }
//            case R.id.continue_last_action_id: {
//                Intent intent = new Intent(context, CountActivity.class);
//                startActivity(intent);
//                ((Activity) context).finish();
//                break;
//            }
        }
    }

    public void showAfterStart() {
        mainFragmentNumber = 1;
        fm.beginTransaction().hide(active).show(fragment4).commit();
        active = fragment4;
    }

    @Override
    public void onBackPressed() {

    }
}
