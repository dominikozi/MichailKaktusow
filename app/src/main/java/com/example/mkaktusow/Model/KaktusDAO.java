package com.example.mkaktusow.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface KaktusDAO {

    @Query("Select * FROM kaktus")
    List<Kaktus> getAllKaktusy();

    @Query("Select * FROM kaktus WHERE idkaktus=:id")
    Kaktus getKaktusWithID(Long id);

    @Query("Select data_podlania FROM kaktus WHERE idkaktus=:id")
    Date getDataPWithID(Long id);

    @Query("Select data_zakwitu FROM kaktus WHERE idkaktus=:id")
    Date getDataKWithID(Long id);

    @Insert
    void insertAll(Kaktus... kaktusy);

    @Query("DELETE FROM kaktus WHERE idkaktus = :id")
    abstract void deleteByKaktusId(long id);

    @Query("UPDATE kaktus SET data_podlania=:datap WHERE idkaktus = :id")
    void updateDataOstPodlania(Date datap, Long id);

    @Query("UPDATE kaktus SET data_zakwitu=:datak WHERE idkaktus = :id")
    void updateDataOstKwitniecia(Date datak, Long id);

    @Query("UPDATE kaktus SET nazwa_kaktusa=:nazwak, nazwa_miejsca=:nazwam WHERE idkaktus = :id")
    void updateNazwy(String nazwak, String nazwam, Long id);


}
