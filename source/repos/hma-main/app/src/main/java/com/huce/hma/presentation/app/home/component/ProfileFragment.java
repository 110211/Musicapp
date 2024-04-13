package com.huce.hma.presentation.app.home.component;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huce.hma.R;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.data.common.dto.User;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.app.home.DTO.ProfileDTO;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.presentation.app.home.adapter.AlbumAdapter;
import com.huce.hma.presentation.app.playmusic.PlayMusicActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private boolean isLoading;
    private LoadingDialog loadingFragment;
    private String keyword;
    private ListView listView;
    private TextView username;
    private TextView description;
    private GridView gridView;
    private boolean isAlbumButtonClicked = false;
    private boolean isLikesButtonClicked = false;
    private Button btn_setting;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.component_app_profile_user_infomation, container, false);
        username = view.findViewById(R.id.user_name);
        description = view.findViewById(R.id.description);
        gridView = view.findViewById(R.id.grid_album);
        Button btnAlbums = view.findViewById(R.id.btn_show_album);
        Button btnLikes = view.findViewById(R.id.btn_show_likes);
        btn_setting = view.findViewById(R.id.btn_user_setting);
        btnAlbums.callOnClick();
        getData();
        getMe();
        btnAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                btnAlbums.setBackgroundColor(getResources().getColor(R.color.white));
                btnLikes.setBackgroundColor(getResources().getColor(R.color.white_transparent));
            }
        });
        btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                btnLikes.setBackgroundColor(getResources().getColor(R.color.white));
                btnAlbums.setBackgroundColor(getResources().getColor(R.color.white_transparent));
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public void getMe() {
        Call<HttpResponse<ProfileDTO>> repos = Retrofit.getInstance().RequestAuth(getContext()).getProfile();
        repos.enqueue(new Callback<HttpResponse<ProfileDTO>>() {
            @Override
            public void onResponse(Call<HttpResponse<ProfileDTO>> call, Response<HttpResponse<ProfileDTO>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                HttpResponse<ProfileDTO> post = response.body();
                username.setText(post.getData().getUsername());
                description.setText(post.getData().getEmail());
            }
            @Override
            public void onFailure(Call<HttpResponse<ProfileDTO>> call, Throwable t) {
                // Xử lý khi request thất bại
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                HttpResponse<ArrayList<User>> res = new HttpResponse<>();
                res.setStatus(400);
                res.setMessage("Thất bại");
            }
        });
    }

    public void getData() {
        String keyword = " ";
        Call<HttpResponse<ArrayList<SongSearchResultDTO>>> repos = Retrofit.getInstance().RequestAuth(getContext()).SearchSongsByKeyword(keyword);
        repos.enqueue(new Callback<HttpResponse<ArrayList<SongSearchResultDTO>>>() {
            @Override
            public void onResponse(Call<HttpResponse<ArrayList<SongSearchResultDTO>>> call, Response<HttpResponse<ArrayList<SongSearchResultDTO>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                HttpResponse<ArrayList<SongSearchResultDTO>> post = response.body();
                AlbumAdapter albumAdapter = new AlbumAdapter(getContext(), R.layout.component_music_album,post.getData());
                gridView.setAdapter(albumAdapter);
                gridView.setNumColumns(2);
            }
            @Override
            public void onFailure(Call<HttpResponse<ArrayList<SongSearchResultDTO>>> call, Throwable t) {
                // Xử lý khi request thất bại
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                HttpResponse<ArrayList<SongSearchResultDTO>> res = new HttpResponse<>();
                res.setStatus(400);
                res.setMessage("Thất bại");
            }
        });
    }
}
