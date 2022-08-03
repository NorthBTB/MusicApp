package com.example.mymusic.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.mymusic.R;
import com.example.mymusic.broadcast.NotificationReceiver;
import com.example.mymusic.entity.Song;

import java.util.ArrayList;
import java.util.List;

import static com.example.mymusic.MyApplication.CHANNEL_ID;

public class BindService extends Service {

    private Song cr_song;
    private final IBinder binder = new MyBinder();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int cr_second = 0,cr;
    private List<Song> songs;

    private final static int ACTION_PAUSE = 1;
    private final static int ACTION_RESUME = 2;
    private final static int ACTION_STOP = 3;
    private final static int ACTION_PRE = 4;
    private final static int ACTION_NEXT = 5;

    public class MyBinder extends Binder {
        public com.example.mymusic.utils.BindService getService() {
            return com.example.mymusic.utils.BindService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSongs();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //Song song = (Song) bundle.get("cr_song");
            if(cr_song == null){
                cr = bundle.getInt("cr");
                Song song = songs.get(cr);
                if (song != null) {
                    cr_song = song;
                    playSong(song);
                    sendNotification(song);
                }
            }
        }
        int actionmusic = intent.getIntExtra("action_music_service", 0);
        handelActionMusic(actionmusic);

        return START_STICKY;
    }

    private void initSongs(){
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
    }

    private void sendNotification(Song song) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        startForeground(1, createNotification(song));

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        builder.setContentTitle(getString(R.string.app_name));
//        builder.setContentText("abc");
//        builder.setSmallIcon(R.drawable.ic_baseline_music_note_24);
//
//        Notification notification = builder.build();
//
//        startForeground(1, notification);
    }

    private Notification createNotification(Song song) {
        Song cr_song = song;
        RemoteViews collapsedLayout = new RemoteViews(this.getPackageName(), R.layout.layout_notification_custom_collapsed);
        RemoteViews expandedLayout = new RemoteViews(this.getPackageName(), R.layout.layout_notification_custom_expanded);

        //ui
        collapsedLayout.setImageViewResource(R.id.im_avatar_notification_music, cr_song.getPicId());
        collapsedLayout.setTextViewText(R.id.tv_title_video_notification_music, cr_song.getSongName());
        collapsedLayout.setTextViewText(R.id.tv_channel_notification_music, cr_song.getAuthor());
        collapsedLayout.setImageViewResource(R.id.im_play_notification_music, isPlaying ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24);

        expandedLayout.setImageViewResource(R.id.im_avatar_notification_music, cr_song.getPicId());
        expandedLayout.setTextViewText(R.id.tv_title_video_notification_music, cr_song.getSongName());
        expandedLayout.setTextViewText(R.id.tv_author_notification_music, cr_song.getAuthor());
        expandedLayout.setImageViewResource(R.id.im_play_notification_music, isPlaying ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_baseline_play_arrow_24);

        //action
        PendingIntent prevIntent = createIntentAction(this, ACTION_PRE);
        PendingIntent pauseIntent = createIntentAction(this, ACTION_PAUSE);
        PendingIntent resumeIntent = createIntentAction(this, ACTION_RESUME);
        PendingIntent nextIntent = createIntentAction(this, ACTION_NEXT);

        collapsedLayout.setOnClickPendingIntent(R.id.im_next_notification_music, nextIntent);

        expandedLayout.setOnClickPendingIntent(R.id.im_prev_notification_music, prevIntent);
        expandedLayout.setOnClickPendingIntent(R.id.im_next_notification_music, nextIntent);

        if (isPlaying) {
            collapsedLayout.setOnClickPendingIntent(R.id.im_play_notification_music, pauseIntent);
            collapsedLayout.setImageViewResource(R.id.im_play_notification_music, R.drawable.ic_baseline_pause_24);

            expandedLayout.setOnClickPendingIntent(R.id.im_play_notification_music, pauseIntent);
            expandedLayout.setImageViewResource(R.id.im_play_notification_music, R.drawable.ic_baseline_pause_24);
        } else {
            collapsedLayout.setOnClickPendingIntent(R.id.im_play_notification_music, resumeIntent);
            collapsedLayout.setImageViewResource(R.id.im_play_notification_music, R.drawable.ic_baseline_play_arrow_24);

            expandedLayout.setOnClickPendingIntent(R.id.im_play_notification_music, resumeIntent);
            expandedLayout.setImageViewResource(R.id.im_play_notification_music, R.drawable.ic_baseline_play_arrow_24);
        }

        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.noti);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContent(collapsedLayout)
                .setCustomContentView(collapsedLayout)
                .setCustomBigContentView(expandedLayout)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    private PendingIntent createIntentAction(@NonNull Context context, int action) {
        Intent actionIntent = new Intent(this, NotificationReceiver.class);
        actionIntent.putExtra("action_music", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        Log.i("BindService", "onBind");
//        Log.i("BindService", "Action: " + intent.getAction());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        Log.i("BindService", "onUnbind: " + super.onUnbind(intent));
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
//        Log.i("BindService", "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
//        Log.i("BindService", "onDestroy");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    public Song getSong() {
        return cr_song;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void handelActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                onPause();
                break;

            case ACTION_RESUME:
                onResume();
                break;

            case ACTION_STOP:
                stopSelf();
                break;

            case ACTION_PRE:
                onPre();
                break;

            case ACTION_NEXT:
                onNext();
                break;
        }
    }

    private void playSong(Song song) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, song.getMp3());
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(cr_song);
        }
    }

    public void onPause() {
        if (mediaPlayer != null & isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            cr_second = mediaPlayer.getCurrentPosition();
            sendNotification(cr_song);
        }
    }

    public void onResume() {
        if (mediaPlayer != null & !isPlaying) {
            mediaPlayer.seekTo(cr_second);
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(cr_song);
        }
    }

    public void onPre(){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (cr == 0) {
            cr = songs.size() - 1;
        } else {
            cr--;
        }
        cr_song = songs.get(cr);
        playSong(cr_song);
        sendNotification(cr_song);
    }

    public void onNext(){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (cr == songs.size() - 1) {
            cr = 0;
        } else {
            cr++;
        }
        cr_song = songs.get(cr);
        playSong(cr_song);
        sendNotification(cr_song);
    }
}
