package com.example.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

public class MusicCompletionReceiver extends BroadcastReceiver {

    PlayActivity playActivity;

    public MusicCompletionReceiver(){
        //empty constructor
    }

    public MusicCompletionReceiver(PlayActivity playActivity) {
        this.playActivity= playActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String musicName= intent.getStringExtra(MusicService.MUSICNAME);
        int currentBitmapID = intent.getIntExtra("BitmapID", -1);
        if(musicName != null)
            playActivity.updateName(musicName);
        if(currentBitmapID != -1)
            playActivity.updateImage(currentBitmapID);
    }
}
