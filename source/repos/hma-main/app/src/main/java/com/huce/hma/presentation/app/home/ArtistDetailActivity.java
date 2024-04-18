package com.huce.hma.presentation.app.home;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.huce.hma.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ArtistDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        String artistName = "Thúy Lan";
        String artistDescription = "Thông tin chi tiết về Thúy Lan...";

        // Hiển thị thông tin về ca sĩ trên giao diện
        ImageView artistImageView = findViewById(R.id.imageViewTaylor);
        // Đặt hình ảnh của ca sĩ (thay thế với hình ảnh thực tế của Thúy Lan)
        artistImageView.setImageResource(R.drawable.taylor);

        TextView artistNameTextView = findViewById(R.id.text_artist_name);
        artistNameTextView.setText(artistName);

        TextView artistDescriptionTextView = findViewById(R.id.text_artist_description);
        artistDescriptionTextView.setText(artistDescription);
    }
}

