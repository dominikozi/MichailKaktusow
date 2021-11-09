package com.example.mkaktusow.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface KaktusDAO {

    @Query("Select * FROM kaktus")
    List<Kaktus> getAllKaktusy();

    @Query("Select * FROM kaktus WHERE idkaktus=:id")
    Kaktus getKaktusWithID(Long id);

    @Insert
    void insertAll(Kaktus... kaktusy);

    @Query("DELETE FROM kaktus WHERE idkaktus = :id")
    abstract void deleteByKaktusId(long id);

}
