package com.huce.hma.presentation.app.playmusic;


import static com.huce.hma.common.utils.Utils.fmMilisecondsToTimeString;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huce.hma.BuildConfig;
import com.huce.hma.R;
import com.huce.hma.common.services.MediaPlayerProvider;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.presentation.app.home.adapter.SearchResultAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayMusicActivity extends AppCompatActivity {
    ImageButton imgback, imgshare, imgsetting, imgadd, imgdow, imgfavorite, imgprevious, imgplay, imgnext,imgrepeat;
    SeekBar skBar;

    ImageView circleImageView;
    ObjectAnimator objectAnimator;
    MediaPlayer mediaPlayer;
    MediaPlayer player;
    TextView timeStart,timeEnd,nameSong,nameAlbum,nameAuthor;
    private final String channelId = "music_channel";
    private final int notificationId = 1;
    private int currentPosition = 0;
    private boolean isRepeat = false;
    private boolean like = false;

    private SongSearchResultDTO selectedSong;


    @SneakyThrows
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_music_play);

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


        Intent intent = getIntent();
        selectedSong = (SongSearchResultDTO) intent.getSerializableExtra("selected_song");
        initializeMediaPlayer();

        nameSong.setText(selectedSong.getName());
        nameAuthor.setText(selectedSong.getArtistName());
        nameAlbum.setText(selectedSong.getName());
        imgfavorite.setImageResource(selectedSong.getIsLiked() == 1 ? R.drawable.ic_like : R.drawable.icon_heart);
        Picasso.get().load(selectedSong.getThumbnail()).into(circleImageView);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLike();
                like = !like;
                updateLikeButton();
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
        imgrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeat = !isRepeat;
                imgrepeat.setImageResource(!isRepeat ? R.drawable.btn_loop : R.drawable.ic_loop);
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(isRepeat);
                }
            }
        });
        imgprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
            }
        });
        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                } else {
                    resumeMusic();
                }
            }
        });
        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
            }
        });

    }

    private void updateLikeButton() {
        if (!like) {
            imgfavorite.setImageResource(R.drawable.icon_heart);
        } else {
            imgfavorite.setImageResource(R.drawable.ic_like);
        }
    }

    private void initializeMediaPlayer(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(BuildConfig.API_URL + "/api/v1/song/" + selectedSong.getId() + "/download");
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.prepareAsync();
        imgrepeat.setImageResource(R.drawable.btn_loop);
        imgplay.setImageResource(R.drawable.btn_pause);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.setLooping(isRepeat);
                mediaPlayer.start();
                updateSeekBar();
                objectAnimator.start();
            }
        });
    }

    private void resumeMusic() {
        mediaPlayer.reset();
        mediaPlayer.seekTo(currentPosition);
        try {
            mediaPlayer.setDataSource(BuildConfig.API_URL + "/api/v1/song/" + selectedSong.getId() + "/download");
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.prepareAsync();
        imgplay.setImageResource(R.drawable.btn_pause);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.setLooping(isRepeat);
                mediaPlayer.start();
                updateSeekBar();
                objectAnimator.resume();
            }
        });
    }

    private void pauseMusic() {
        imgplay.setImageResource(R.drawable.btn_play1);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            currentPosition = mediaPlayer.getCurrentPosition();
        }
        objectAnimator.pause();
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

                String timeStartText = fmMilisecondsToTimeString(currentPosition);
                String timeEndText = fmMilisecondsToTimeString(duration);

                timeStart.setText(timeStartText);
                timeEnd.setText(timeEndText);
            }
        }.execute();
    }

    private void setLike() {
        Call<HttpResponse<Object>> repos = Retrofit.getInstance().RequestAuth(getApplicationContext()).setLikeSong(selectedSong.getId());

        repos.enqueue(new Callback<HttpResponse<Object>>() {
            @Override
            public void onResponse(Call<HttpResponse<Object>> call, Response<HttpResponse<Object>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                HttpResponse<Object> post = response.body();
            }

            @Override
            public void onFailure(Call<HttpResponse<Object>> call, Throwable t) {
                // Xử lý khi request thất bại
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                HttpResponse<ArrayList<SongSearchResultDTO>> res = new HttpResponse<>();
                res.setStatus(400);
                res.setMessage("Thất bại");
            }
        });
    }

}
