package com.huce.hma.presentation.app.home.component;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson; // Import Gson nếu bạn sử dụng Gson để serialize/deserialize.
import com.google.gson.reflect.TypeToken;
import com.huce.hma.R;
import com.huce.hma.common.component.AdapterLinearLayout;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.component.PopupFragment;
import com.huce.hma.data.local.model.Song;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.app.home.ArtistDetailActivity;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.presentation.app.home.adapter.AlbumAdapter;
import com.huce.hma.presentation.app.home.adapter.PlaylistAdapter;
import com.huce.hma.presentation.app.home.adapter.RecentSongAdapter;
import com.huce.hma.presentation.app.home.adapter.TrendingAdapter;
import com.huce.hma.presentation.app.playmusic.PlayMusicActivity;
import com.huce.hma.presentation.guest.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private static final int YOUR_REQUEST_CODE = 1;
    private HorizontalScrollView scrollView;
    private ListView listView;
    private GridView gridView;
    private  GridView artistsView;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewPlaylist;

    private LoadingDialog loadingFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.component_app_home_home, container, false);

        scrollView = view.findViewById(R.id.scv_trending);
        listView = view.findViewById(R.id.list_recent_song);
        gridView = view.findViewById(R.id.grid_album);
        loadingFragment = new LoadingDialog(getActivity());
        recyclerView = view.findViewById(R.id.recycler_view_playlist);
        if (recyclerView != null) {
            recyclerView.setNestedScrollingEnabled(false);  // Vô hiệu hóa nested scrolling
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            // Cài đặt adapter và các thiết lập khác cho recyclerView ở đây
            loadPlaylist();
        } else {
            Log.e("HomeFragment", "RecyclerView is null");
        }
        getData();
        ImageView imageViewTaylor = view.findViewById(R.id.thuyloan);
        imageViewTaylor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openArtistDetailActivity();
            }
        });
        return view;

    }
    private void openArtistDetailActivity() {
        Intent intent = new Intent(getActivity(), ArtistDetailActivity.class);
        startActivity(intent);
    }
    public void getData() {
        String keyword = " ";
        Call<HttpResponse<ArrayList<SongSearchResultDTO>>> repos = Retrofit.getInstance().RequestAuth(getContext()).SearchSongsByKeyword(keyword);
        loadingFragment.startLoading();
        repos.enqueue(new Callback<HttpResponse<ArrayList<SongSearchResultDTO>>>() {
            @Override
            public void onResponse(Call<HttpResponse<ArrayList<SongSearchResultDTO>>> call, Response<HttpResponse<ArrayList<SongSearchResultDTO>>> response) {
                loadingFragment.stopLoading();
                if (!response.isSuccessful()) {
//                    try {
//                        Gson gson = new Gson();
//                        HttpResponse<ArrayList<SongDTO>> res = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
//                    } catch (Exception e) {
//                        HttpResponse<ArrayList<SongDTO>> res = new HttpResponse<>();
//                    }

                    return;
                }

                HttpResponse<ArrayList<SongSearchResultDTO>> post = response.body();

                RecentSongAdapter recentSongAdapter = new RecentSongAdapter(getContext(), R.layout.component_music_download,post.getData());
                listView.setAdapter(recentSongAdapter);
                AlbumAdapter albumAdapter = new AlbumAdapter(getContext(), R.layout.component_music_album,post.getData());
                gridView.setAdapter(albumAdapter);
                gridView.setNumColumns(2);

                TrendingAdapter trendingAdapter = new TrendingAdapter(getContext(), R.layout.component_music_trending,post.getData());
                AdapterLinearLayout adapterLinearLayout = new AdapterLinearLayout(getContext());
                adapterLinearLayout.setAdapter(trendingAdapter);

                scrollView.addView(adapterLinearLayout);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SongSearchResultDTO selectedSong = post.getData().get(position);
                        Intent intent = new Intent(getContext(), PlayMusicActivity.class);
                        intent.putExtra("selected_song", selectedSong);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<HttpResponse<ArrayList<SongSearchResultDTO>>> call, Throwable t) {
                // Xử lý khi request thất bại
                loadingFragment.stopLoading();
               // Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                PopupFragment popupFragment = new PopupFragment(
                        getActivity(),
                        PopupFragment.ERROR,
                        "Error",
                        t.getMessage(), new PopupFragment.OnShow() {
                });
                popupFragment.show();
            }
        });
    }
private Set<String> loadSongIdsFromPlaylist() {
    SharedPreferences prefs = getActivity().getSharedPreferences("Playlists", Context.MODE_PRIVATE);
    return prefs.getStringSet("myPlaylist", new HashSet<>());
}

    private List<Song> loadSongsFromPlaylist() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Playlists", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("songsJson", null); // Sử dụng tên khóa phù hợp
        if (json == null) {
            return new ArrayList<>();  // Trả về một danh sách rỗng nếu không có dữ liệu
        }
        Type type = new TypeToken<List<Song>>() {}.getType();
        return gson.fromJson(json, type);
    }



    private void loadPlaylist() {
        List<Song> songs = loadSongsFromPlaylist();
        if (songs == null) {
            songs = new ArrayList<>();
        }
        if (recyclerView != null) {
            PlaylistAdapter adapter = (PlaylistAdapter) recyclerView.getAdapter();

            if (adapter == null) {
                // Nếu chưa có Adapter, thiết lập nó
                adapter = new PlaylistAdapter(getContext(), songs);
                recyclerView.setAdapter(adapter);
            } else {
                // Nếu đã có Adapter, cập nhật dữ liệu mới
                adapter.updateSongList(songs);
                adapter.notifyDataSetChanged();
            }
        } else {
            Log.e("HomeFragment", "RecyclerView is not initialized.");
        }
    }


    private void updatePlaylist() {
        List<Song> songs = loadSongsFromPlaylist();
        PlaylistAdapter adapter = (PlaylistAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.updateSongList(songs); // Giả sử bạn có một phương thức updateSongList() trong PlaylistAdapter
            adapter.notifyDataSetChanged();
        }
    }


}
