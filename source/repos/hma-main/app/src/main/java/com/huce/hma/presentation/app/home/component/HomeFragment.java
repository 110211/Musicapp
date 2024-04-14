package com.huce.hma.presentation.app.home.component;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huce.hma.R;
import com.huce.hma.common.component.AdapterLinearLayout;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.component.PopupFragment;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.presentation.app.home.adapter.AlbumAdapter;
import com.huce.hma.presentation.app.home.adapter.RecentSongAdapter;
import com.huce.hma.presentation.app.home.adapter.TrendingAdapter;
import com.huce.hma.presentation.app.playmusic.PlayMusicActivity;
import com.huce.hma.presentation.guest.login.LoginActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final int YOUR_REQUEST_CODE = 1;
    private HorizontalScrollView scrollView;
    private ListView listView;
    private GridView gridView;
    private  GridView artistsView;
    private LoadingDialog loadingFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.component_app_home_home, container, false);
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        scrollView = view.findViewById(R.id.scv_trending);
        listView = view.findViewById(R.id.list_recent_song);
        gridView = view.findViewById(R.id.grid_album);
       // artistsView  = view.findViewById(R.id.grid_artists);
        if (loadingFragment == null) {
            loadingFragment = new LoadingDialog(getActivity());
        }
        getData();

        return view;
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
                    }
                });

            }

            @Override
            public void onFailure(Call<HttpResponse<ArrayList<SongSearchResultDTO>>> call, Throwable t) {
                // Xử lý khi request thất bại
                loadingFragment.stopLoading();
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
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
}
