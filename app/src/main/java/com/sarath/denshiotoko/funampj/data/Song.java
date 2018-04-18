package com.sarath.denshiotoko.funampj.data;

public class Song {

    private Long songId;

    private String songTitle;

    private String songArtist;

    private String songData;

    public Song(Long songId, String songTitle, String songArtist, String songData) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
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

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }
}
