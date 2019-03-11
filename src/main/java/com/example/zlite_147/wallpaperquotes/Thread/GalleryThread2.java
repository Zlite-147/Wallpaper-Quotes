package com.example.zlite_147.wallpaperquotes.Thread;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zlite_147.wallpaperquotes.Common.Common;
import com.example.zlite_147.wallpaperquotes.DataBase.DataSource.RecentRepository;
import com.example.zlite_147.wallpaperquotes.DataBase.LocalDataBase.LocalDatabase;
import com.example.zlite_147.wallpaperquotes.DataBase.LocalDataBase.RecentsDataSource;
import com.example.zlite_147.wallpaperquotes.DataBase.Recents;
import com.example.zlite_147.wallpaperquotes.DownloadImages.SaveImageHelper;
import com.example.zlite_147.wallpaperquotes.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

public class GalleryThread2 extends AppCompatActivity {

    private ActionBar toolbar;
    ImageView img_product;
    private int PERMISSION_REQUEST_CODE=1000;

    //Room DB
    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;


    //For Set Wallpaper
    private Target target=new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_thread2);

        toolbar=getSupportActionBar();

        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(Common.STR_GALLERY_NAME);
    //    toolbar.setIcon(getDrawable(R.drawable.ic_arrow_back_black_24dp));

//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
//       toolbar.setN
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //What to do on back clicked
//            }
//        });

        //Init DB
        compositeDisposable=new CompositeDisposable();
        LocalDatabase database=LocalDatabase.getInstance(this);
        recentRepository=RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));

        addToRecents();

       img_product =(ImageView)findViewById(R.id.img_gallery_2_thread);
        PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(img_product);
        photoViewAttacher.update();

        try {
            checkPermission();
            addGalleryThread();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addToRecents() {

        Disposable disposable= (Disposable) Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                Recents recents=new Recents(Common.STR_GALLERYTHREAD_SELECTED,Common.CATEGORY_ID_SELECTED,
                        String.valueOf(System.currentTimeMillis()));
                recentRepository.insertRecents(recents);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR", throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        Picasso.with(getBaseContext()).cancelRequest(target);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() throws IOException {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
    }


    private void addGalleryThread() throws IOException{
        Picasso.with(getBaseContext())
                .load(Common.STR_GALLERYTHREAD_SELECTED)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(img_product);
        ImageView btn_downlaod=(ImageView)findViewById(R.id.download_view_gallery) ;
        ImageView btn_Share=(ImageView)findViewById(R.id.share_view_gallery);
        ImageView btn_wallpaper=(ImageView)findViewById(R.id.wallpaper_view_gallery);

        btn_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(getBaseContext())
                        .load(Common.STR_GALLERYTHREAD_SELECTED)
                        .into(target);
                Toast.makeText(GalleryThread2.this, "Wallpaper Set successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btn_downlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String filename = UUID.randomUUID().toString() + ".jpg";
                Picasso.with(getBaseContext())
                        .load(Common.STR_GALLERYTHREAD_SELECTED)
                        .into(new SaveImageHelper(getBaseContext(),
                                getBaseContext().getContentResolver(),
                                filename, "Image DEcs"));
            }
        });


        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareItem(Common.STR_GALLERYTHREAD_SELECTED);
                Toast.makeText(getBaseContext(), "Share....", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //Share Image Via Intent
    private void shareItem(String url) {

        Picasso.with(getBaseContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    //Image Save As Bitmap...
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
