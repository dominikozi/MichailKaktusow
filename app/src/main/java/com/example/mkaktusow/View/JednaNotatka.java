package com.example.mkaktusow.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

public class JednaNotatka extends AppCompatActivity {

    TextView typ;
    TextView nazwa;
    TextView nazwaKaktusa;

    ImageView zdjecie;

    Notatka notatka;
    Kaktus kaktus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jedna_notatka);

        typ=findViewById(R.id.jednanotatka_typ);
        nazwa=findViewById(R.id.jednanotatka_nazwa);
        nazwaKaktusa=findViewById(R.id.jednanotatka_nazwakaktusa);

        Long idNotatki = getIntent().getExtras().getLong("id_notatki");

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        notatka = db.notatkaDAO().getNotatkaWithID(idNotatki);
        kaktus = db.kaktusDAO().getKaktusWithID(notatka.getKaktusid());
        getSupportActionBar().setTitle("Notatka: " + notatka.getNazwaNotatki());
        nazwa.setText(notatka.getNazwaNotatki());

        typ.setText("Typ: "+notatka.getTypNotatki());

        nazwaKaktusa.setText("Nazwa kaktusa: " + kaktus.getNazwaKaktusa());

        zdjecie=findViewById(R.id.jednanotatka_zdjecie);
        zdjecie.setVisibility(View.GONE);
        if(notatka.getTypNotatki().equals("zdjecie")){
            zdjecie.setVisibility(View.VISIBLE);
            zdjecie.setImageURI(Uri.parse(notatka.getSciezkaDoZdjecia()));
        }


    }
}