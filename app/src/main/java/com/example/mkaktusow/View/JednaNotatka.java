package com.example.mkaktusow.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class JednaNotatka extends AppCompatActivity {

    TextView typ;
    TextView nazwaKaktusa;

    ImageView zdjecie;
    VideoView film;
    LinearLayout filmlinearwrapper;
    TextInputEditText tresc;
    ImageButton imageButton;

    Notatka notatka;
    Kaktus kaktus;

    FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jedna_notatka);

        typ=findViewById(R.id.jednanotatka_typ);
        nazwaKaktusa=findViewById(R.id.jednanotatka_nazwakaktusa);
        fabSave=findViewById(R.id.notatki_fab_save);
        Long idNotatki = getIntent().getExtras().getLong("id_notatki");

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        notatka = db.notatkaDAO().getNotatkaWithID(idNotatki);
        kaktus = db.kaktusDAO().getKaktusWithID(notatka.getKaktusid());
        getSupportActionBar().setTitle(notatka.getNazwaNotatki());

        typ.setText("Typ: "+notatka.getTypNotatki());

        nazwaKaktusa.setText("Nazwa kaktusa: " + kaktus.getNazwaKaktusa());

        zdjecie=findViewById(R.id.jednanotatka_zdjecie);
        film = findViewById(R.id.jednanotatka_video_view);
        filmlinearwrapper = findViewById(R.id.jednanotatka_film_linear_wrapper);
        tresc=findViewById(R.id.jednanotatka_tresc_notatki);
        imageButton = findViewById(R.id.jednanotatka_odtworz_audio);

        zdjecie.setVisibility(View.GONE);
        if(notatka.getTypNotatki().equals("zdjecie")) {
            // Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(holder.previewZdjNotatki);
            zdjecie.setVisibility(View.VISIBLE);
            Picasso.get().load(notatka.getSciezkaDoZdjecia()).fit().centerInside().into(zdjecie);
           // zdjecie.setImageURI(Uri.parse(notatka.getSciezkaDoZdjecia()));
        }
        if(notatka.getTypNotatki().equals("tekstowa")||notatka.getTypNotatki().equals("zdjecie")){
            tresc.setVisibility(View.VISIBLE);
            tresc.setText(notatka.getTrescNotatki());
        }else{
            tresc.setVisibility(View.GONE);
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

        //fab save
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(notatka.getTypNotatki().equals("tekstowa")||notatka.getTypNotatki().equals("zdjecie")) {
                    db.notatkaDAO().updateNotatka(tresc.getText().toString(),notatka.getIdnotatka());

                    Toast.makeText(getApplicationContext(),"Zmieniono tresc notatki "+notatka.getNazwaNotatki(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Nie mozesz edytowac notatki.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_notatka,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        switch(item.getItemId()){
            case R.id.om_n_zapisz:

                if(notatka.getTrescNotatki().length()!=0) {
                    db.notatkaDAO().updateNotatka(tresc.getText().toString(), notatka.getIdnotatka());

                    Toast.makeText(getApplicationContext(), "Zmieniono tresc notatki " + notatka.getNazwaNotatki(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Nie mozesz edytowac notatki.", Toast.LENGTH_SHORT).show();

                }

                return true;
            case R.id.om_n_usun:

                AlertDialog.Builder alertPotwierdzenie = new AlertDialog.Builder(this);
                alertPotwierdzenie.setCancelable(true);
                alertPotwierdzenie.setTitle("Usunięcie notatki");
                alertPotwierdzenie.setMessage("Czy potwierdzasz usunięcie notatki?");
                alertPotwierdzenie.setPositiveButton("Potwierdz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.notatkaDAO().deleteByNotatkaId(notatka.getIdnotatka());
                        Toast.makeText(getApplicationContext(),"Usunieto notatke "+notatka.getNazwaNotatki()+".",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Notatki.class);

                        startActivity(intent);

                    }
                });
                alertPotwierdzenie.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Usuniecie notatki anulowane.",Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog alertDialog = alertPotwierdzenie.create();
                alertDialog.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}