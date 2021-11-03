package com.example.mkaktusow.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Kaktus {

    @PrimaryKey(autoGenerate = true)
    private long idkaktus;

    @ColumnInfo(name="nazwa_kaktusa")
    private String nazwaKaktusa;
    @ColumnInfo(name="gatunek")
    private String gatunek;
    @ColumnInfo(name="sciezkadozdjecia")
    private String sciezkaDoZdjecia;
    @ColumnInfo(name="nazwa_miejsca")
    private String nazwaMiejsca;

    @ColumnInfo(name="lokalizacja")
    private String lokalizacja;
    @ColumnInfo(name="data_podlania")
    private String dataOstPodlania;
    @ColumnInfo(name="data_zakwitu")
    private String dataOstZakwitu;




    public Kaktus(String nazwaKaktusa, String gatunek, String nazwaMiejsca, String sciezkaDoZdjecia) {
        this.nazwaKaktusa = nazwaKaktusa;
        this.gatunek = gatunek;
        this.nazwaMiejsca = nazwaMiejsca;
        this.sciezkaDoZdjecia=sciezkaDoZdjecia;
    }

    public Kaktus(String nazwaKaktusa, String gatunek, String nazwaMiejsca) {
        this.nazwaKaktusa = nazwaKaktusa;
        this.gatunek = gatunek;
        this.nazwaMiejsca = nazwaMiejsca;
    }

    public Kaktus(String nazwaKaktusa, String gatunek, String dataOstPodlania, String dataOstZakwitu, String nazwaMiejsca, String lokalizacja) {
        this.nazwaKaktusa = nazwaKaktusa;
        this.gatunek = gatunek;
        this.dataOstPodlania = dataOstPodlania;
        this.dataOstZakwitu = dataOstZakwitu;
        this.nazwaMiejsca = nazwaMiejsca;
        this.lokalizacja = lokalizacja;
    }

    public Kaktus() {
    }


    public long getIdkaktus() {
        return idkaktus;
    }

    public void setIdkaktus(long idkaktus) {
        this.idkaktus = idkaktus;
    }

    public String getNazwaKaktusa() {
        return nazwaKaktusa;
    }

    public void setNazwaKaktusa(String nazwaKaktusa) {
        this.nazwaKaktusa = nazwaKaktusa;
    }

    public String getGatunek() {
        return gatunek;
    }

    public void setGatunek(String gatunek) {
        this.gatunek = gatunek;
    }

    public String getDataOstPodlania() {
        return dataOstPodlania;
    }

    public void setDataOstPodlania(String dataOstPodlania) {
        this.dataOstPodlania = dataOstPodlania;
    }

    public String getSciezkaDoZdjecia() {
        return sciezkaDoZdjecia;
    }

    public void setSciezkaDoZdjecia(String sciezkaDoZdjecia) {
        this.sciezkaDoZdjecia = sciezkaDoZdjecia;
    }

    public String getDataOstZakwitu() {
        return dataOstZakwitu;
    }

    public void setDataOstZakwitu(String dataOstZakwitu) {
        this.dataOstZakwitu = dataOstZakwitu;
    }

    public String getNazwaMiejsca() {
        return nazwaMiejsca;
    }

    public void setNazwaMiejsca(String nazwaMiejsca) {
        this.nazwaMiejsca = nazwaMiejsca;
    }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public void setLokalizacja(String lokalizacja) {
        this.lokalizacja = lokalizacja;
    }

    @Override
    public String toString(){
        return getNazwaKaktusa();
    }

}
