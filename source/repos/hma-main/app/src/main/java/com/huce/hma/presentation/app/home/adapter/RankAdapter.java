package com.huce.hma.presentation.app.home.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog; // Added import for AlertDialog
import com.huce.hma.R;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.presentation.app.playlist.PlaylistActivity;
import com.huce.hma.presentation.app.playmusic.PlayMusicActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RankAdapter extends ArrayAdapter<SongSearchResultDTO> {
    Context context;
    ArrayList<SongSearchResultDTO> arrayList;
    int layoutResource;

    // Constructor
    public RankAdapter(Context context, int resource, ArrayList<SongSearchResultDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arrayList = objects;
        this.layoutResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutResource, null);

        // Binding data to views
        TextView songName = convertView.findViewById(R.id.tv_songName);
        songName.setText(arrayList.get(position).getName());

        TextView txt2 = convertView.findViewById(R.id.tv_songArtist);
        txt2.setText(arrayList.get(position).getArtistName());

        ImageView img = convertView.findViewById(R.id.song_image);
        Picasso.get().load(arrayList.get(position).getThumbnail()).into(img);

        ImageView addToPlaylistIcon = convertView.findViewById(R.id.add_to_playlist_icon);

        // Set onClickListener for addToPlaylistIcon
        addToPlaylistIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPlaylist(position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start PlayMusicActivity when clicked on the list item
                SongSearchResultDTO selectedSong = arrayList.get(position);
                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putExtra("selected_song", selectedSong);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    // Method to handle adding to playlist
    private void addToPlaylist(int position) {
        // Display a dialog when addToPlaylistIcon is clicked
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Đã thêm vào playlist")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        // Navigate to PlaylistActivity to handle adding to playlist
                        Intent intent = new Intent(context, PlaylistActivity.class);
                        context.startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
