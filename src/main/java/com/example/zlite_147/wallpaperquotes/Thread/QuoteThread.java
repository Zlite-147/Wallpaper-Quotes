package com.example.zlite_147.wallpaperquotes.Thread;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zlite_147.wallpaperquotes.Common.Common;
import com.example.zlite_147.wallpaperquotes.Fragments.QuotesFragment;
import com.example.zlite_147.wallpaperquotes.InterFace.IItemClickListener;
import com.example.zlite_147.wallpaperquotes.MainActivity;
import com.example.zlite_147.wallpaperquotes.Model.GalleryCard;
import com.example.zlite_147.wallpaperquotes.Model.QuotesCard;
import com.example.zlite_147.wallpaperquotes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;

public class QuoteThread extends AppCompatActivity {

    private ActionBar toolbar;

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<QuotesCard> options;
    FirebaseRecyclerAdapter<QuotesCard,QuoteThreadViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference category;
    Query query;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_thread);

        toolbar=getSupportActionBar();

        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(Common.STR_QUOTES_NAME);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_quote_thread);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);


        try {
            addQuotes();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
        Intent intent = new Intent(QuoteThread.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void addQuotes() throws IOException{

        query = FirebaseDatabase.getInstance().getReference(Common.STR_QUOTESTHREAD)
                .orderByChild("categoryId").equalTo(Common.CATEGORY_ID_SELECTED);


        options = new FirebaseRecyclerOptions.Builder<QuotesCard>()
                .setQuery(query, QuotesCard.class)
                .build();
        adapter=new FirebaseRecyclerAdapter<QuotesCard,QuoteThreadViewHolder>(options){

            @NonNull
            @Override
            public QuoteThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.quotes_view_menu_card, parent, false);
                return new QuoteThreadViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull QuoteThreadViewHolder holder, int position, @NonNull final QuotesCard model) {
                holder.txt_Quote.setText(model.getQuotes());

                  Common.STR_QUOTES_NAME=model.getColor();
               // Toast.makeText(getBaseContext(), ""+Common.STR_QUOTES_NAME, Toast.LENGTH_SHORT).show();
               //   holder.txt_Quote.setBackgroundColor(Color.parseColor(Common.STR_QUOTES_NAME));
//                holder.linearLayout.setBackgroundColor(Color.parseColor("000000"));

                holder.button_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(getBaseContext().CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text",model.getQuotes());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getBaseContext(), "Copied !!!", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.button_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getBaseContext(), "Added To Favourite !!!", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.button_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String shareBody = "Here is the share content body";
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quotes");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,model.getQuotes());
                        startActivity(Intent.createChooser(sharingIntent,"Share Via"));
                        Toast.makeText(getBaseContext(), "Tap To Share !!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private class QuoteThreadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        IItemClickListener iItemClickListener;
        TextView txt_Quote;
        ImageButton button_copy,button_share,button_fav;
        LinearLayout linearLayout;

        public QuoteThreadViewHolder(View itemView) {
            super(itemView);
            button_copy=(ImageButton)itemView.findViewById(R.id.btn_copy);
            button_fav=(ImageButton)itemView.findViewById(R.id.btn_fav);
            button_share=(ImageButton)itemView.findViewById(R.id.btn_share);
            txt_Quote=(TextView)itemView.findViewById(R.id.txt_quote) ;
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linear_bg);
        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onClick(view);
        }
    }
}
