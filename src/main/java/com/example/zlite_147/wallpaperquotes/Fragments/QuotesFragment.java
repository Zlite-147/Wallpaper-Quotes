package com.example.zlite_147.wallpaperquotes.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zlite_147.wallpaperquotes.Adapter.GalleryViewHolder;
import com.example.zlite_147.wallpaperquotes.Common.Common;
import com.example.zlite_147.wallpaperquotes.InterFace.IItemClickListener;
import com.example.zlite_147.wallpaperquotes.Model.Gallery;
import com.example.zlite_147.wallpaperquotes.Model.Quotes;
import com.example.zlite_147.wallpaperquotes.R;
import com.example.zlite_147.wallpaperquotes.Thread.GalleryThread;
import com.example.zlite_147.wallpaperquotes.Thread.QuoteThread;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static android.graphics.Color.RED;

public class QuotesFragment extends Fragment {

    RecyclerView recyclerView;

    Context context;
    // Firebase Auth...
    FirebaseRecyclerOptions<Quotes> options;
    FirebaseRecyclerAdapter<Quotes,QuotesViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference category;


    ProgressBar progressBar;
    Handler handler = new Handler();
    private SwipeRefreshLayout swipeContainer;


    public QuotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_quotes, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_fragment_quotes);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setHasFixedSize(true);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                try {
                    addGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        },3000);
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




//
//        try {
//            addGallery();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return view;
    }

    private void addGallery()  throws IOException{

        // database=FirebaseDatabase.getInstance();
        database=FirebaseDatabase.getInstance();
        category=database.getReference(Common.STR_QUOTES);

        options=new FirebaseRecyclerOptions.Builder<Quotes>()
                .setQuery(category,Quotes.class)
                .build();

        adapter= new FirebaseRecyclerAdapter<Quotes, QuotesViewHolder>(options) {
            @NonNull
            @Override
            public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.quotes_menu_card,null);
                return new QuotesViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final QuotesViewHolder holder, final int position, @NonNull final Quotes model) {

                holder.textView.setText(model.getName());
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.STR_QUOTES_NAME=model.getName();
                        Common.CATEGORY_ID_SELECTED=adapter.getRef(position).getKey();
                        startActivity(new Intent(getContext(), QuoteThread.class));
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    private class QuotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        IItemClickListener iItemClickListener;
        TextView textView;
        public QuotesViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.txt_menu_name);
        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onClick(view);
        }
    }
}
