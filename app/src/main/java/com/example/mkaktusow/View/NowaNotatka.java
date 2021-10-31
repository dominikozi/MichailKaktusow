package com.example.mkaktusow.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NowaNotatka extends AppCompatActivity implements View.OnClickListener {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    EditText nazwa;
    Button buttonDodajNotatke;
    Button buttonZrobzdj;
    Button buttonNagrajfilm;

    RadioButton radiobutton1;
    RadioButton radiobutton2;
    RadioButton radiobutton3;
    RadioButton radiobutton4;

    LinearLayout linearLayoutdoukrywaniazdjecia;
    LinearLayout linearLayoutdoukrywaniatekstu;
    LinearLayout linearLayoutdoukrywaniafilmu;
    LinearLayout linearLayoutdoukrywaniaaudio;
    ImageView imageView;

    TextView textViewCzynagrywanietrwa;
    MediaRecorder mediaRecorder;
    public static String fileName = "recorded.3gp";
    ImageButton imageButtonStart;
    ImageButton imageButtonStop;
    ImageButton imageButtonOdtworz;
   // String file = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+fileName;
    String file = Environment.getExternalStorageDirectory()+"/Kaktusy/"+File.separator+fileName;


    Spinner spinnerlistakaktusow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowa_notatka);
        //--db
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        //!!--db
        File f = new File(Environment.getExternalStorageDirectory()+"/Kaktusy/");
        if (!f.exists()) {
            f.mkdirs();
        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        linearLayoutdoukrywaniazdjecia=findViewById(R.id.nowanotatka_linear_doukrywania_zdjecia);
        linearLayoutdoukrywaniatekstu=findViewById(R.id.nowanotatka_linear_doukrywania_tekstu);
        linearLayoutdoukrywaniafilmu=findViewById(R.id.nowanotatka_linear_doukrywania_filmu);
        linearLayoutdoukrywaniaaudio=findViewById(R.id.nowanotatka_linear_doukrywania_audio);

        buttonZrobzdj=findViewById(R.id.nowanotatka_zrobzdj);
        buttonNagrajfilm=findViewById(R.id.nowanotatka_nagrajfilm);

        imageView = findViewById(R.id.nowanotatka_image_view);

        //ustawienie poczatkowe widzialnosci
        linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
        linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
        linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
        linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);

        radiobutton1= findViewById(R.id.nowanotatka_radiobutton_czytekstowa);
        radiobutton2= findViewById(R.id.nowanotatka_radiobutton_czyzdj);
        radiobutton3= findViewById(R.id.nowanotatka_radiobutton_czyaudio);
        radiobutton4= findViewById(R.id.nowanotatka_radiobutton_czyfilm);

        radiobutton1.setOnClickListener(this);
        radiobutton2.setOnClickListener(this);
        radiobutton3.setOnClickListener(this);
        radiobutton4.setOnClickListener(this);

        imageButtonStart = findViewById(R.id.nowanotatka_przycisk_nagraj_audio);
        imageButtonStop = findViewById(R.id.nowanotatka_przycisk_stop_nagrywania_audio);
        imageButtonOdtworz = findViewById(R.id.nowanotatka_przycisk_odtworz_audio);
        textViewCzynagrywanietrwa = findViewById(R.id.nowanotatka_tekst_czynagrywanietrwa);
        mediaRecorder = new MediaRecorder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(file);
        }

        //Spinner z lista kaktusow do wyboru
        spinnerlistakaktusow = findViewById(R.id.nowanotatka_spinner_listakaktusow);
        List<Kaktus> kaktusy = db.kaktusDAO().getAllKaktusy();

        ArrayAdapter<Kaktus> spinnnerKaktusyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kaktusy);
        spinnnerKaktusyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlistakaktusow.setAdapter(spinnnerKaktusyAdapter);

        spinnerlistakaktusow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Kaktus kaktustemp = (Kaktus) spinnerlistakaktusow.getSelectedItem();
                String nazwakaktusatemp = kaktustemp.getNazwaKaktusa();
                Long idKaktusatemp = kaktustemp.getIdkaktus();
                Toast.makeText(NowaNotatka.this, nazwakaktusatemp +", ID: "+idKaktusatemp, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //obsługa audio
        imageButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rozpocznijNagrywanieDzwieku();
            }
        });

        imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                przerwijNagrywanieDzwieku();
            }
        });

        imageButtonOdtworz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OdtworzNagranieDzwieku();
            }
        });

        nazwa=findViewById(R.id.nowanotatka_textinputedittext_1_nazwanotatki);
        buttonDodajNotatke=findViewById(R.id.nowanotatka_button_dodaj_notatke);

        buttonDodajNotatke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save to datebase
                //Log.d("StworzNotatke", "onClick: Nazwa:" + nazwa.getText().toString()+ "onClick: typNotatki:" + typNotatki.getText().toString());

                db.notatkaDAO().insertAll(new Notatka(nazwa.getText().toString()));
                startActivity(new Intent(NowaNotatka.this,Notatki.class));

            }
        });


        //obsluga zdjecia
        if(ContextCompat.checkSelfPermission(NowaNotatka.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NowaNotatka.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        buttonZrobzdj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launchZrobZdjActivity.launch(intent);
            }
        });

        buttonNagrajfilm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                launchNagrajFilmActivity.launch(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.nowanotatka_radiobutton_czytekstowa:
                linearLayoutdoukrywaniatekstu.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);
                break;

            case R.id.nowanotatka_radiobutton_czyzdj:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);
                break;

            case R.id.nowanotatka_radiobutton_czyaudio:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.VISIBLE);
                break;

            case R.id.nowanotatka_radiobutton_czyfilm:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);

                break;

        }
    }

    ActivityResultLauncher<Intent> launchZrobZdjActivity = registerForActivityResult(
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

    ActivityResultLauncher<Intent> launchNagrajFilmActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        // akcje

                    }
                }
            });

    public void rozpocznijNagrywanieDzwieku(){
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textViewCzynagrywanietrwa.setText("Trwa nagrywanie dźwięku");

    }
    public void przerwijNagrywanieDzwieku(){
        mediaRecorder.stop();
        mediaRecorder.release();

        textViewCzynagrywanietrwa.setText("Nagrywanie zakończone");

    }
    public void OdtworzNagranieDzwieku(){
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textViewCzynagrywanietrwa.setText("Odtwarzanie nagrania");

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

