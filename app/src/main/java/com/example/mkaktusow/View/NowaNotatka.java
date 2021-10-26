package com.example.mkaktusow.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NowaNotatka extends AppCompatActivity {

    private static final String TAG = "StworzNotatke";

    EditText nazwa;
    EditText typNotatki;
    Button buttonDodajNotatke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowa_notatka);


        nazwa=findViewById(R.id.nowanotatka_textinputedittext_1_nazwanotatki);
        typNotatki=findViewById(R.id.nowanotatka_textinputedittext_2_typnotatki);
        buttonDodajNotatke=findViewById(R.id.nowanotatka_button_dodaj_notatke);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        buttonDodajNotatke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save to datebase
                //Log.d(TAG, "onClick: Nazwa:" + nazwa.getText().toString()+ "onClick: typNotatki:" + typNotatki.getText().toString());

                db.notatkaDAO().insertAll(new Notatka(typNotatki.getText().toString(), nazwa.getText().toString()));
                startActivity(new Intent(NowaNotatka.this,Notatki.class));

            }
        });


    }
}