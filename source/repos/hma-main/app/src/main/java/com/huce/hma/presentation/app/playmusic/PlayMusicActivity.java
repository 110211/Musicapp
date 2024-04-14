package com.huce.hma.presentation.app.playmusic;

import static com.huce.hma.common.utils.Utils.fmMilisecondsToTimeString;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayMusicActivity extends AppCompatActivity {
    ImageButton imgBack, imgFavorite, imgPrevious, imgPlay, imgNext, imgRepeat;
    SeekBar seekBar;
    ImageView circleImageView;
    ObjectAnimator objectAnimator;
    MediaPlayer mediaPlayer;
    TextView timeStart, timeEnd, nameSong, nameAlbum, nameAuthor;
    private boolean isRepeat = false;
    private boolean like = false;
    private SongSearchResultDTO selectedSong;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_music_play);

        initView();
        setupListeners();
    }

    private void initView() {
        imgBack = findViewById(R.id.backButton);
        imgFavorite = findViewById(R.id.favoriteButton);
        imgPrevious = findViewById(R.id.previousButton);
        imgPlay = findViewById(R.id.playButton);
        imgNext = findViewById(R.id.nextButton);
        imgRepeat = findViewById(R.id.btn_loop);
        seekBar = findViewById(R.id.seekBar);
        circleImageView = findViewById(R.id.musicDiscImageView);
        timeStart = findViewById(R.id.timeStart);
        timeEnd = findViewById(R.id.timeEnd);
        nameSong = findViewById(R.id.textViewAlbum);
        nameAlbum = findViewById(R.id.textViewSong);
        nameAuthor = findViewById(R.id.textViewAuthor);

        Intent intent = getIntent();
        selectedSong = (SongSearchResultDTO) intent.getSerializableExtra("selected_song");
        if (selectedSong == null) {
            Toast.makeText(this, "No song data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeMediaPlayer();
    }

    private void setupListeners() {
        imgBack.setOnClickListener(v -> finish());
        imgFavorite.setOnClickListener(v -> toggleLike());
        imgRepeat.setOnClickListener(v -> toggleRepeat());
        imgPrevious.setOnClickListener(v -> skip(-5000)); // skip backwards 5 seconds
        imgNext.setOnClickListener(v -> skip(5000)); // skip forward 5 seconds
        imgPlay.setOnClickListener(v -> togglePlay());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void initializeMediaPlayer() {
        mediaPlayer = MediaPlayerProvider.getInstance();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(BuildConfig.API_URL + "/api/v1/song/" + selectedSong.getId() + "/download");
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Toast.makeText(this, "Error preparing media: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            mp.start();
            updateSeekBar();
        });

        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0, 360);
        objectAnimator.setDuration(10000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.start();

        updateUI();
    }

    private void togglePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            objectAnimator.pause();
            imgPlay.setImageResource(R.drawable.btn_play1);
        } else {
            mediaPlayer.start();
            objectAnimator.resume();
            imgPlay.setImageResource(R.drawable.btn_pause);
        }
        updateSeekBar();
    }

    private void toggleRepeat() {
        isRepeat = !isRepeat;
        mediaPlayer.setLooping(isRepeat);
        imgRepeat.setImageResource(isRepeat ? R.drawable.ic_loop : R.drawable.btn_loop);
    }

    private void skip(int ms) {
        if (mediaPlayer != null) {
            int newPosition = mediaPlayer.getCurrentPosition() + ms;
            if (newPosition < 0) newPosition = 0;
            else if (newPosition > mediaPlayer.getDuration()) newPosition = mediaPlayer.getDuration();
            mediaPlayer.seekTo(newPosition);
        }
    }

    private void toggleLike() {
        like = !like;
        SongService service = RetrofitClient.getSongService(); // Sử dụng RetrofitClient để lấy SongService
        Call<HttpResponse<Object>> call = service.likeSong(String.valueOf(selectedSong.getId()));
        call.enqueue(new Callback<HttpResponse<Object>>() {
            @Override
            public void onResponse(Call<HttpResponse<Object>> call, Response<HttpResponse<Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Like status updated", Toast.LENGTH_SHORT).show();
                } else {
                    like = !like; // revert if failed
                    Toast.makeText(getApplicationContext(), "Failed to update like status", Toast.LENGTH_SHORT).show();
                }
                updateUI();
            }

            @Override
            public void onFailure(Call<HttpResponse<Object>> call, Throwable t) {
                like = !like; // revert if failed
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private void updateUI() {
        imgFavorite.setImageResource(like ? R.drawable.ic_like : R.drawable.icon_heart);
        nameSong.setText(selectedSong.getName());
        nameAuthor.setText(selectedSong.getArtistName());
        //nameAlbum.setText(selectedSong.getAlbumName());
        Picasso.get().load(selectedSong.getThumbnail()).into(circleImageView);
    }

    private void updateSeekBar() {
        mHandler.postDelayed(updateSeekBarRunnable, 1000);
    }

    private Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.setMax(mediaPlayer.getDuration());
                timeStart.setText(fmMilisecondsToTimeString(mediaPlayer.getCurrentPosition()));
                timeEnd.setText(fmMilisecondsToTimeString(mediaPlayer.getDuration()));
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateSeekBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateSeekBarRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        objectAnimator.end();
    }
}
