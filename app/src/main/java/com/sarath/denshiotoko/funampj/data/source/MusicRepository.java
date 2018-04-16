package com.sarath.denshiotoko.funampj.data.source;

import android.support.annotation.NonNull;

import com.sarath.denshiotoko.funampj.data.Song;

import java.util.List;

public interface MusicRepository {

    interface LoadMusicCallback {

        void onMusicLoaded(List<Song> songs);

        void onDataNotAvailable();
    }

    void getSongs(@NonNull LoadMusicCallback callback);
}
