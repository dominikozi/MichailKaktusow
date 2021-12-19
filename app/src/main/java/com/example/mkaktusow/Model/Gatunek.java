package com.example.mkaktusow.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Gatunek {

    @PrimaryKey(autoGenerate = true)
    private long idgatunek;
    @ColumnInfo(name="nazwa_gatunku")
    private String nazwaGatunku;
    @ColumnInfo(name="czas_podlanie")
    private int czasNaPodlanie;

    public Gatunek(String nazwaGatunku, int czasNaPodlanie) {
        this.nazwaGatunku = nazwaGatunku;
        this.czasNaPodlanie = czasNaPodlanie;
    }

    public long getIdgatunek() {
        return idgatunek;
    }

    public void setIdgatunek(long idgatunek) {
        this.idgatunek = idgatunek;
    }

    public String getNazwaGatunku() {
        return nazwaGatunku;
    }

    public void setNazwaGatunku(String nazwaGatunku) {
        this.nazwaGatunku = nazwaGatunku;
    }

    public int getCzasNaPodlanie() {
        return czasNaPodlanie;
    }

    public void setCzasNaPodlanie(int czasNaPodlanie) {
        this.czasNaPodlanie = czasNaPodlanie;
    }

    @Override
    public String toString(){
        return nazwaGatunku;
    }
}
