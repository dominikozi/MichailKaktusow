package com.example.mkaktusow.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mkaktusow.Controller.MapInfoWindowAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class Mapa extends AppCompatActivity {

    Kaktus kaktus;

    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //----bottom navigation bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.mapa_bottom_navigation);
        //set Miejsca selected
        bottomNavigationView.setSelectedItemId(R.id.Mapa);
        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Mapa:
                        return true;
                    case R.id.Encyklopedia:
                        startActivity(new Intent(getApplicationContext(),Encyklopedia.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Miejsca:
                        startActivity(new Intent(getApplicationContext(), Kaktusy.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Notatki:
                        startActivity(new Intent(getApplicationContext(),Notatki.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //!!!!!----bottom navigation bar

    //  Long idKaktusa = getIntent().getExtras().getLong("id_kaktusa");
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

   //   kaktus = db.kaktusDAO().getKaktusWithID(idKaktusa);
        List<Kaktus> kaktusy= db.kaktusDAO().getAllKaktusy();

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                for(Kaktus kakus : kaktusy){
                    LatLng latLng = new LatLng(kakus.getLatitude(),kakus.getLongtitude());
                    String snippet = kakus.getGatunek() + "!!" + kakus.getSciezkaDoZdjecia();

                    googleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(Mapa.this));

                    MarkerOptions options = new MarkerOptions().position(latLng).title(kakus.getNazwaKaktusa()).snippet(snippet);
                    //zoom camera
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    //add marker on map
                    googleMap.addMarker(options);



                }
            }
        });

    }


}