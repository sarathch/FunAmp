package com.sarath.denshiotoko.funampj.data;

public class Song {

    private Long songId;

    private String songTitle;

    private String songData;

    public Song(Long songId, String songTitle, String songData) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.songData = songData;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongData() {
        return songData;
    }

    public void setSongData(String songData) {
        this.songData = songData;
    }
}
