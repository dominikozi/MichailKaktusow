package com.example.mkaktusow.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GatunekDAO {

    @Query("Select * FROM gatunek")
    List<Gatunek> getAllGatunki();

    @Query("Select * FROM gatunek WHERE idgatunek=:id")
    Gatunek getGatunek(long id);

    @Query("Select idgatunek FROM gatunek WHERE nazwa_gatunku=:name")
    long getIDGatunekFromName(String name);

    @Query("DELETE FROM gatunek WHERE idgatunek = :id")
    abstract void deleteByGatunekId(long id);

    @Query("DELETE FROM gatunek WHERE nazwa_gatunku = :name")
    abstract void deleteByGatunekName(String name);


    @Query("DELETE FROM gatunek")
    void nukeGatunek();

    @Insert
    void insertAll(Gatunek... gatunki);

}
