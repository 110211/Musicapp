package com.huce.hma.presentation.app.home.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.huce.hma.data.local.model.Song;
import com.huce.hma.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.SongViewHolder> {
    private Context context;
    private List<Song> songList; // Đây là danh sách các đối tượng Song, không phải các ID bài hát

    public PlaylistAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.songTitle.setText(song.getName());  // Sử dụng getName() để lấy tên bài hát
        Picasso.get().load(song.getThumbnailPath()).into(holder.songThumbnail);  // Sử dụng getThumbnailPath()
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView songThumbnail;
        TextView songTitle;

        public SongViewHolder(View itemView) {
            super(itemView);
            songThumbnail = itemView.findViewById(R.id.song_thumbnail);
            songTitle = itemView.findViewById(R.id.text_song_title);
        }
    }
    public void updateSongList(List<Song> newSongs) {
        this.songList.clear();
        this.songList.addAll(newSongs);
        notifyDataSetChanged();
    }

}


