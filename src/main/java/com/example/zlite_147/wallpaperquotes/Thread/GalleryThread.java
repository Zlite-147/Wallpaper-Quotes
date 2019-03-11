package com.example.zlite_147.wallpaperquotes.Thread;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zlite_147.wallpaperquotes.Adapter.GalleryViewHolder;
import com.example.zlite_147.wallpaperquotes.Common.Common;
import com.example.zlite_147.wallpaperquotes.Fragments.ImagesFragment;
import com.example.zlite_147.wallpaperquotes.InterFace.IItemClickListener;
import com.example.zlite_147.wallpaperquotes.MainActivity;
import com.example.zlite_147.wallpaperquotes.Model.Gallery;
import com.example.zlite_147.wallpaperquotes.Model.GalleryCard;
import com.example.zlite_147.wallpaperquotes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class GalleryThread extends AppCompatActivity {


    private ActionBar toolbar;
    RecyclerView recyclerView;

    FirebaseRecyclerOptions<GalleryCard> options;
    FirebaseRecyclerAdapter<GalleryCard,GalleryThreadViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference category;
    Query query;

    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_thread);

        toolbar=getSupportActionBar();
        toolbar.setTitle(Common.STR_GALLERY_NAME);

        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_gallery_thread);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);

        try {
            addGalleryThread();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addGalleryThread() throws IOException {
        query = FirebaseDatabase.getInstance().getReference(Common.STR_GALLERYTHREAD)
                .orderByChild("categoryId").equalTo(Common.CATEGORY_ID_SELECTED);


        options = new FirebaseRecyclerOptions.Builder<GalleryCard>()
                .setQuery(query, GalleryCard.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<GalleryCard, GalleryThreadViewHolder>(options) {

            @NonNull
            @Override
            public GalleryThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.gallery_menu_card, parent, false);
                return new GalleryThreadViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull GalleryThreadViewHolder holder, final int position, @NonNull final GalleryCard model) {

                Picasso.with(context)
                        .load(model.getImagelink())
                        .into(holder.imageView);

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.STR_GALLERYTHREAD_SELECTED=model.getImagelink();
                        startActivity(new Intent(getBaseContext(),GalleryThread2.class));
                    }
                });
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        if (adapter!=null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.startListening();
    }

    public static class GalleryThreadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       public IItemClickListener iItemClickListener;
       public ImageView imageView;
        public GalleryThreadViewHolder(View itemView) {
            super(itemView);
           imageView =(ImageView)itemView.findViewById(R.id.image_gallery);
        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onClick(view);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GalleryThread.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}

