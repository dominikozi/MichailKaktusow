package com.example.mkaktusow.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

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
    @ColumnInfo(name="latitude")
    private Double latitude;
    @ColumnInfo(name="longtitude")
    private Double longtitude;
    @ColumnInfo(name="data_dodania")
    private Date dataDodania;
    @ColumnInfo(name="data_podlania")
    private Date dataOstPodlania;
    @ColumnInfo(name="data_zakwitu")
    private Date dataOstZakwitu;



    public Kaktus(String nazwaKaktusa, String gatunek, String nazwaMiejsca, String sciezkaDoZdjecia, Double latitude, Double longtitude, Date dataDodania) {
        this.nazwaKaktusa = nazwaKaktusa;
        this.gatunek = gatunek;
        this.nazwaMiejsca = nazwaMiejsca;
        this.sciezkaDoZdjecia=sciezkaDoZdjecia;
        this.latitude=latitude;
        this.longtitude=longtitude;
        this.dataDodania=dataDodania;
    }

//    public Kaktus(String nazwaKaktusa, String gatunek, String nazwaMiejsca, Double latitude, Double longtitude, Date dataDodania) {
//        this.nazwaKaktusa = nazwaKaktusa;
//        this.gatunek = gatunek;
//        this.nazwaMiejsca = nazwaMiejsca;
//        this.latitude=latitude;
//        this.longtitude=longtitude;
//    }

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

    public Date getDataDodania() {
        return dataDodania;
    }

    public void setDataDodania(Date dataDodania) {
        this.dataDodania = dataDodania;
    }

    public Date getDataOstPodlania() {
        return dataOstPodlania;
    }

    public void setDataOstPodlania(Date dataOstPodlania) {
        this.dataOstPodlania = dataOstPodlania;
    }

    public Date getDataOstZakwitu() {
        return dataOstZakwitu;
    }

    public void setDataOstZakwitu(Date dataOstZakwitu) {
        this.dataOstZakwitu = dataOstZakwitu;
    }

    public String getSciezkaDoZdjecia() {
        return sciezkaDoZdjecia;
    }

    public void setSciezkaDoZdjecia(String sciezkaDoZdjecia) {
        this.sciezkaDoZdjecia = sciezkaDoZdjecia;
    }



    public String getNazwaMiejsca() {
        return nazwaMiejsca;
    }

    public void setNazwaMiejsca(String nazwaMiejsca) {
        this.nazwaMiejsca = nazwaMiejsca;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString(){
        return getNazwaKaktusa();
    }

}
