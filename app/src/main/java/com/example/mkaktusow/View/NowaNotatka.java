package com.example.mkaktusow.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mkaktusow.R;
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


        buttonDodajNotatke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save to datebase
                Log.d(TAG, "onClick: Nazwa:" + nazwa.getText().toString());
                Log.d(TAG, "onClick: typNotatki:" + typNotatki.getText().toString());
            }
        });


    }
}