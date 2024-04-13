package com.huce.hma.presentation.app.download;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.huce.hma.R;
import com.huce.hma.common.component.BaseActivity;
import com.huce.hma.common.component.MusicThumbnailWithInfoComponent;
import com.huce.hma.data.local.RoomDB;
import com.huce.hma.data.local.model.Song;
import com.huce.hma.databinding.ActivityAppMusicDownloadBinding;

import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends BaseActivity {
    private ActivityAppMusicDownloadBinding binding;
    private DownloadViewModel downloadViewModel;
    private LinearLayout container;

    private List<Song> offlineSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        downloadViewModel = new ViewModelProvider(this).get(DownloadViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_music_download);

        binding.setLifecycleOwner(this);

        binding.setDownloadViewModel(downloadViewModel);



        LinearLayout mainLayout = findViewById(R.id.linearLayout);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0,30,0,30);

        scrollView.setLayoutParams(params);
        mainLayout.addView(scrollView);

        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(container);

        RoomDB db = RoomDB.getInMemoryDatabase(getApplicationContext());

        offlineSongs = db.songDao().getAllSongs();

        getOfflineSongs();


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Xử lý khi tab được chọn
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        removeAll();
                        getOfflineSongs();
                        break;
                    case 1:
                        removeAll();
                        getOfflinePlaylist();
                        break;
                    case 2:
                        removeAll();
                        getOfflineAlbums();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Xử lý khi tab không còn được chọn
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void removeAll() {
        container.removeAllViews();
    }
    private void getOfflineAlbums() {

    }
    private void getOfflinePlaylist() {

    }
    @SuppressLint("CommitTransaction")
    private void getOfflineSongs() {
        TextView txt = findViewById(R.id.textView6);
        txt.setText(txt.getText().toString().replace("{{number}}", String.valueOf(offlineSongs.size())));
        txt.setText(txt.getText().toString().replace("{{type}}", "bài hát"));

        if (offlineSongs.isEmpty()) {
            DownloadEmptyFragment downloadEmptyFragment = new DownloadEmptyFragment("bài hát");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.linear_layout_container, downloadEmptyFragment);
            fragmentTransaction.commit();
        } else {
            for (int i = 0; i < offlineSongs.size(); i += 2) {
                MusicThumbnailWithInfoComponent musicThumbnail1 = null;
                MusicThumbnailWithInfoComponent musicThumbnail2 = null;

                try {
                    Song song = offlineSongs.get(i);
                    musicThumbnail1 = new MusicThumbnailWithInfoComponent(this);
                    musicThumbnail1.getTitle().setText(song.Name);
                    musicThumbnail1.getDesc().setText(song.Artist);
                    musicThumbnail1.getThumbnail().setImageBitmap(BitmapFactory.decodeFile(song.FilePath));
                } catch (Exception ignored) {
                }

                try {
                    Song song = offlineSongs.get(i + 1);
                    musicThumbnail2 = new MusicThumbnailWithInfoComponent(this);
                    musicThumbnail2.getTitle().setText(song.Name);
                    musicThumbnail2.getDesc().setText(song.Artist);
                    musicThumbnail2.getThumbnail().setImageBitmap(BitmapFactory.decodeFile(song.FilePath));
                } catch (Exception ignored) {
                }

                container.addView(createRow(musicThumbnail1, musicThumbnail2));
            }
        }
    }
    private LinearLayout createRow(MusicThumbnailWithInfoComponent musicThumbnailWithInfoComponent1, MusicThumbnailWithInfoComponent musicThumbnailWithInfoComponent2) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        if (musicThumbnailWithInfoComponent1 != null) {
            linearLayout.addView(musicThumbnailWithInfoComponent1);
        }
        if (musicThumbnailWithInfoComponent2 != null) {
            linearLayout.addView(musicThumbnailWithInfoComponent2);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }
}
