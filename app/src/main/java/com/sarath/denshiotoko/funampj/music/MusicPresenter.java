package com.sarath.denshiotoko.funampj.music;

import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.sarath.denshiotoko.funampj.data.Song;
import com.sarath.denshiotoko.funampj.data.source.MusicRepository;
import com.sarath.denshiotoko.funampj.data.source.MusicRepositoryImpl;
import com.sarath.denshiotoko.funampj.di.ActivityScoped;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
        if(currentSong!= null && mLyricView!=null){
            loadSongLyrics(currentSong.getSongArtist(), currentSong.getSongTitle());
        }
    }

    @Override
    public void loadSongLyrics(String artist, String song) {
        artist = artist.toLowerCase();
        if(artist.trim().startsWith("the")){
            artist = artist.substring(3);
        }
        artist= artist.replaceAll("\\s+","");
        song = song.toLowerCase().replaceAll("\\s+","");
        String url = "http://azlyrics.com/lyrics/"+artist+"/"+song+".html";
        new RetrieveSongLyrics(mLyricView).execute(url);
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

        RetrieveSongLyrics(MusicContract.LyricView lyricView) {
            this.lyricView = lyricView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lyricView.showProgressBar();
        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                Document doc = Jsoup.connect(urls[0]).get();
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
            if(s.isEmpty())
                lyricView.showNoLyrics();
            else
                lyricView.showLyrics(s);

            lyricView.stopProgressBar();
        }
    }
}
