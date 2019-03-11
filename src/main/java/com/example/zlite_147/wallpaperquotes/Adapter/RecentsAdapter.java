package com.example.zlite_147.wallpaperquotes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zlite_147.wallpaperquotes.Common.Common;
import com.example.zlite_147.wallpaperquotes.DataBase.Recents;
import com.example.zlite_147.wallpaperquotes.R;
import com.example.zlite_147.wallpaperquotes.Thread.GalleryThread;
import com.example.zlite_147.wallpaperquotes.Thread.GalleryThread2;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentsAdapter extends RecyclerView.Adapter<GalleryThread.GalleryThreadViewHolder> {

    List<Recents> recentsList;
    Context context;


    public RecentsAdapter(Context context, List<Recents> recentsList) {
        this.recentsList = recentsList;
        this.context = context;
    }

    @NonNull
    @Override
    public GalleryThread.GalleryThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_menu_card, parent, false);
        return new GalleryThread.GalleryThreadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryThread.GalleryThreadViewHolder holder, final int position) {

        Picasso.with(context)
                .load(recentsList.get(position).getImageLink())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.STR_GALLERYTHREAD_SELECTED=recentsList.get(position).getImageLink();
                context.startActivity(new Intent(context,GalleryThread2.class));
            }
        });
    }
    @Override
    public int getItemCount() {
        return recentsList.size();
    }
}



