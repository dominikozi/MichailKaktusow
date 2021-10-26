package com.example.mkaktusow.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity=Kaktus.class,
        parentColumns = "idKaktus",
        childColumns="kaktusId"))

public class Notatka {

    @PrimaryKey(autoGenerate = true)
    private long idNotatka;

    private int typNotatki; //0-tekstowa, 1-audio, 2-zdjecie, 3-film
    private String sciezkaDoZdjecia;
    private String sciezkaDoAudio;
    private String dataDodania;

    private long kaktusId;

    public Notatka(){

    }

    public Notatka(long idNotatka, int typNotatki, String sciezkaDoZdjecia, String sciezkaDoAudio, String dataDodania, long kaktusId) {
        this.idNotatka = idNotatka;
        this.typNotatki = typNotatki;
        this.sciezkaDoZdjecia = sciezkaDoZdjecia;
        this.sciezkaDoAudio = sciezkaDoAudio;
        this.dataDodania = dataDodania;
        this.kaktusId = kaktusId;
    }

    public long getIdNotatka() {
        return idNotatka;
    }

    public void setIdNotatka(long idNotatka) {
        this.idNotatka = idNotatka;
    }

    public int getTypNotatki() {
        return typNotatki;
    }

    public void setTypNotatki(int typNotatki) {
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

    public long getKaktusId() {
        return kaktusId;
    }

    public void setKaktusId(long kaktusId) {
        this.kaktusId = kaktusId;
    }
}
