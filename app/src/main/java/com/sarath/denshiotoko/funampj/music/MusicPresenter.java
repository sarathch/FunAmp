package com.sarath.denshiotoko.funampj.music;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import com.sarath.denshiotoko.funampj.data.Song;
import com.sarath.denshiotoko.funampj.data.source.MusicRepository;
import com.sarath.denshiotoko.funampj.data.source.MusicRepositoryImpl;
import com.sarath.denshiotoko.funampj.di.ActivityScoped;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.annotation.Documented;
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

    @Nullable
    private MusicContract.LyricView mLyricView;

    private Song currentSong = null;

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
        currentSong = song;
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
    public void loadSongLyrics() {
        Log.v("mPresenter","fetch song lyrics");
        String lyrics="";
        if(currentSong!= null && mLyricView!=null){
            String url = "";
            String artist = currentSong.getSongArtist().toLowerCase();
            if(artist.trim().startsWith("the")){
                artist = artist.substring(3);
            }
            artist= artist.replaceAll("\\s+","");
            String songTitle = currentSong.getSongTitle().toLowerCase().replaceAll("\\s+","");
            url = "http://azlyrics.com/lyrics/"+artist+"/"+songTitle+".html";
            new RetrieveSongLyrics(mLyricView).execute(url);
        }
    }

    @Override
    public Song fetchCurrentSongState() {
        return currentSong;
    }

    @Override
    public void takeMusicView(MusicContract.MusicView view) {
        mMusicView = view;
        loadMusic();
    }

    @Override
    public void dropMusicView() {
        mMusicView = null;
    }

    @Override
    public void takeLyricView(MusicContract.LyricView view) {
        mLyricView = view;
    }

    @Override
    public void dropLyricView() {
        mLyricView = null;
    }

    private static class RetrieveSongLyrics extends AsyncTask<String, Void, String> {

        private MusicContract.LyricView lyricView;

        public RetrieveSongLyrics(MusicContract.LyricView lyricView) {
            this.lyricView = lyricView;
        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                Document doc = Jsoup.connect(urls[0]).get();
                Log.v("mPresenter",doc.toString());
                String lyrics = doc.body().toString();
                //lyrics lies between up_partition and down_partition
                String start = "<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->";
                String end = "<!-- MxM banner -->";
                int start_index = lyrics.indexOf(start) + start.length();
                int end_index = lyrics.indexOf(end);
                if(start_index >= 0 && end_index >=0){
                    lyrics = lyrics.substring(start_index, end_index+1);
                    lyrics = lyrics.replace("<br>","");
                    lyrics = lyrics.replace("</div>","");
                    return "     "+lyrics;
                }
                return "";
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lyricView.showLyrics(s);
        }
    }
}
