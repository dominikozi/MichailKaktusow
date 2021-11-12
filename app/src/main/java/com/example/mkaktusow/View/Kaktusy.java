package com.example.mkaktusow.View;
import com.example.mkaktusow.Controller.KaktusAdapter;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;

import com.example.mkaktusow.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import java.util.List;

public class Kaktusy extends AppCompatActivity implements KaktusAdapter.OnKaktusListener {

    RecyclerView recyclerView;
    KaktusAdapter adapter;
    List<Kaktus> kaktusy;

    FloatingActionButton fabDuzy;
    FloatingActionButton fabMalyZdj;
    FloatingActionButton fabMalyBezZdj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miejsca);

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

        //fab
        fabDuzy = findViewById(R.id.kaktusy_floatingActionButtonDuzy);
        fabMalyZdj = findViewById(R.id.kaktusy_floatingActionButtonZdjecie);
        fabMalyBezZdj = findViewById(R.id.kaktusy_floatingActionButtonBezZdjecia);

        fabDuzy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fabMalyZdj.getVisibility()!=View.VISIBLE) {
                    fabMalyZdj.setVisibility(View.VISIBLE);
                    fabMalyBezZdj.setVisibility(View.VISIBLE);
                }else{
                    fabMalyZdj.setVisibility(View.GONE);
                    fabMalyBezZdj.setVisibility(View.GONE);
                }
            }
        });

        fabMalyZdj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"nowy kaktus ze zdj", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Kaktusy.this, NowyKaktus.class);
                intent.putExtra("czyZdj", 1);

                startActivity(intent);

            }
        });
        fabMalyBezZdj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Kaktusy.this, NowyKaktus.class));
            }
        });
    }

    @Override
    public void onKaktusClick(int position) {
        Intent intent = new Intent(this, JedenKaktusTL.class);
        //intent.putExtra("wybrany_kaktus", kaktusy.get(position) );

        Long tempId = kaktusy.get(position).getIdkaktus();
        intent.putExtra("id_kaktusa", tempId);

        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case 121:
                int id = item.getGroupId();
                displayMessage("Usunięto kaktus o nazwie: "+kaktusy.get(id).getNazwaKaktusa());
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                db.kaktusDAO().deleteByKaktusId(kaktusy.get(id).getIdkaktus());
                adapter.removeItem(id);
                return true;
            case 122:
                displayMessage("edytuj");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void displayMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_sortowanie_k,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.om_k_sort_nazwa:
                Toast.makeText(getApplicationContext(),"Posortowano po nazwie",Toast.LENGTH_SHORT).show();
                break;
            case R.id.om_k_sort_data:
                Toast.makeText(getApplicationContext(),"Posortowano po dacie",Toast.LENGTH_SHORT).show();
                break;
            case R.id.om_k_sort_gatunek:
                Toast.makeText(getApplicationContext(),"Posortowano po gatunku",Toast.LENGTH_SHORT).show();
                break;
        }



        return super.onOptionsItemSelected(item);
    }
}