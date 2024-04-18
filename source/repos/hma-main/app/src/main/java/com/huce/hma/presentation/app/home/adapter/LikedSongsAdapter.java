package com.huce.hma.presentation.app.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huce.hma.R;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.presentation.app.playmusic.PlayMusicActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikedSongsAdapter extends ArrayAdapter<SongSearchResultDTO> {
    Context context;
    ArrayList<SongSearchResultDTO> arrayList;
    int layoutResource;

    public LikedSongsAdapter(Context context, int resource, ArrayList<SongSearchResultDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arrayList = objects;
        this.layoutResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutResource, null);

        TextView songName = convertView.findViewById(R.id.tv_songName);
        songName.setText(arrayList.get(position).getName());

        TextView artistName = convertView.findViewById(R.id.tv_songArtist);
        artistName.setText(arrayList.get(position).getArtistName());

        ImageView songImage = convertView.findViewById(R.id.song_image);
        Picasso.get().load(arrayList.get(position).getThumbnail()).into(songImage);

        convertView.setOnClickListener(v -> {
            SongSearchResultDTO selectedSong = arrayList.get(position);
            Intent intent = new Intent(context, PlayMusicActivity.class);
            intent.putExtra("selected_song", selectedSong);
            context.startActivity(intent);
        });

        return convertView;
    }
}
