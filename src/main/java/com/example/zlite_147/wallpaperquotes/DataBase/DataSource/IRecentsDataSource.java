package com.example.zlite_147.wallpaperquotes.DataBase.DataSource;

import com.example.zlite_147.wallpaperquotes.DataBase.Recents;

import java.util.List;

import io.reactivex.Flowable;

public interface IRecentsDataSource {

    Flowable<List<Recents>> getAllRecents();

    void insertRecents(Recents... recents);
    void updateRecents(Recents... recents);
    void deleteRecents(Recents... recents);
    void deleteAllRecents();

}
