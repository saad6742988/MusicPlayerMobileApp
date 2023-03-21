package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView musicRecyclerView;
    TextView notFoundtv;
    final String[] projection = {MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION};
    final String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
    ArrayList<MusicModel> musicList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        notFoundtv = findViewById(R.id.notFoundtv);
        if(!checkPermission())
        {
            requestPermission();
        }


        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while (cursor.moveToNext()){
            MusicModel music =  new MusicModel(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            if(new File(music.getPath()).exists() && music.getDuration()!=null)
                musicList.add(music);
        }
        if(musicList.size()==0)
        {
            notFoundtv.setVisibility(View.VISIBLE);
        }
        else
        {
            musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            musicAdapter musicAdapter = new musicAdapter(musicList,getApplicationContext());
            musicRecyclerView.setAdapter(musicAdapter);
        }
    }
    public Boolean checkPermission()
    {
        int storagePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(storagePermission == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
    public void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(this, "Please Allow Read Permission From Settings", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(musicRecyclerView!=null)
        {
            musicAdapter musicAdapter = new musicAdapter(musicList,getApplicationContext());
            musicRecyclerView.setAdapter(musicAdapter);
        }
    }
}