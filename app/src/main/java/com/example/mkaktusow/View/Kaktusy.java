package com.example.mkaktusow.View;
import com.example.mkaktusow.Controller.KaktusAdapter;
import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class Kaktusy extends AppCompatActivity implements KaktusAdapter.OnKaktusListener {

    Button buttonDodajKaktusa;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Kaktus> kaktusy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miejsca);

        //TODO robić to w background treat a nie w main!
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        kaktusy = db.kaktusDAO().getAllKaktusy();

        //recycler view
        recyclerView = findViewById(R.id.kaktusyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new KaktusAdapter(kaktusy, this);

        recyclerView.setAdapter(adapter);

        //----bottom navigation bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set Miejsca selected
        bottomNavigationView.setSelectedItemId(R.id.Miejsca);
        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Miejsca:
                        return true;
                    case R.id.Notatki:
                        startActivity(new Intent(getApplicationContext(),Notatki.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Encyklopedia:
                        startActivity(new Intent(getApplicationContext(),Encyklopedia.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //----bottom navigation bar


        //Button dodaj kaktusa
        buttonDodajKaktusa=findViewById(R.id.kaktusy_button_dodajKaktusa);
        buttonDodajKaktusa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Kaktusy.this, NowyKaktus.class));
            }
        });

    }

    @Override
    public void onKaktusClick(int position) {
        Intent intent = new Intent(this, JedenKaktus.class);
        //intent.putExtra("wybrany_kaktus", kaktusy.get(position) );

        Long tempId = kaktusy.get(position).getIdkaktus();
        intent.putExtra("id_kaktusa", tempId);

        startActivity(intent);
    }
}