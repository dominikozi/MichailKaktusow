package com.example.mkaktusow.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class JedenKaktus extends AppCompatActivity implements NotatkaAdapter.OnNotatkaListener {

    TextView nazwa;
    TextView gatunek;
    TextView miejsce;
    ImageView imageViewZdjecieKaktusa;
    RecyclerView recyclerViewNotatki;
    RecyclerView.Adapter adapter;

    Kaktus kaktus;

    List<Notatka> notatki;

    Button buttonMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeden_kaktus);

        nazwa = findViewById(R.id.jedenkaktus_textview);
        gatunek=findViewById(R.id.jedenkaktus_textview2);
        miejsce=findViewById(R.id.jedenkaktus_textview3);
        recyclerViewNotatki = findViewById(R.id.jedenkaktus_recyclerview);
        imageViewZdjecieKaktusa = findViewById(R.id.jedenkaktus_imageview);
/*
        if(getIntent().hasExtra("id_kakusa")) {
            idKaktusa = getIntent().getExtras().getLong("id_kaktusa");
        }
*/
        Long idKaktusa = getIntent().getExtras().getLong("id_kaktusa");
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        kaktus = db.kaktusDAO().getKaktusWithID(idKaktusa);

        getSupportActionBar().setTitle("Kaktus: " + kaktus.getNazwaKaktusa());
        nazwa.setText(kaktus.getNazwaKaktusa());
        gatunek.setText(kaktus.getGatunek());
        miejsce.setText(kaktus.getNazwaMiejsca());

        if(kaktus.getSciezkaDoZdjecia()!=null) {
            imageViewZdjecieKaktusa.setImageURI(Uri.parse(kaktus.getSciezkaDoZdjecia()));
        }
        recyclerViewNotatki.setLayoutManager(new LinearLayoutManager(this));
        notatki = db.notatkaDAO().getAllNotatkiWithID(kaktus.getIdkaktus());
        adapter=new NotatkaAdapter(notatki, this);

        recyclerViewNotatki.setAdapter(adapter);

        buttonMapa = findViewById(R.id.jedenkaktus_button_mapa);
        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(JedenKaktus.this, Mapa.class);

                Long tempId = kaktus.getIdkaktus();
                intent.putExtra("id_kaktusa", tempId);

                startActivity(intent);
                */

                Intent intent = new Intent(JedenKaktus.this, JedenKaktusTL.class);
                Long tempId = kaktus.getIdkaktus();
                intent.putExtra("id_kaktusa", tempId);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onNotatkaClick(int position) {
        Intent intent = new Intent(this, JednaNotatka.class);
        Long tempId = notatki.get(position).getIdnotatka();

        intent.putExtra("id_notatki",tempId);

        startActivity(intent);
    }



}