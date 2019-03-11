package com.example.zlite_147.wallpaperquotes.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zlite_147.wallpaperquotes.Adapter.RecentsAdapter;
import com.example.zlite_147.wallpaperquotes.DataBase.DataSource.RecentRepository;
import com.example.zlite_147.wallpaperquotes.DataBase.LocalDataBase.LocalDatabase;
import com.example.zlite_147.wallpaperquotes.DataBase.LocalDataBase.RecentsDataSource;
import com.example.zlite_147.wallpaperquotes.DataBase.Recents;
import com.example.zlite_147.wallpaperquotes.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentsFragment extends Fragment {

    RecyclerView recyclerView;

    Context context;

    List<Recents> recentsList;

    RecentsAdapter adapter;

    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    private static RecentsFragment INSTANCE=null;

    public RecentsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context){
        this.context=context;

        //Init DB
        compositeDisposable =new CompositeDisposable();
        LocalDatabase database=LocalDatabase.getInstance(context);
        recentRepository=RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));

    }

    private static RecentsFragment getInstance(){
        if(INSTANCE==null)
            INSTANCE=new RecentsFragment();
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_recents, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_fragment_recents);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recentsList=new ArrayList<>();
        adapter=new RecentsAdapter(context,recentsList);
        recyclerView.setAdapter(adapter);
        //loadRecents();
        return view;
    }

    private void loadRecents() {

        Disposable disposable=recentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {
                        onGetALLRecents(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR",throwable.getMessage());
                    }
                });
    }

    private void onGetALLRecents(List<Recents> recents) {

        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();
    }

}
