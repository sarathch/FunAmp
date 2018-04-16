package com.sarath.denshiotoko.funampj.data.source;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sarath.denshiotoko.funampj.MyMusicApplication;
import com.sarath.denshiotoko.funampj.data.Song;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MusicRepositoryImpl implements MusicRepository{


    Context context;

    @Inject
    public MusicRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getSongs(@NonNull final LoadMusicCallback callback) {

        List<Song> musicList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                Long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentSongData = songCursor.getString(songData);
                musicList.add(new Song(currentId, currentTitle, currentSongData));
            } while (songCursor.moveToNext());
            songCursor.close();
        }
        if(!musicList.isEmpty())
            callback.onMusicLoaded(musicList);
        else
            callback.onDataNotAvailable();
    }
}
