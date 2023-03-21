package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {

    TextView musicTitle,currentTime,totalTime;
    ImageView previous,pause_play,next,mainIcon;
    SeekBar seek_bar;
    ArrayList<MusicModel> musicList = new ArrayList<>();
    MusicModel currentMusic;
    MediaPlayer mediaPlayer = MyMediaPlayer.getMediaPlayer();
    int rotate=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        musicTitle=findViewById(R.id.musicTitle);
        currentTime=findViewById(R.id.currentTime);
        totalTime=findViewById(R.id.totalTime);
        previous=findViewById(R.id.previous);
        pause_play=findViewById(R.id.pause_play);
        next=findViewById(R.id.next);
        mainIcon=findViewById(R.id.mainIcon);
        seek_bar=findViewById(R.id.seek_bar);
        musicList = (ArrayList<MusicModel>) getIntent().getSerializableExtra("musicList");

        musicTitle.setSelected(true);
        setResources();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null)
                {
                    seek_bar.setProgress(mediaPlayer.getCurrentPosition());
                    mainIcon.setRotation(rotate+=2);
                    currentTime.setText(convertTime(mediaPlayer.getCurrentPosition()+""));
                    //check if song is compelete than automatically move to next song
                    if(convertTime(mediaPlayer.getCurrentPosition()+"").equals(convertTime(currentMusic.getDuration())))
                    {
                        playNext();
                    }
                    //change play and pause button
                    if(mediaPlayer.isPlaying())
                        pause_play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    else
                        pause_play.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                }
                new Handler().postDelayed(this,100);
            }
        });
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //if user changes the seek bar
                if(mediaPlayer!=null&&fromUser==true)
                {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setResources() {
        currentMusic = musicList.get(MyMediaPlayer.currentIndex);
        musicTitle.setText(currentMusic.getTitle());
        totalTime.setText(convertTime(currentMusic.getDuration()));
        pause_play.setOnClickListener(v->playPause());
        previous.setOnClickListener(v->playPrev());
        next.setOnClickListener(v->playNext());
        playMusic();
    }


    public void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentMusic.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seek_bar.setProgress(0);
            seek_bar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void playPause(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();

    }
    public void playNext(){
        if(MyMediaPlayer.currentIndex==musicList.size()-1) {
            MyMediaPlayer.currentIndex=0;
            mediaPlayer.reset();
            setResources();
        }
        else{
            MyMediaPlayer.currentIndex+=1;
            mediaPlayer.reset();
            setResources();
        }


    }
    public void playPrev(){
        //check if song is played less than 1 sec than move to prev
        //else move to start of that song
        if(mediaPlayer.getCurrentPosition()<1000)
        {
            if(MyMediaPlayer.currentIndex==0) {
                MyMediaPlayer.currentIndex=musicList.size()-1;
                mediaPlayer.reset();
                setResources();
            }
            else{
                MyMediaPlayer.currentIndex-=1;
                mediaPlayer.reset();
                setResources();
            }
        }
        else
        {
            mediaPlayer.seekTo(0);
            seek_bar.setProgress(0);
        }
    }

    public static String convertTime(String duration)
    {
        Log.d("convertTime", "convertTime: "+duration);

        Long ms =Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(ms)%TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(ms)%TimeUnit.MINUTES.toSeconds(1));
    }
}