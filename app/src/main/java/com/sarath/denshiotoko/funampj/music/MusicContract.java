package com.sarath.denshiotoko.funampj.music;

import com.sarath.denshiotoko.funampj.BasePresenter;
import com.sarath.denshiotoko.funampj.BaseView;
import com.sarath.denshiotoko.funampj.data.Song;

import java.util.List;

public interface MusicContract {

    interface MusicView extends BaseView<Presenter>{

        void showMusic(List<Song> songs);

        void showNoMusic();

        void showSongMessage(String sTitle, String msg);

        boolean isActive();

    }

    interface Presenter extends BasePresenter<MusicView>{

        //fetch Songs
        void loadMusic();

        // play song
        void playSong(Song song);

        // stop songs
        void stopSong();

    }
}
