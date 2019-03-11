package com.example.zlite_147.wallpaperquotes.DataBase.LocalDataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.zlite_147.wallpaperquotes.DataBase.Recents;

import java.util.List;

import io.reactivex.Flowable;
@Dao
public interface RecentsDAO {
    @Query("SELECT *FROM recents ORDER BY saveTime DESC LIMIT 10")
    Flowable<List<Recents>> getAllRecents();

    @Insert
    void insertRecents(Recents... recents);

    @Update
    void updateRecents(Recents... recents);

    @Delete
    void deleteRecents(Recents... recents);

    @Query("DELETE FROM recents")
    void deleteAllRecents();

}
