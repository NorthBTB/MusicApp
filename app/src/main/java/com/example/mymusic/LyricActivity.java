package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.entity.Song;
import com.example.mymusic.utils.BindService;

import java.util.ArrayList;
import java.util.List;

public class LyricActivity extends AppCompatActivity implements View.OnClickListener, ISongAdapter {

    private List<Song> songs;

    private ImageView iv_l_song_avatar, iv_l_pre, iv_l_play, iv_l_next;
    private TextView tv_l_song_name, tv_l_author, tv_l_second;
    private SeekBar sb_second;

    private BindService bindService;
    private Handler handler = new Handler();

    private int cr_song_position;
    private boolean isServiceConnected;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BindService.MyBinder myBinder = (BindService.MyBinder) iBinder;
            bindService = myBinder.getService();
            isServiceConnected = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bindService = null;
            isServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        initSongs();

        initView();

        initEvent();
        //isServiceConnected = true;
    }

    private void initEvent() {
        Intent intent = new Intent(LyricActivity.this, BindService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(this, isServiceConnected + "", Toast.LENGTH_SHORT).show();

        //runSeekBar();
        iv_l_play.setOnClickListener(this);
        iv_l_next.setOnClickListener(this);
        iv_l_pre.setOnClickListener(this);
    }

    private void initView() {
        iv_l_song_avatar = findViewById(R.id.iv_l_song_avatar);
        iv_l_play = findViewById(R.id.iv_ly_play);
        iv_l_pre = findViewById(R.id.iv_ly_pre);
        iv_l_next = findViewById(R.id.iv_ly_next);

        sb_second = findViewById(R.id.sb_song);
        sb_second.setClickable(false);

        tv_l_song_name = findViewById(R.id.tv_l_song_name);
        tv_l_author = findViewById(R.id.tv_l_author);
        tv_l_second = findViewById(R.id.tv_second);

        cr_song_position = getIntent().getIntExtra("cr_song", 0);
        Song s = songs.get(cr_song_position);
        iv_l_song_avatar.setImageResource(s.getPicId());
        tv_l_song_name.setText(s.getSongName());
        tv_l_author.setText(s.getAuthor());

        iv_l_play.setImageResource(getIntent().getBooleanExtra("is_played", false) ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24);
    }

    private void initSongs() {
        songs = new ArrayList<>();
        songs.add(new Song(R.drawable.mtp, R.raw.con_mua_ngang_qua, "Cơn mưa ngang qua", "Sơn Tùng-MTP", false));
        songs.add(new Song(R.drawable.mood, R.raw.mood, "Mood", "24KGoldn ft. Iann Dior", false));
        songs.add(new Song(R.drawable.mtp, R.raw.con_mua_ngang_qua, "Hãy trao cho anh", "Sơn Tùng-MTP", false));
        songs.add(new Song(R.drawable.fools_garden, R.raw.lemon_tree, "Lemon Tree", "Fool's Gardeen", false));
        songs.add(new Song(R.drawable.maroon_5, R.raw.sugar, "Sugar", "Maroon 5", false));
        songs.add(new Song(R.drawable.justin_bieber, R.raw.sorry, "Sorry", "Justin Bieber", false));
        songs.add(new Song(R.drawable.justin_bieber, R.raw.mood, "Baby", "Justin Bieber", false));
        songs.add(new Song(R.drawable.image_1, R.raw.mood, "Em gái mưa", "Hương Tràm", false));
        songs.add(new Song(R.drawable.image_2, R.raw.mood, "Hơn cả yêu", "Dức Phúc", false));
        songs.add(new Song(R.drawable.image_3, R.raw.mood, "Simple Cypher", "Low G", false));
        songs.add(new Song(R.drawable.image_4, R.raw.mood, "Chán gái 707", "Low G", false));
        songs.add(new Song(R.drawable.image_5, R.raw.mood, "Không quen", "Chí", false));
        if (getIntent().getIntExtra("cr_playing_song", 0) != -1) {
            songs.get(getIntent().getIntExtra("cr_playing_song", 0)).setPlayed(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_ly_play:
                //onPlay(cr_song_position);

                break;

            case R.id.iv_ly_pre:
                onPre();
                break;

            case R.id.iv_ly_next:
                onNext();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LyricActivity.this, BindService.class);
        stopService(intent);
        if (isServiceConnected) {
            unbindService(serviceConnection);
            isServiceConnected = false;
        }
        finish();
    }

    @Override
    public List<Song> getSongs() {
        return songs;
    }

    @Override
    public void onLongClickItem(int position, int songPosition) {

    }

    @Override
    public void onPlay(int songPosition) {
        cr_song_position = songPosition;
        Song cr_song = getSongs().get(cr_song_position);
        if (cr_song.isPlayed()) {
            iv_l_play.setImageResource(R.drawable.ic_baseline_pause_24);

        } else {
            iv_l_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        iv_l_song_avatar.setImageResource(cr_song.getPicId());
        tv_l_song_name.setText(cr_song.getSongName());
        tv_l_author.setText(cr_song.getAuthor());
    }

    @Override
    public void onClickFooter(int songPositon) {

    }

    @Override
    public void onClickHeader() {

    }

    private void onPre() {
        if (cr_song_position == 0) {
            cr_song_position = songs.size() - 1;
        } else {
            cr_song_position--;
        }
//        onPlayCurrentSong();
        onPlay(cr_song_position);
    }

    private void onNext() {
        if (cr_song_position == songs.size() - 1) {
            cr_song_position = 0;
        } else {
            cr_song_position++;
        }
        //onPlayCurrentSong();
        onPlay(cr_song_position);
    }

    private String convertSecondsToString(int seconds) {
        long m = seconds / 60;
        long s = seconds - 60 * m;
        return m + ":" + s;
    }

    private class UpdateSeekBarRunnable implements Runnable {

        @Override
        public void run() {
            int currentPosition = bindService.getMediaPlayer().getCurrentPosition(); // milliseconds
            tv_l_second.setText(convertSecondsToString(currentPosition) + " / " + convertSecondsToString(bindService.getMediaPlayer().getDuration()));
            sb_second.setProgress(currentPosition);

            handler.postDelayed(this, 1000);
        }
    }

    private void runSeekBar() {
        sb_second.setMax(bindService.getMediaPlayer().getDuration());
        handler.post(new UpdateSeekBarRunnable());
    }

}