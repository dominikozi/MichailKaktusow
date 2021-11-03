package com.example.mkaktusow.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

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

        nazwa.setText(kaktus.getNazwaKaktusa());
        gatunek.setText(kaktus.getGatunek());
        miejsce.setText(kaktus.getNazwaMiejsca());
        imageViewZdjecieKaktusa.setImageURI(Uri.parse(kaktus.getSciezkaDoZdjecia()));

        recyclerViewNotatki.setLayoutManager(new LinearLayoutManager(this));
        notatki = db.notatkaDAO().getAllNotatkiWithID(kaktus.getIdkaktus());
        adapter=new NotatkaAdapter(notatki, this);

        recyclerViewNotatki.setAdapter(adapter);


    }

    @Override
    public void onNotatkaClick(int position) {
        Intent intent = new Intent(this, JednaNotatka.class);
        Long tempId = notatki.get(position).getIdnotatka();

        intent.putExtra("id_notatki",tempId);

        startActivity(intent);
    }
}