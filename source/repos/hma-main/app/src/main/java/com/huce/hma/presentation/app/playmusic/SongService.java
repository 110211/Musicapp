package com.huce.hma.presentation.app.playmusic;
import com.huce.hma.data.remote.dto.HttpResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
public interface SongService {
    @POST("song/{id}/like")
    Call<HttpResponse<Object>> likeSong(@Path("id") String songId);

    @DELETE("song/{id}/like")
    Call<HttpResponse<Object>> unlikeSong(@Path("id") String songId);
}
