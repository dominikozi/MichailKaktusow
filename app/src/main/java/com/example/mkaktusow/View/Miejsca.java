package com.example.mkaktusow.View;
import com.example.mkaktusow.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class Miejsca extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miejsca);


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

        //
    }
}