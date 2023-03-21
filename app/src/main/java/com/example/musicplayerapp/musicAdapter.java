package com.example.musicplayerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class musicAdapter extends RecyclerView.Adapter<musicAdapter.ViewHolder>{
    ArrayList<MusicModel> musicList;
    Context context;

    public musicAdapter(ArrayList<MusicModel> musicList, Context context) {
        this.musicList = musicList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicModel music= musicList.get(position);
        holder.musicTitle.setText(music.getTitle());
        if(MyMediaPlayer.currentIndex==position)
        {
            holder.musicTitle.setTextColor(Color.parseColor("#FFFF0000"));
        }
        else {
            holder.musicTitle.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getMediaPlayer().reset();
                MyMediaPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent=new Intent(context,MusicPlayer.class);
                intent.putExtra("musicList",musicList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView musicTitle;
        ImageView musicIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicTitle = itemView.findViewById(R.id.musicTitle);
            musicIcon = itemView.findViewById(R.id.musicIcon);
        }
    }
}
