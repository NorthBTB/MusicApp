package com.example.mymusic;

import com.example.mymusic.entity.Song;

import java.util.List;

public interface ISongAdapter {
    List<Song> getSongs();

    void onLongClickItem(int position, int songPosition);

    void onPlay(int songPosition);

    void onClickFooter(int songPositon);

    void onClickHeader();

}
