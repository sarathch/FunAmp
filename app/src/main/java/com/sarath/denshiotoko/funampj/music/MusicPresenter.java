package com.sarath.denshiotoko.funampj.music;

import android.media.MediaPlayer;
import android.util.Log;

import com.sarath.denshiotoko.funampj.data.Song;
import com.sarath.denshiotoko.funampj.data.source.MusicRepository;
import com.sarath.denshiotoko.funampj.data.source.MusicRepositoryImpl;
import com.sarath.denshiotoko.funampj.di.ActivityScoped;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

@ActivityScoped
public class MusicPresenter implements MusicContract.Presenter {

    private final MusicRepositoryImpl mMusicRepository;

    private MediaPlayer mp=null;

    @Nullable
    private MusicContract.MusicView mMusicView;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    MusicPresenter(MusicRepositoryImpl musicRepository) {
        mMusicRepository = musicRepository;
    }

    @Override
    public void loadMusic() {

        if (mMusicView!=null) {
            mMusicRepository.getSongs(new MusicRepository.LoadMusicCallback() {
                @Override
                public void onMusicLoaded(List<Song> songs) {
                    mMusicView.showMusic(songs);
                }

                @Override
                public void onDataNotAvailable() {
                    mMusicView.showNoMusic();
                }
            });
        }
    }

    public int return1()
    {
        return 1;
    }

    @Override
    public void playSong(Song song) {

        try{
            if(mp != null)
                mp.stop();

            mp = new MediaPlayer();
            mp.setDataSource(song.getSongData());
            mp.prepare();
            mp.start();
            if (mMusicView != null) {
                mMusicView.showSongMessage(song.getSongTitle(), "play");
            }
        }catch(Exception e){
            mp = null;
            if (mMusicView != null) {
                mMusicView.showSongMessage(song.getSongTitle(), "error");
            }
        }

    }

    @Override
    public void stopSong() {

        if(mp != null) {
            mp.stop();
            if (mMusicView != null) {
                mMusicView.showSongMessage("", "stop");
            }
        }else {
            if (mMusicView != null) {
                mMusicView.showSongMessage("", "error");
            }
        }

        mp = null;
    }

    @Override
    public void takeView(MusicContract.MusicView view) {
        mMusicView = view;
        loadMusic();
    }

    @Override
    public void dropView() {
        mMusicView = null;
    }
}
