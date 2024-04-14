package com.huce.hma.presentation.app.playmusic;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static SongService songService = null; // Biến để giữ instance của SongService

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) // Sử dụng tham số baseUrl cho linh hoạt
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            songService = retrofit.create(SongService.class); // Khởi tạo SongService khi Retrofit được khởi tạo
        }
        return retrofit;
    }

    public static SongService getSongService() {
        if (songService == null) {
            getClient("http://10.0.2.2:3000");
        }
        return songService;
    }
}
