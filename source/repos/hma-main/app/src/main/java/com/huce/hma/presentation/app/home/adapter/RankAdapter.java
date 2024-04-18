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
import android.content.SharedPreferences;
import android.content.Context;
import java.util.Set;
import java.util.HashSet;
import android.widget.Toast;

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
    private void addToPlaylist(final int position) {
        final SongSearchResultDTO selectedSong = arrayList.get(position);
        // TODO: Lấy danh sách tên playlist từ SharedPreferences
        final String[] playlists = {"Workout", "Favorites", "Chill"}; // Placeholder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn Playlist");
        builder.setItems(playlists, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSongToPlaylist(playlists[which], selectedSong);
            }
        });
        builder.show();
    }

    private void saveSongToPlaylist(String playlistName, SongSearchResultDTO song) {
        SharedPreferences prefs = context.getSharedPreferences("Playlists", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> songIds = prefs.getStringSet(playlistName, new HashSet<String>());
        songIds.add(String.valueOf(song.getId())); // Đảm bảo song có phương thức getId()
        editor.putStringSet(playlistName, songIds);
        editor.apply();
        Toast.makeText(context, "Đã thêm vào playlist: " + playlistName, Toast.LENGTH_SHORT).show();
    }


}
