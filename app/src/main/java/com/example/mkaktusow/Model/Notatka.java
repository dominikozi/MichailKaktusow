package com.example.mkaktusow.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity=Kaktus.class,
        parentColumns = "idkaktus",
        childColumns="kaktusid",
        onDelete = ForeignKey.CASCADE))
public class Notatka {

    @PrimaryKey(autoGenerate = true)
    private long idnotatka;

    @ColumnInfo(name="typ_notatki")
    private String typNotatki; //0-tekstowa, 1-audio, 2-zdjecie, 3-film
    @ColumnInfo(name="nazwa_notatki")
    private String nazwaNotatki;
    @ColumnInfo(name="tresc_notatki")
    private String trescNotatki;
    @ColumnInfo(name="sciezka_zdjecie")
    private String sciezkaDoZdjecia;
    @ColumnInfo(name="sciezka_audio")
    private String sciezkaDoAudio;
    @ColumnInfo(name="sciezka_film")
    private String sciezkaDoFilmu;
    @ColumnInfo(name="data_dodania")
    private Date dataDodania;

    private long kaktusid;



    public Notatka(String typNotatki, String nazwaNotatki, String trescNotatki, String sciezkaDoZdjecia, String sciezkaDoAudio, String sciezkaDoFilmu, Date dataDodania, long kaktusid) {
        this.typNotatki = typNotatki;
        this.nazwaNotatki = nazwaNotatki;
        this.trescNotatki = trescNotatki;
        this.sciezkaDoZdjecia = sciezkaDoZdjecia;
        this.sciezkaDoAudio = sciezkaDoAudio;
        this.sciezkaDoFilmu = sciezkaDoFilmu;
        this.dataDodania = dataDodania;
        this.kaktusid = kaktusid;
    }

    public long getIdnotatka() {
        return idnotatka;
    }

    public void setIdnotatka(long idnotatka) {
        this.idnotatka = idnotatka;
    }

    public String getTrescNotatki() {
        return trescNotatki;
    }

    public void setTrescNotatki(String trescNotatki) {
        this.trescNotatki = trescNotatki;
    }

    public String getSciezkaDoFilmu() {
        return sciezkaDoFilmu;
    }

    public void setSciezkaDoFilmu(String sciezkaDoFilmu) {
        this.sciezkaDoFilmu = sciezkaDoFilmu;
    }

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

    public long getKaktusid() {
        return kaktusid;
    }

    public void setKaktusid(long kaktusid) {
        this.kaktusid = kaktusid;
    }

    public String getSciezkaDoAudio() {
        return sciezkaDoAudio;
    }

    public void setSciezkaDoAudio(String sciezkaDoAudio) {
        this.sciezkaDoAudio = sciezkaDoAudio;
    }

    public Date getDataDodania() {
        return dataDodania;
    }

    public void setDataDodania(Date dataDodania) {
        this.dataDodania = dataDodania;
    }


    public String getNazwaNotatki() {
        return nazwaNotatki;
    }

    public void setNazwaNotatki(String nazwaNotatki) {
        this.nazwaNotatki = nazwaNotatki;
    }


    public static Comparator<Notatka> NotatkaNazwaAZComparaotr = new Comparator<Notatka>() {
        @Override
        public int compare(Notatka n1, Notatka n2) {
            return n1.getNazwaNotatki().compareTo(n2.getNazwaNotatki());
        }
    };
    public static Comparator<Notatka> NotatkaNazwaZAComparaotr = new Comparator<Notatka>() {
        @Override
        public int compare(Notatka n1, Notatka n2) {
            return n2.getNazwaNotatki().compareTo(n1.getNazwaNotatki());
        }
    };
    public static Comparator<Notatka> NotatkaDataRosnacoComparaotr = new Comparator<Notatka>() {
        @Override
        public int compare(Notatka n1, Notatka n2) {
            return (int)n1.getDataDodania().getTime() - (int) n2.getDataDodania().getTime();
        }
    };
    public static Comparator<Notatka> NotatkaDataMalejacoComparaotr = new Comparator<Notatka>() {
        @Override
        public int compare(Notatka n1, Notatka n2) {
            return (int)n2.getDataDodania().getTime() - (int) n1.getDataDodania().getTime();
        }
    };
    public static Comparator<Notatka> NotatkaTypAZComparaotr = new Comparator<Notatka>() {
        @Override
        public int compare(Notatka n1, Notatka n2) {
            return n1.getTypNotatki().compareTo(n2.getTypNotatki());
        }
    };
    public static Comparator<Notatka> NotatkaTypZAComparaotr = new Comparator<Notatka>() {
        @Override
        public int compare(Notatka n1, Notatka n2) {
            return n2.getTypNotatki().compareTo(n1.getTypNotatki());
        }
    };

}
