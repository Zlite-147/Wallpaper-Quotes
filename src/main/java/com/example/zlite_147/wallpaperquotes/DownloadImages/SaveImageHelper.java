package com.example.zlite_147.wallpaperquotes.DownloadImages;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {

    public Context context;
    private  WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;

    public SaveImageHelper(Context context, ContentResolver contentResolver, String name, String desc) {
        this.context = context;
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        ContentResolver resolver=contentResolverWeakReference.get();
        // AlertDialog dialog=alertDialogWeakReference.get();

        if(resolver !=null)
            MediaStore.Images.Media.insertImage(resolver,bitmap,name,desc);

        Toast.makeText(context, "Download Successfully...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}


