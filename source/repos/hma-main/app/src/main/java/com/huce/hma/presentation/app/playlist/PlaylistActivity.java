package com.huce.hma.presentation.app.playlist;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.huce.hma.R;
import com.huce.hma.common.component.BaseActivity;
import com.huce.hma.common.component.MusicThumbnailWithInfoComponent;
import com.huce.hma.data.local.RoomDB;
import com.huce.hma.data.local.dao.SongDao;
import com.huce.hma.data.local.model.Song;
import com.huce.hma.presentation.app.download.DownloadEmptyFragment;

import java.util.List;

public class PlaylistActivity extends BaseActivity {

    private Spinner spn_playlist;
    private LinearLayout container;
    private List<Song> list_playlist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_playlist);

        RoomDB db = RoomDB.getInMemoryDatabase(getApplicationContext());

        list_playlist = db.songDao().getAllSongs();
        getPlayList();

    }

    private void getPlayList() {
        if (list_playlist.isEmpty()) {
            DownloadEmptyFragment downloadEmptyFragment = new DownloadEmptyFragment("bài hát");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.linear_layout_container, downloadEmptyFragment);
            fragmentTransaction.commit();
        } else {
            for (int i = 0; i < list_playlist.size(); i += 2) {
                MusicThumbnailWithInfoComponent musicThumbnail1 = null;
                MusicThumbnailWithInfoComponent musicThumbnail2 = null;

                try {
                    Song song = list_playlist.get(i);
                    musicThumbnail1 = new MusicThumbnailWithInfoComponent(this);
                    musicThumbnail1.getTitle().setText(song.Name);
                    musicThumbnail1.getDesc().setText(song.Artist);
                    musicThumbnail1.getThumbnail().setImageBitmap(BitmapFactory.decodeFile(song.FilePath));
                } catch (Exception ignored) {
                }

                try {
                    Song song = list_playlist.get(i + 1);
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
