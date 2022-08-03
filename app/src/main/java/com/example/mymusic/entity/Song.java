package com.example.mymusic.entity;

import java.io.Serializable;

public class Song implements Serializable {
    private int mp3;
    private int picId;
    private String songName;
    private String author;
    private boolean isPlayed;
    private boolean isPlaying;

    public Song(int picId, int mp3, String songName, String author, boolean isPlayed) {
        this.picId = picId;
        this.songName = songName;
        this.author = author;
        this.isPlayed = isPlayed;
        this.mp3 = mp3;
        this.isPlaying = false;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public int getMp3() {
        return mp3;
    }

    public void setMp3(int mp3) {
        this.mp3 = mp3;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
