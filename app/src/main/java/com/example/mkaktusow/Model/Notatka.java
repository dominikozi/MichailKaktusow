package com.example.mkaktusow.Model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


   /*     (foreignKeys = @ForeignKey(entity=Kaktus.class,
        parentColumns = "idkaktus",
        childColumns="kaktusid"))*/
@Entity
public class Notatka {

    @PrimaryKey(autoGenerate = true)
    private long idnotatka;

    @ColumnInfo(name="typ_notatki")
    private String typNotatki; //0-tekstowa, 1-audio, 2-zdjecie, 3-film
    @ColumnInfo(name="nazwa_notatki")
    private String nazwaNotatki;
    @ColumnInfo(name="sciezka_zdjecie")
    private String sciezkaDoZdjecia;
    @ColumnInfo(name="sciezka_audio")
    private String sciezkaDoAudio;
    @ColumnInfo(name="data_dodania")
    private String dataDodania;

 //   private long kaktusid;

    public Notatka(){

    }

    public Notatka(String typNotatki, String nazwaNotatki) {
        this.typNotatki = typNotatki;
        this.nazwaNotatki=nazwaNotatki;
    }

    public Notatka(String typNotatki, String nazwaNotatki, String sciezkaDoZdjecia, String sciezkaDoAudio, String dataDodania, long kaktusId) {
        this.nazwaNotatki=nazwaNotatki;
        this.typNotatki = typNotatki;
        this.sciezkaDoZdjecia = sciezkaDoZdjecia;
        this.sciezkaDoAudio = sciezkaDoAudio;
        this.dataDodania = dataDodania;
  //      this.kaktusid = kaktusId;
    }

    public long getIdnotatka() {
        return idnotatka;
    }

    public void setIdnotatka(long idnotatka) {
        this.idnotatka = idnotatka;
    }

/*    public long getKaktusid() {
        return kaktusid;
    }

    public void setKaktusid(long kaktusid) {
        this.kaktusid = kaktusid;
    }
*/
    public String getTypNotatki() {
        return typNotatki;
    }

    public void setTypNotatki(String typNotatki) {
        this.typNotatki = typNotatki;
    }

    public String getSciezkaDoZdjecia() {
        return sciezkaDoZdjecia;
    }

    public void setSciezkaDoZdjecia(String sciezkaDoZdjecia) {
        this.sciezkaDoZdjecia = sciezkaDoZdjecia;
    }

    public String getSciezkaDoAudio() {
        return sciezkaDoAudio;
    }

    public void setSciezkaDoAudio(String sciezkaDoAudio) {
        this.sciezkaDoAudio = sciezkaDoAudio;
    }

    public String getDataDodania() {
        return dataDodania;
    }

    public void setDataDodania(String dataDodania) {
        this.dataDodania = dataDodania;
    }

 /*   public long getKaktusId() {
        return kaktusid;
    }

    public void setKaktusId(long kaktusId) {
        this.kaktusid = kaktusId;
    }
*/
    public String getNazwaNotatki() {
        return nazwaNotatki;
    }

    public void setNazwaNotatki(String nazwaNotatki) {
        this.nazwaNotatki = nazwaNotatki;
    }
}
