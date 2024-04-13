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

public class TrendingAdapter extends ArrayAdapter<SongSearchResultDTO> {
    Context context;
    ArrayList<SongSearchResultDTO> arrayList;
    int layoutResource;
    //Hàm khởi tạo cho CustomArrayAdapter
    public TrendingAdapter(Context context, int resource, ArrayList<SongSearchResultDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arrayList = objects;
        this.layoutResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    //Hàm khởi tạo cho các dòng để hiển thị trên ListView
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutResource,null);
        //Hàm khởi thêm dữ lieu vào các View từ arrayList thông qua position
        TextView songName = (TextView)convertView.findViewById(R.id.trending_name);
        songName.setText(arrayList.get(position).getName());

        TextView descr = (TextView)convertView.findViewById(R.id.trending_description);
        descr.setText(arrayList.get(position).getArtistName());

        ImageView img = (ImageView) convertView.findViewById(R.id.trending_image);

        Picasso.get().load(arrayList.get(position).getThumbnail()).fit().centerCrop()
                .into(img);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongSearchResultDTO selectedSong = arrayList.get(position);
                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putExtra("selected_song", selectedSong);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
