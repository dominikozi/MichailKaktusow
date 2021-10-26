package com.example.mkaktusow.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Kaktus {

    @PrimaryKey(autoGenerate = true)
    private long idKaktus;

    private String nazwaKaktusa;
    private String gatunek;
    private String dataOstPodlania;
    private String dataOstZakwitu;
    private String nazwaMiejsca;
    private String lokalizacja;

    public Kaktus(long idKaktus, String nazwaKaktusa, String gatunek, String dataOstPodlania, String dataOstZakwitu, String nazwaMiejsca, String lokalizacja) {
        this.idKaktus = idKaktus;
        this.nazwaKaktusa = nazwaKaktusa;
        this.gatunek = gatunek;
        this.dataOstPodlania = dataOstPodlania;
        this.dataOstZakwitu = dataOstZakwitu;
        this.nazwaMiejsca = nazwaMiejsca;
        this.lokalizacja = lokalizacja;
    }

    public Kaktus() {
    }

    public long getIdKaktus() {
        return idKaktus;
    }

    public void setIdKaktus(long idKaktus) {
        this.idKaktus = idKaktus;
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
}
