package com.example.mymusic.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mymusic.Fragment_1;
import com.example.mymusic.IFragment1;
import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.utils.BindService;

public class NotificationReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        int actionmusic =intent.getIntExtra("action_music",0);
        //Toast.makeText(context,actionmusic+"",Toast.LENGTH_SHORT).show();
        Intent intentService = new Intent(context, BindService.class);
        intentService.putExtra("action_music_service",actionmusic);

//        Intent intentServiceActivity = new Intent(context, MainActivity.class);
//        intentServiceActivity.putExtra("action_music_service_activity",actionmusic);
//        context.startActivity(intentServiceActivity);

        context.startService(intentService);
    }
}













