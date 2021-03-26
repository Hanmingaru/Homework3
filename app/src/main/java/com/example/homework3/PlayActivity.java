package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity{
    TextView musicName;
    ImageView musicImage;
    Button playpauseButton;
    Button restartButton;
    String songName;
    String sound1Name, sound2Name, sound3Name;
    int sbPos1, sbPos2, sbPos3;
    private int currentBitmapID;
    MusicService musicService;
    MusicCompletionReceiver musicCompletionReceiver;
    Intent startMusicServiceIntent;
    boolean isBound = false;
    boolean isInitialized = false;
    boolean hasChanged = false;
    public static final String INITIALIZE_STATUS = "intialization status";
    public static final String MUSIC_PLAYING = "music playing";

    static final int[] MUSICIMAGEPATH = new int[]{
            R.drawable.winning,
            R.drawable.marching,
            R.drawable.trumpet
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        musicName = (TextView) findViewById(R.id.musicName);
        musicImage = (ImageView) findViewById(R.id.musicImage);

        playpauseButton= (Button) findViewById(R.id.playpauseButton);
        restartButton= (Button) findViewById(R.id.restartButton);

        Bundle b1 = getIntent().getExtras();
        songName = b1.getString("SongName");
        sound1Name = b1.getString("Sound1Name");
        sound2Name = b1.getString("Sound2Name");
        sound3Name = b1.getString("Sound3Name");
        sbPos1 = b1.getInt("SeekBarPosition1");
        sbPos2 = b1.getInt("SeekBarPosition2");
        sbPos3 = b1.getInt("SeekBarPosition3");
        hasChanged = b1.getBoolean("HasChanged");
        if(songName.equals("Tech Triumph")){
            currentBitmapID = MUSICIMAGEPATH[0];
        }
        else if(songName.equals("Victory March")){
            currentBitmapID = MUSICIMAGEPATH[1];
        }
        else if(songName.equals("Go Tech Go")){
            currentBitmapID = MUSICIMAGEPATH[2];
        }
        if(currentBitmapID != 0){
            updateImage(currentBitmapID);
        }
        musicName.setText(songName);
        if(savedInstanceState != null){
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS);
        }
        startMusicServiceIntent= new Intent(this, MusicService.class);
        if(!isInitialized){
            startService(startMusicServiceIntent);
            isInitialized= true;
        }

        musicCompletionReceiver = new MusicCompletionReceiver(this);
        if(musicService != null){
            musicService.startAsync();
        }
    }

    public void playpause(View view) {
        if (isBound) {
            musicService.setSoundPositions(sbPos1,sbPos2,sbPos3);
            musicService.setSoundNames(sound1Name, sound2Name, sound3Name);
            if(hasChanged){
                musicService.stopMusic();
                musicService.startMusic(songName);
                playpauseButton.setText("Pause");
                hasChanged = false;
            }
            else {
                switch (musicService.getPlayingStatus()) {
                    case 1:
                        musicService.pauseMusic();
                        playpauseButton.setText("Resume");
                        break;
                    case 2:
                        musicService.resumeMusic();
                        playpauseButton.setText("Pause");
                        break;
                }
            }
        }
    }
    public void restart(View view){
        if (isBound){
            musicService.restartMusic(songName);
        }
    }

    public void updateName(String s) {
        musicName.setText(s);
    }
    public void updateImage(int bitmapID){
        Bitmap temp = BitmapFactory.decodeResource(getResources(), bitmapID);
        musicImage.setImageBitmap(temp);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isInitialized && !isBound){
            bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
        }

        registerReceiver(musicCompletionReceiver, new IntentFilter(MusicService.COMPLETE_INTENT));
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(isBound){
            unbindService(musicServiceConnection);
            isBound= false;
        }

        unregisterReceiver(musicCompletionReceiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        outState.putString(MUSIC_PLAYING, musicName.getText().toString());
        outState.putInt("BitmapID", currentBitmapID);
    }
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        currentBitmapID = inState.getInt("BitmapID");
        musicImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),currentBitmapID));
        musicName.setText(inState.getString(MUSIC_PLAYING));
        Toast.makeText(this, "Bitmap ID is " + currentBitmapID, Toast.LENGTH_SHORT).show();

    }

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
            musicService = binder.getService();
            isBound = true;
            if(hasChanged){
                playpauseButton.setText("Start");
            }
            else {
                switch (musicService.getPlayingStatus()) {
                    case 1:
                        playpauseButton.setText("Pause");
                        break;
                    case 2:
                        playpauseButton.setText("Resume");
                        break;
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
            isBound = false;
        }
    };
}