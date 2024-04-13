package com.huce.hma.presentation.app.home.component;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huce.hma.R;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.component.PopupFragment;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.presentation.app.home.adapter.SearchResultAdapter;
import com.huce.hma.presentation.app.playmusic.PlayMusicActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private boolean isLoading;
    private LoadingDialog loadingFragment;
    private String keyword;
    private ListView listView;
    private EditText searchEditText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.component_app_home_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        listView = view.findViewById(R.id.resultListView);

        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(!searchEditText.getText().toString().isEmpty()) {
                    getData();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return view;
    }

    public void getData() {
        String keyword = searchEditText.getText().toString();
        Call<HttpResponse<ArrayList<SongSearchResultDTO>>> repos = Retrofit.getInstance().RequestAuth(getContext()).SearchSongsByKeyword(keyword);

        repos.enqueue(new Callback<HttpResponse<ArrayList<SongSearchResultDTO>>>() {
            @Override
            public void onResponse(Call<HttpResponse<ArrayList<SongSearchResultDTO>>> call, Response<HttpResponse<ArrayList<SongSearchResultDTO>>> response) {
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
                SearchResultAdapter searchResultAdapter = new SearchResultAdapter(getContext(), R.layout.component_music_download,post.getData());
                listView.setAdapter(searchResultAdapter);
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
