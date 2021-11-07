package com.example.mkaktusow.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.os.Bundle;

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

public class Mapa extends AppCompatActivity {

    Kaktus kaktus;

    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Long idKaktusa = getIntent().getExtras().getLong("id_kaktusa");
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        kaktus = db.kaktusDAO().getKaktusWithID(idKaktusa);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng latLng = new LatLng(kaktus.getLatitude(),kaktus.getLongtitude());
                //create marker options
                MarkerOptions options = new MarkerOptions().position(latLng).title("Kaktus: " + kaktus.getNazwaKaktusa());
                //zoom camera
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                //add marker on map
                googleMap.addMarker(options);
            }
        });

    }


}