package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    Button play;

    Spinner musicSpinner;
    Spinner sound1Spinner;
    Spinner sound2Spinner;
    Spinner sound3Spinner;

    SeekBar sb1;
    SeekBar sb2;
    SeekBar sb3;

    String songName;
    String sound1Name, sound2Name, sound3Name;
    int sbPos1, sbPos2, sbPos3;
    boolean hasChanged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.playButton);
        ArrayAdapter<CharSequence> musicAdapter = ArrayAdapter.createFromResource(this,
                R.array.music_array, android.R.layout.simple_spinner_item);
        musicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> soundAdapter = ArrayAdapter.createFromResource(this,
                R.array.sound_array, android.R.layout.simple_spinner_item);
        soundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        musicSpinner = (Spinner) findViewById(R.id.musicSpinner);
        musicSpinner.setAdapter(musicAdapter);

        sound1Spinner = (Spinner) findViewById(R.id.sound1Spinner);
        sound1Spinner.setAdapter(soundAdapter);
        sound2Spinner = (Spinner) findViewById(R.id.sound2Spinner);
        sound2Spinner.setAdapter(soundAdapter);
        sound3Spinner = (Spinner) findViewById(R.id.sound3Spinner);
        sound3Spinner.setAdapter(soundAdapter);

        sb1 = (SeekBar) findViewById(R.id.seekBar1);
        sb2 = (SeekBar) findViewById(R.id.seekBar2);
        sb3 = (SeekBar) findViewById(R.id.seekBar3);

        musicSpinner.setOnItemSelectedListener(this);
        sound1Spinner.setOnItemSelectedListener(this);
        sound2Spinner.setOnItemSelectedListener(this);
        sound3Spinner.setOnItemSelectedListener(this);

        sb1.setOnSeekBarChangeListener(this);
        sb2.setOnSeekBarChangeListener(this);
        sb3.setOnSeekBarChangeListener(this);
        songName = "";
        sound1Name = "";
        sound2Name = "";
        sound3Name = "";
        hasChanged = false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == musicSpinner.getId()) {
            if(!songName.equals(parent.getItemAtPosition(position).toString())) {
                hasChanged= true;
            }
            songName = parent.getItemAtPosition(position).toString();

        }
        if(parent.getId() == sound1Spinner.getId()){
            if(!sound1Name.equals(parent.getItemAtPosition(position).toString())) {
                hasChanged= true;
            }
            sound1Name = parent.getItemAtPosition(position).toString();
        }
        if(parent.getId() == sound2Spinner.getId()){
            if(!sound2Name.equals(parent.getItemAtPosition(position).toString())) {
                hasChanged= true;
            }
            sound2Name = parent.getItemAtPosition(position).toString();
        }
        if(parent.getId() == sound3Spinner.getId()){
            if(!sound3Name.equals(parent.getItemAtPosition(position).toString())) {
                hasChanged= true;
            }
            sound3Name = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void playMusic(View view){
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("SongName", songName);
        intent.putExtra("Sound1Name", sound1Name);
        intent.putExtra("Sound2Name", sound2Name);
        intent.putExtra("Sound3Name", sound3Name);

        intent.putExtra("SeekBarPosition1", sbPos1);
        intent.putExtra("SeekBarPosition2", sbPos2);
        intent.putExtra("SeekBarPosition3", sbPos3);
        intent.putExtra("HasChanged", hasChanged);
        hasChanged = false;
        startActivity(intent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId() == sb1.getId()){
            sbPos1 = progress;
        }
        if(seekBar.getId() == sb2.getId()){
            sbPos2 = progress;
        }
        if(seekBar.getId() == sb3.getId()){
            sbPos3 = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}