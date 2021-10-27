package com.example.mkaktusow.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NowyKaktus extends AppCompatActivity {

    EditText nazwaKaktusa;
    EditText gatunek;
    EditText nazwaMiejsca;
    Button buttonDodajKaktusa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowy_kaktus);

        nazwaKaktusa=findViewById(R.id.nowykaktus_textinputedittext_1_nazwakaktusa);
        gatunek=findViewById(R.id.nowykaktus_textinputedittext_2_gatunek);
        nazwaMiejsca=findViewById(R.id.nowykaktus_textinputedittext_3_miejsce);
        buttonDodajKaktusa=findViewById(R.id.nowykaktus_button_dodajkaktus);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        buttonDodajKaktusa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save to datebase
                //Log.d("StworzKaktus", "onClick: Nazwa:" + nazwa.getText().toString()+ "onClick: typNotatki:" + typNotatki.getText().toString());

                db.kaktusDAO().insertAll(new Kaktus(nazwaKaktusa.getText().toString(), gatunek.getText().toString(), nazwaMiejsca.getText().toString()));
                startActivity(new Intent(NowyKaktus.this,Kaktusy.class));

            }
        });



        //String nazwaKaktusa, String gatunek, String nazwaMiejsca
    }
}