package com.example.zlite_147.wallpaperquotes.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zlite_147.wallpaperquotes.R;

import com.example.zlite_147.wallpaperquotes.InterFace.IItemClickListener;

public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    IItemClickListener iItemClickListener;
    public ImageView image_gallery;
    public TextView txt_gallery,txt_number;

    public GalleryViewHolder(View itemView) {
        super(itemView);
        image_gallery=(ImageView)itemView.findViewById(R.id.image_gallery);
        txt_gallery=(TextView)itemView.findViewById(R.id.menu_name_gallery_menu_card);
      //  txt_number=(TextView)itemView.findViewById(R.id.id_gallery_menu_card);
    }

    @Override
    public void onClick(View view) {
        iItemClickListener.onClick(view);
    }
}
