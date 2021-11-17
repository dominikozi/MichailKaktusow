package com.example.mkaktusow.View;
import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Notatka;
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
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class Notatki extends AppCompatActivity implements NotatkaAdapter.OnNotatkaListener {


    RecyclerView recyclerView;
    NotatkaAdapter adapter;

    List<Notatka> notatki;

    FloatingActionButton fabDuzy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatki);


     /*   notatki = new ArrayList<>();
        for( int i=0;i<100; i++){
            Notatka notatka = new Notatka("typ Notatki"+i*3, "nazwaNotatki #"+ i);
            notatki.add(notatka);
        }*/
        //TODO robić to w background threat a nie w main!
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        notatki = db.notatkaDAO().getAllNotatki();



        //recycler view
        recyclerView = findViewById(R.id.notatkiRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NotatkaAdapter(notatki, this);

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
                        startActivity(new Intent(getApplicationContext(), Kaktusy.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Mapa:
                        startActivity(new Intent(getApplicationContext(),Mapa.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //!!!!!----bottom navigation bar


        //fab
        fabDuzy=findViewById(R.id.notatki_floatingActionButtonDuzy);
        fabDuzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notatki.this, NowaNotatka.class);
                intent.putExtra("idkaktusap",-1L);
                intent.putExtra("liczbanotatek",adapter.getItemCount());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case 221:
                int id = item.getGroupId();
                displayMessage("Usunięto notatke o nazwie: "+notatki.get(id).getNazwaNotatki());
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                db.notatkaDAO().deleteByNotatkaId(notatki.get(id).getIdnotatka());
                adapter.removeNotatka(id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void displayMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotatkaClick(int position) {
        Intent intent = new Intent(this, JednaNotatka.class);
        Long tempId = notatki.get(position).getIdnotatka();

        intent.putExtra("id_notatki",tempId);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_sortowanie_n,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.om_n_sort_nazwa:
                Toast.makeText(getApplicationContext(),"Posortowano po nazwie",Toast.LENGTH_SHORT).show();
                break;
            case R.id.om_n_sort_data:
                Toast.makeText(getApplicationContext(),"Posortowano po dacie",Toast.LENGTH_SHORT).show();
                break;

        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }
}