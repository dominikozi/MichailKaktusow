package com.example.mkaktusow.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.R;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NowyKaktus extends AppCompatActivity{

    EditText nazwaKaktusa;
    EditText gatunek;
    EditText nazwaMiejsca;
    Button buttonDodajKaktusa;

    ImageView imageView;
    Button buttonZrobzdj;

    LinearLayout linearLayoutdoukrywaniazdjecia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowy_kaktus);
        nazwaKaktusa=findViewById(R.id.nowykaktus_textinputedittext_1_nazwakaktusa);
        gatunek=findViewById(R.id.nowykaktus_textinputedittext_2_gatunek);
        nazwaMiejsca=findViewById(R.id.nowykaktus_textinputedittext_3_miejsce);
        buttonDodajKaktusa=findViewById(R.id.nowykaktus_button_dodajkaktus);
        imageView = findViewById(R.id.new_kaktus_image_view);
        buttonZrobzdj=findViewById(R.id.nowykaktus_zrobzdj);
        linearLayoutdoukrywaniazdjecia=findViewById(R.id.nowykaktus_linear_doukrywania_zdjecia);


        //baza danych
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

        //obsluga zdjecia
        if(ContextCompat.checkSelfPermission(NowyKaktus.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NowyKaktus.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        buttonZrobzdj.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //    startActivityForResult(intent, 100);


                launchSomeActivity.launch(intent);
            }
        });


    }



    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap captureImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(captureImage);
                    }
                }
    });



}

//TODO lokalizacja+mapy,  przypomnienia, kalendarz, rozpoznawanie po zdjeciu(w google grafika podobne zdjecia)