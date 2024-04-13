package com.huce.hma.common.services;

import android.media.MediaPlayer;

public class MediaPlayerProvider extends MediaPlayer {
    private static MediaPlayerProvider instance;

    MediaPlayerProvider() {}

    public static MediaPlayerProvider getInstance() {
        if (instance == null) {
            instance = new MediaPlayerProvider();
        }
        return instance;
    }
}
