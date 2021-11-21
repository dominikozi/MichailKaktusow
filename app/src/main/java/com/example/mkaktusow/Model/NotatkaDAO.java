package com.example.mkaktusow.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotatkaDAO {

    @Query("Select * FROM notatka")
    List<Notatka> getAllNotatki();

    @Query("Select * FROM notatka WHERE kaktusid=:id")
    List<Notatka> getAllNotatkiWithID(Long id);

    @Query("Select * FROM notatka WHERE idnotatka=:id")
    Notatka getNotatkaWithID(Long id);

    @Insert
    void insertAll(Notatka... notatki);

    @Query("DELETE FROM notatka WHERE idnotatka = :id")
    void deleteByNotatkaId(long id);

    @Query("UPDATE notatka SET tresc_notatki=:tresc WHERE idnotatka = :id")
    void updateNotatka(String tresc, Long id);

    @Query("UPDATE notatka SET nazwa_notatki=:nazwa WHERE idnotatka = :id")
    void updateNazwaNotatki(String nazwa, Long id);

}
