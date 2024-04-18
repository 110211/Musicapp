package com.huce.hma.presentation.app.playmusic;

import static com.huce.hma.common.utils.Utils.fmMilisecondsToTimeString;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.huce.hma.BuildConfig;
import com.huce.hma.R;
import com.huce.hma.common.services.MediaPlayerProvider;
import com.huce.hma.data.local.model.Song;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.squareup.picasso.Picasso;

public class PlayMusicActivity extends AppCompatActivity {
    ImageButton imgback, imgshare, imgsetting, imgadd, imgdow, imgfavorite, imgprevious, imgplay, imgnext, imgrepeat,addToPlaylistButton;
    SeekBar skBar;

    ImageView circleImageView;
    ObjectAnimator objectAnimator;
    MediaPlayer mediaPlayer;
    TextView timeStart, timeEnd, nameSong, nameAlbum, nameAuthor;
    private final String channelId = "music_channel";
    private final int notificationId = 1;
    private int currentPosition = 0;
    private boolean isRepeat = false;
    private boolean like = false;
    private SharedPreferences prefs;

    private SongSearchResultDTO selectedSong;


    @SuppressLint("SneakyThrows")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_music_play);
        Intent intent = getIntent();

        // Initialize SharedPreferences
        prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);

        imgback = findViewById(R.id.backButton);
        imgshare = findViewById(R.id.shareButton);
        imgsetting = findViewById(R.id.settingButton);
        timeStart = findViewById(R.id.timeStart);
        timeEnd = findViewById(R.id.timeEnd);
        imgrepeat = findViewById(R.id.btn_loop);
        imgfavorite = findViewById(R.id.favoriteButton);
        imgprevious = findViewById(R.id.previousButton);
        imgplay = findViewById(R.id.playButton);
        imgnext = findViewById(R.id.nextButton);
        skBar = findViewById(R.id.seekBar);
        nameSong = findViewById(R.id.textViewAlbum);
        nameAlbum = findViewById(R.id.textViewSong);
        nameAuthor = findViewById(R.id.textViewAuthor);
        circleImageView = findViewById(R.id.musicDiscImageView);

        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(10000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        mediaPlayer = MediaPlayerProvider.getInstance();

        selectedSong = (SongSearchResultDTO) intent.getSerializableExtra("selected_song");
        initializeMediaPlayer();

        nameSong.setText(selectedSong.getName());
        nameAuthor.setText(selectedSong.getArtistName());
        nameAlbum.setText(selectedSong.getName());
        long songId = selectedSong.getId();  // Assume ID is available from selectedSong
        like = getLikeStatus(songId);
        updateLikeButton(like);
        addToPlaylistButton = findViewById(R.id.addToPlaylistButton);

        Picasso.get().load(selectedSong.getThumbnail()).into(circleImageView);

        setupListeners();
//        public void onClick(View v) {
//            showAddToPlaylistDialog();
//        }
//    });

    }

    private void setupListeners() {
        imgback.setOnClickListener(view -> onBackPressed());

        imgfavorite.setOnClickListener(view -> {
            long songId = selectedSong.getId();
            like = !like;
            setLikeStatus(songId, like);
            updateLikeButton(like);
            Toast.makeText(this, like ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
        });

        imgprevious.setOnClickListener(v -> mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000));
        imgnext.setOnClickListener(v -> mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000));
        imgrepeat.setOnClickListener(v -> {
            isRepeat = !isRepeat;
            mediaPlayer.setLooping(isRepeat);
            imgrepeat.setImageResource(isRepeat ? R.drawable.ic_loop : R.drawable.btn_loop);
        });

        imgplay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                pauseMusic();
            } else {
                resumeMusic();
            }
        });


        skBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        addToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToPlaylistDialog();
            }
        });
    }

    private boolean getLikeStatus(long songId) {
        return prefs.getBoolean("LikeStatus_" + songId, false);
    }

    private void setLikeStatus(long songId, boolean isLiked) {
        prefs.edit().putBoolean("LikeStatus_" + songId, isLiked).apply();
    }

    private void updateLikeButton(boolean isLiked) {
        imgfavorite.setImageResource(isLiked ? R.drawable.ic_like : R.drawable.icon_heart);
    }

    private void initializeMediaPlayer() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(BuildConfig.API_URL + "/api/v1/song/" + selectedSong.getId() + "/download");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                updateSeekBar();
                objectAnimator.start();
            });
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        imgplay.setImageResource(R.drawable.btn_play1);
        objectAnimator.pause();
    }

    private void resumeMusic() {
        mediaPlayer.start();
        imgplay.setImageResource(R.drawable.btn_pause);
        objectAnimator.resume();
        updateSeekBar();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateSeekBar() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (mediaPlayer.isPlaying()) {
                    try {
                        publishProgress();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                int duration = mediaPlayer.getDuration();
                int currentPosition = mediaPlayer.getCurrentPosition();

                skBar.setMax(duration);
                skBar.setProgress(currentPosition);

                timeStart.setText(fmMilisecondsToTimeString(currentPosition));
                timeEnd.setText(fmMilisecondsToTimeString(duration));
            }
        }.execute();
    }


    private void showAddToPlaylistDialog() {
        // Giả sử bạn có một mảng hoặc danh sách các playlist
        final String[] playlists = {"Workout", "Favorites", "Chill"}; // Lấy danh sách từ SharedPreferences

        // Thêm một tùy chọn để tạo playlist mới
        List<String> playlistOptions = new ArrayList<>(Arrays.asList(playlists));
        playlistOptions.add("Tạo playlist mới...");

        AlertDialog.Builder builder = new AlertDialog.Builder(PlayMusicActivity.this);
        builder.setTitle("Thêm vào playlist");
        builder.setItems(playlistOptions.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == playlistOptions.size() - 1) {
                    // Người dùng chọn tạo playlist mới
                    showCreatePlaylistDialog();
                } else {
                    // Thêm bài hát vào playlist đã chọn
                    saveSongToPlaylist(playlistOptions.get(which), selectedSong.getId());
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showCreatePlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayMusicActivity.this);
        builder.setTitle("Tạo playlist mới");

        final EditText input = new EditText(PlayMusicActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPlaylistName = input.getText().toString();
                if (!newPlaylistName.isEmpty()) {
                    // Lưu playlist mới và thêm bài hát vào playlist đó
                    savePlaylist(newPlaylistName);
                    saveSongToPlaylist(newPlaylistName, selectedSong.getId());
                }
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Hàm này lưu tên của playlist mới vào SharedPreferences
    private void savePlaylist(String newPlaylistName) {
        SharedPreferences prefs = getSharedPreferences("Playlists", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> playlists = prefs.getStringSet("playlists", new HashSet<String>());
        playlists.add(newPlaylistName); // Thêm tên playlist mới
        editor.putStringSet("playlists", playlists);
        editor.apply();
    }

    private void saveSongToPlaylist(String playlistName, long songId) {
        SharedPreferences prefs = this.getSharedPreferences("Playlists", Context.MODE_PRIVATE);  // Sử dụng this thay vì getActivity()
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> songIds = prefs.getStringSet(playlistName, new HashSet<>());
        songIds.add(String.valueOf(songId));
        editor.putStringSet(playlistName, new HashSet<>(songIds));
        editor.apply();
        Toast.makeText(this, "Đã thêm vào playlist: " + playlistName, Toast.LENGTH_SHORT).show();
    }


    private void saveFavoriteSong(long songId) {
        Set<String> favorites = prefs.getStringSet("favoriteSongs", new HashSet<String>());
        favorites.add(String.valueOf(songId));
        prefs.edit().putStringSet("favoriteSongs", favorites).apply();
    }

    private void removeFavoriteSong(long songId) {
        Set<String> favorites = prefs.getStringSet("favoriteSongs", new HashSet<>());
        favorites.remove(String.valueOf(songId));
        prefs.edit().putStringSet("favoriteSongs", favorites).apply();
    }
    private Set<String> getFavoriteSongs() {
        return prefs.getStringSet("favoriteSongs", new HashSet<>());
    }

}




