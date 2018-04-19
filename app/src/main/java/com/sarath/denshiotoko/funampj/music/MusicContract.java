package com.sarath.denshiotoko.funampj.music;

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

    interface LyricView extends BaseView<Presenter>{

        void setCurrentSongDetails(Song song);

        void showLyrics(String lyrics);

        void showNoLyrics();

        void showProgressBar();

        void stopProgressBar();

    }

    interface Presenter {

        //fetch Songs
        void loadMusic();

        // play song
        void playSong(Song song);

        // stop songs
        void stopSong();

        //fetch Lyrics
        void loadSongLyrics();

        //fetch Lyrics with artist name and song name
        void loadSongLyrics(String artist, String song);

        // fetch Current Song
        Song fetchCurrentSongState();

        /**
         * Binds presenter with Music view when resumed. The Presenter will perform initialization here.
         *
         * @param view the view associated with this presenter
         */
        void takeMusicView(MusicView view);

        /**
         * Drops the reference to the view when destroyed
         */
        void dropMusicView();

        /**
         * Binds presenter with Music view when resumed. The Presenter will perform initialization here.
         *
         * @param view the view associated with this presenter
         */
        void takeLyricView(LyricView view);

        /**
         * Drops the reference to the view when destroyed
         */
        void dropLyricView();

    }
}
