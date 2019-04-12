package coleo.com.abjo.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import coleo.com.abjo.R;
import coleo.com.abjo.activity.fragments.AfterStartFragment;
import coleo.com.abjo.activity.fragments.Heart;
import coleo.com.abjo.activity.fragments.LeaderBoard;
import coleo.com.abjo.activity.fragments.Profile;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_class.LeaderBoardData;
import coleo.com.abjo.data_class.ProfileData;
import coleo.com.abjo.server_class.ServerClass;

public class MainActivity extends AppCompatActivity implements Serializable {

    final Fragment fragment1 = new Profile();
    final Fragment fragment2 = new Heart();
    final Fragment fragment3 = new LeaderBoard();
    final Fragment fragment4 = new AfterStartFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment2;
    int mainFragmentNumber = 0;
    Context context = this;

    private TabLayout tabLayout;
    private ImageView menuButton;

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
                        ServerClass.getLeaderBoard(context);
                        icon = R.drawable.leader_board_selected;
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        menuButton.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case 1: {
                        icon = R.drawable.heart_selected;
                        if (mainFragmentNumber == 0) {
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            menuButton.setVisibility(View.VISIBLE);
                        } else {
                            if (mainFragmentNumber == 1) {
                                fm.beginTransaction().hide(active).show(fragment4).commit();
                                active = fragment4;
                                menuButton.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                    }
                    case 2: {
                        icon = R.drawable.profile_selected;
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        menuButton.setVisibility(View.INVISIBLE);
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

        menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Heart) fragment2).openNavigation();
            }
        });

        tabLayout.getTabAt(0).setText("");
        tabLayout.getTabAt(0).setIcon(R.drawable.leader_board_selected);
        tabLayout.getTabAt(1).setText("");
        tabLayout.getTabAt(1).setIcon(R.drawable.heart);
        tabLayout.getTabAt(2).setText("");
        tabLayout.getTabAt(2).setIcon(R.drawable.profile);


        tabLayout.selectTab(tabLayout.getTabAt(1));

        Bundle extra = getIntent().getExtras();
        boolean temp = extra.getBoolean(Constants.FROM_NOTIFICATION, false);
        if (temp) {
            showAfterStartFromNotification();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.context = this;
        if (Constants.checkPermission(this)) {
            if (Constants.checkLocation(this)) {
                Log.i("MAIN_ACTIVITY", "onResume: have permission");
            }
        }
        ServerClass.getProfile(this);
    }

    public void noPermission() {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Do not call super class method here.
//        super.onSaveInstanceState(outState);
    }

    public void showAfterStart(boolean isStep, ProfileData data) {
        mainFragmentNumber = 1;
        fm.beginTransaction().hide(active).show(fragment4).commit();
        active = fragment4;
        ((AfterStartFragment) fragment4).startServiceFromOut(isStep, data);
        menuButton.setVisibility(View.INVISIBLE);
    }

    public void updateLeaderBoard(ArrayList<LeaderBoardData> arrayList) {
        ((LeaderBoard) fragment3).update(arrayList);
    }

    public void showAfterStartFromNotification() {
        mainFragmentNumber = 1;
        fm.beginTransaction().hide(active).show(fragment4).commit();
        active = fragment4;
        ((AfterStartFragment) fragment4).startServiceFromNotification();
        menuButton.setVisibility(View.INVISIBLE);

    }

    public void backToMain() {
        mainFragmentNumber = 0;
        fm.beginTransaction().hide(active).show(fragment2).commit();
        active = fragment2;
        menuButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

    }

    public void updateProfile(ProfileData data) {
        ((Heart) fragment2).updateProfile(data);
    }

}
