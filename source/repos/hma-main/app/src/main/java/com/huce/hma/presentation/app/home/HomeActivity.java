package com.huce.hma.presentation.app.home;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.huce.hma.R;
import com.huce.hma.common.services.MediaPlayerProvider;
import com.huce.hma.presentation.app.home.component.HomeFragment;
import com.huce.hma.presentation.app.home.component.RankFragment;
import com.huce.hma.presentation.app.home.component.ProfileFragment;
import com.huce.hma.presentation.app.home.component.SearchFragment;
import com.huce.hma.presentation.app.home.component.SettingActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnHome;
    private Button btnSearch;
    private Button btnPlay,btnRank;
    private Button btnUser;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_home);

        btnHome = findViewById(R.id.btn_home);
        btnSearch = findViewById(R.id.btn_search);
        btnPlay = findViewById(R.id.playButton);
        btnRank = findViewById(R.id.btn_rank);
        btnUser = findViewById(R.id.btn_user);

        swapFragment(1);

//        if (!Utils.isServiceRunning(MusicPlayService.class, getApplicationContext())) {
//            btnPlay.setVisibility(0);
//        }
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(1);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(2);
            }
        });
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(4);
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });
        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(3);
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(4);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlayStatus();
    }

    private void swapFragment(int index) {
        Fragment fragment = new Fragment();
        switch (index) {
            case 1:
                fragment = new HomeFragment();
//                btnHome.setBackground(getDrawable(R.id.btn_home));
                break;
            case 2:
                fragment = new SearchFragment();
                break;
            case 4:
                fragment = new ProfileFragment();
                break;
            case 3:
                fragment = new RankFragment();
                break;
            default:
                return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.contenrFragment, fragment);
        fragmentTransaction.commit();
    }

    private void loadPlayStatus() {
        MediaPlayer mediaPlayer = MediaPlayerProvider.getInstance();
        btnPlay.setBackgroundResource(mediaPlayer.isPlaying() ? R.drawable.btn_pause : R.drawable.btn_play1);
    }
    private void togglePlayPause() {
        MediaPlayer mediaPlayer = MediaPlayerProvider.getInstance();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setBackgroundResource(R.drawable.btn_play1);
        } else {
            mediaPlayer.start();
            btnPlay.setBackgroundResource(R.drawable.btn_pause);
        }
    }
}
