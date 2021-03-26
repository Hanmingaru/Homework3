package com.example.homework3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    MusicPlayer musicPlayer;
    private final IBinder iBinder= new MyBinder();

    public static final String COMPLETE_INTENT = "complete intent";
    public static final String MUSICNAME = "music name";

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MusicPlayer(this);
    }

    public void setSoundPositions(int sound1, int sound2, int sound3){
        musicPlayer.setSoundPositions(sound1, sound2,sound3);
    }
    public void setSoundNames(String sound1Name, String sound2Name, String sound3Name){
        musicPlayer.setSoundName(sound1Name,sound2Name, sound3Name);
    }

    public void startMusic(String songName){

        musicPlayer.playMusic(songName);
    }
    public void stopMusic(){musicPlayer.stopMusic();}
    public void pauseMusic(){

        musicPlayer.pauseMusic();
    }
    public void resumeMusic(){

        musicPlayer.resumeMusic();
    }
    public void restartMusic(String songName){
        musicPlayer.restartMusic(songName);
    }

    public int getPlayingStatus(){

        return musicPlayer.getMusicStatus();
    }
    public void startAsync(){
        musicPlayer.startAsync();
    }
    public void onUpdateMusicName(String musicname) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra(MUSICNAME, musicname);
        sendBroadcast(intent);
    }
    public void onUpdateImage(int bitmap){
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra("BitmapID", bitmap);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }


    public class MyBinder extends Binder {

        MusicService getService(){

            return MusicService.this;
        }
    }
}