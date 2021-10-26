package com.example.mkaktusow.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotatkaDAO {

    @Query("Select * FROM notatka")
    List<Notatka> getAllNotatki();

    @Insert
    void insertAll(Notatka... notatki);



}
