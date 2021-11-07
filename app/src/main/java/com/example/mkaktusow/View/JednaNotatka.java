package com.example.mkaktusow.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import java.io.IOException;

public class JednaNotatka extends AppCompatActivity {

    TextView typ;
    TextView nazwa;
    TextView nazwaKaktusa;

    ImageView zdjecie;
    VideoView film;
    LinearLayout filmlinearwrapper;
    TextView tresc;
    ImageButton imageButton;

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
        film = findViewById(R.id.jednanotatka_video_view);
        filmlinearwrapper = findViewById(R.id.jednanotatka_film_linear_wrapper);
        tresc=findViewById(R.id.jednanotatka_tresc_notatki);
        imageButton = findViewById(R.id.jednanotatka_odtworz_audio);

        zdjecie.setVisibility(View.GONE);
        if(notatka.getTypNotatki().equals("zdjecie")){
            zdjecie.setVisibility(View.VISIBLE);
            zdjecie.setImageURI(Uri.parse(notatka.getSciezkaDoZdjecia()));
        }
        if(notatka.getTrescNotatki().length()==0){
            tresc.setVisibility(View.GONE);
        }else{
            tresc.setVisibility(View.VISIBLE);
            tresc.setText(notatka.getTrescNotatki());
        }

        if(notatka.getSciezkaDoAudio()==null){
            imageButton.setVisibility(View.GONE);
        }else {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(notatka.getSciezkaDoAudio());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        if(notatka.getSciezkaDoFilmu()==null){
            film.setVisibility(View.GONE);
        }else{
            film.setVisibility(View.VISIBLE);
            film.setVideoURI(Uri.parse(notatka.getSciezkaDoFilmu()));
         /*   film.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                   film.start();
                   return true;
                }
            });*/
            film.seekTo(1);
            filmlinearwrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(film.isPlaying()) {
                        film.pause();
                    }
                    else {
                        film.start();
                    }
                }
            });

        }

    }

}