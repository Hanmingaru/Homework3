package com.example.homework3;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class MusicPlayer implements MediaPlayer.OnCompletionListener  {
    MediaPlayer player;
    MediaPlayer sound1Player;
    MediaPlayer sound2Player;
    MediaPlayer sound3Player;
    MyAsyncTask asyncTask;


    boolean done;
    int currentPosition = 0;
    int musicIndex = 0;
    String songName = "";
    String sound1Name, sound2Name, sound3Name;
    int sb1Pos, sb2Pos, sb3Pos;
    int sound1CurrPos, sound2CurrPos, sound3CurrPos;
    private int musicStatus = 0;//0: before playing, 1 playing, 2 paused
    private MusicService musicService;
    int currentBitmapID = 0;
    static final int[] MUSICPATH = new int[]{
            R.raw.techtriumph,
            R.raw.victorymarch,
            R.raw.gotechgo
    };

    static final int[] SOUNDPATH = new int[]{
            R.raw.cheering,
            R.raw.clapping,
            R.raw.lestgohokies
    };

    static final String[] MUSICNAME = new String[]{
            "Tech Triumph",
            "Victory March",
            "Go Tech Go"
    };

    static final String[] SOUNDNAME = new String[]{
            "Cheering",
            "Clapping",
            "Let\'s Go Hokies"
    };

    static final int[] MUSICIMAGEPATH = new int[]{
            R.drawable.winning,
            R.drawable.marching,
            R.drawable.trumpet
    };
    static final int[] SOUNDIMAGEPATH = new int[]{
            R.drawable.cheering,
            R.drawable.clapping,
            R.drawable.vt
    };
    public MusicPlayer(MusicService service) {

        this.musicService = service;
        done = false;
    }

    public int getPathFromName(String songName){
        for(int i = 0; i < MUSICNAME.length; i++){
            if(songName.equals(MUSICNAME[i])){
                return i;
            }
        }
        return -1;
    }
    public int getIndexFromSound(String soundName){
        for(int i = 0; i < SOUNDNAME.length; i++){
            if(soundName.equals(SOUNDNAME[i])){
                return i;
            }
        }
        return -1;
    }
    public int getMusicStatus() {

        return musicStatus;
    }
    public String getMusicName() {

        return MUSICNAME[musicIndex];
    }

    public void setSoundPositions(int sound1, int sound2, int sound3){
        sb1Pos = sound1;
        sb2Pos = sound2;
        sb3Pos = sound3;
    }
    public void setSoundName(String s1, String s2, String s3){
        sound1Name = s1;
        sound2Name = s2;
        sound3Name = s3;
    }

    public void playMusic(String songName) {
        done = false;
        musicIndex = getPathFromName(songName);
        currentBitmapID = MUSICIMAGEPATH[musicIndex];
        player= MediaPlayer.create(this.musicService, MUSICPATH[musicIndex]);
        this.songName = songName;
        int index = getIndexFromSound(sound1Name);
        sound1Player = MediaPlayer.create(this.musicService, SOUNDPATH[index]);
        index = getIndexFromSound(sound2Name);
        sound2Player = MediaPlayer.create(this.musicService, SOUNDPATH[index]);
        index = getIndexFromSound(sound3Name);
        sound3Player = MediaPlayer.create(this.musicService, SOUNDPATH[index]);

        sound1Player.setOnCompletionListener(this);
        sound2Player.setOnCompletionListener(this);
        sound3Player.setOnCompletionListener(this);
        player.setOnCompletionListener(this);

        player.start();
        musicService.onUpdateMusicName(getMusicName());
        musicService.onUpdateImage(currentBitmapID);
        musicStatus = 1;
        startAsync();
    }
    public void stopMusic(){
        if(player != null){
            if(asyncTask!=null && asyncTask.getStatus()== AsyncTask.Status.RUNNING){
                asyncTask.cancel(true);
                asyncTask= null;
            }
            stopSound();
            musicStatus = 0;
            player.release();
            player = null;
        }
    }
    public void pauseMusic() {
        if(player!= null && player.isPlaying()){
            player.pause();
            currentPosition= player.getCurrentPosition();
            musicStatus= 2;
            pauseSound();
        }
    }
    public void resumeMusic() {
        if(player!= null){
            player.seekTo(currentPosition);
            player.start();
            resumeSound();
            musicStatus=1;
        }
    }
    public void restartMusic(String songName){
        if(player != null) {
            if(asyncTask!=null && asyncTask.getStatus()== AsyncTask.Status.RUNNING){
                asyncTask.cancel(true);
                asyncTask= null;
            }
            stopSound();
            player.release();
            player = null;
            playMusic(songName);
        }
    }

    public void playSound(int soundNumber){
        switch(soundNumber){
            case 1:
                currentBitmapID = SOUNDIMAGEPATH[0];
                sound1Player.start();
                break;
            case 2:
                currentBitmapID = SOUNDIMAGEPATH[1];
                sound2Player.start();
                break;
            case 3:
                currentBitmapID = SOUNDIMAGEPATH[2];
                sound3Player.start();
                break;
        }
        musicService.onUpdateImage(currentBitmapID);
    }
    private void stopSound(){
        if(sound1Player != null) {
            sound1Player.release();
            sound1Player = null;
        }
        if(sound2Player != null) {
            sound2Player.release();
            sound2Player = null;
        }
        if(sound3Player != null) {
            sound3Player.release();
            sound3Player = null;
        }
    }
    private void pauseSound(){
        if(sound1Player != null && sound1Player.isPlaying()){
            sound1Player.pause();
            sound1CurrPos = sound1Player.getCurrentPosition();
        }
        if(sound2Player != null && sound2Player.isPlaying()){
            sound2Player.pause();
            sound2CurrPos = sound2Player.getCurrentPosition();
        }
        if(sound3Player != null && sound3Player.isPlaying()){
            sound3Player.pause();
            sound3CurrPos = sound3Player.getCurrentPosition();
        }
    }
    private void resumeSound(){
        if(sound1Player!= null){
            sound1Player.seekTo(sound1CurrPos);
            sound1Player.start();
        }
        if(sound2Player!= null){
            sound2Player.seekTo(sound2CurrPos);
            sound2Player.start();
        }
        if(sound3Player!= null){
            sound3Player.seekTo(sound3CurrPos);
            sound3Player.start();
        }
    }
    private boolean isSound(){
        return (sound1Player.isPlaying() || sound2Player.isPlaying() || sound3Player.isPlaying());
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mediaPlayer.equals(player)) {
            stopSound();
            done = true;
        }
        if((mediaPlayer.equals(sound1Player) || mediaPlayer.equals(sound2Player) || mediaPlayer.equals(sound3Player)) && !isSound()){
            currentBitmapID = MUSICIMAGEPATH[getPathFromName(songName)];
            musicService.onUpdateImage(currentBitmapID);
        }
    }

    public void startAsync() {
        asyncTask = new MyAsyncTask();
        asyncTask.execute();
    }
    private class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            while(!done){
                if(player != null) {
                    if (player.getCurrentPosition() == (int)(player.getDuration() * (sb1Pos / 100.0))) {
                        playSound(1);
                    }
                    if(player.getCurrentPosition() == (int)(player.getDuration()*(sb2Pos/100.0))){
                        playSound(2);
                    }
                    if(player.getCurrentPosition() == (int)(player.getDuration()*(sb3Pos/100.0))){
                        playSound(3);
                    }
                }
            }
            return null;
        }

    }

}
