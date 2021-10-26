package com.example.mkaktusow.View;
import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Notatki extends AppCompatActivity {

    Button buttonDodajNotatke;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;


    ArrayList<String> notatki;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatki);


        notatki = new ArrayList<>();
        for( int i=0;i<100; i++){
            notatki.add("Notatka # "+ i);
        }
        //recycler view
        recyclerView = findViewById(R.id.notatkiRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NotatkaAdapter(notatki);

        recyclerView.setAdapter(adapter);








        //----bottom navigation bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set Miejsca selected
        bottomNavigationView.setSelectedItemId(R.id.Notatki);
        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Notatki:
                        return true;
                    case R.id.Encyklopedia:
                        startActivity(new Intent(getApplicationContext(),Encyklopedia.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Miejsca:
                        startActivity(new Intent(getApplicationContext(),Miejsca.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //!!!!!----bottom navigation bar

        //Button dodaj notatke
        buttonDodajNotatke=findViewById(R.id.notatki_button_dodajNotatke);
        buttonDodajNotatke.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notatki.this, NowaNotatka.class));
            }
        });






        //
    }
}