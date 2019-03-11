package com.example.zlite_147.wallpaperquotes.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.zlite_147.wallpaperquotes.Adapter.GalleryViewHolder;
import com.example.zlite_147.wallpaperquotes.Common.Common;
import com.example.zlite_147.wallpaperquotes.Model.Gallery;
import com.example.zlite_147.wallpaperquotes.R;
import com.example.zlite_147.wallpaperquotes.Thread.GalleryThread;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import android.os.Handler;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ImagesFragment extends Fragment {

    RecyclerView gallery;

    Context context;
// Firebase Auth...
    FirebaseRecyclerOptions<Gallery> options;
    FirebaseRecyclerAdapter<Gallery,GalleryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference category;

    ProgressBar progressBar;
    Handler handler = new Handler();

    ShimmerLayout shimmerLayout;

    private SwipeRefreshLayout swipeContainer;



    public ImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_images, container, false);
//        progressBar = view.findViewById(R.id.progressBar);
        shimmerLayout=(ShimmerLayout)view.findViewById(R.id.shimmerLayout);

        gallery=(RecyclerView)view.findViewById(R.id.recyclerView_fragment_images);
        gallery.setLayoutManager(new GridLayoutManager(context,2));
        gallery.setHasFixedSize(true);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.setVisibility(View.GONE);
//                try {
//                    addGallery();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        },3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerLayout.startShimmerAnimation();
//                try {
//                    addGallery();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        },5000);

        try {
            addGallery();
            } catch (IOException e) {
                e.printStackTrace();
            }
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        try {
                            addGallery();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        swipeContainer.setRefreshing(false);
                    }
                }, 4000);

            }

        });

        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_light),
                getResources().getColor(android.R.color.holo_purple),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

        return view;
    }

    private void addGallery()  throws IOException{
        shimmerLayout.startShimmerAnimation();
        database=FirebaseDatabase.getInstance();
        category=database.getReference(Common.STR_GALLERY);

        options=new FirebaseRecyclerOptions.Builder<Gallery>()
                .setQuery(category,Gallery.class)
                .build();

        adapter= new FirebaseRecyclerAdapter<Gallery, GalleryViewHolder>(options) {
            @NonNull
            @Override
            public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_menu_card_first,null);
                return new GalleryViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final GalleryViewHolder holder, final int position, @NonNull final Gallery model) {

                Picasso.with(getContext())
                        .load(model.getimagelink())
                        .into(holder.image_gallery);

                holder.txt_gallery.setText(model.getname());
//                holder.txt_number.setText(model.getnumber());
                holder.image_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.STR_GALLERY_NAME=model.getname();
                        Common.CATEGORY_ID_SELECTED=adapter.getRef(position).getKey();
                        startActivity(new Intent(getContext(), GalleryThread.class));
                    }
                });
            }
        };
        gallery.setAdapter(adapter);
        adapter.startListening();
        shimmerLayout.stopShimmerAnimation();
        shimmerLayout.setVisibility(View.GONE);

    }

}
