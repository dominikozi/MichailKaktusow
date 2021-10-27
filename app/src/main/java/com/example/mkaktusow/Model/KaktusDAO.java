package com.example.mkaktusow.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface KaktusDAO {

    @Query("Select * FROM kaktus")
    List<Kaktus> getAllKaktusy();

    @Insert
    void insertAll(Kaktus... kaktusy);

}
