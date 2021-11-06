package com.example.mkaktusow.View;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.example.mkaktusow.BuildConfig;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
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
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    EditText editTrescNotatki;
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
    VideoView videoView;
    TextView textViewCzynagrywanietrwa;
    MediaRecorder mediaRecorder;
    public static String fileName = "recorded.3gp";
    ImageButton imageButtonStart;
    ImageButton imageButtonStop;
    ImageButton imageButtonOdtworz;

    String file = Environment.getExternalStorageDirectory()+"/Kaktusy/"+File.separator+fileName;
    String filmpath;

    Spinner spinnerlistakaktusow;

    //zmienne do zapisywania do bazy danych
    Long idWybranegoKaktusa;
    String typNotatki; //0-tekstowa, 1-audio, 2-zdjecie, 3-film
    String pathDoZdjecia;
    String pathDoAudio;
    String pathDoFilmu;
    String trescNotatki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowa_notatka);
        //--db
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        //!!--db

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        linearLayoutdoukrywaniazdjecia=findViewById(R.id.nowanotatka_linear_doukrywania_zdjecia);
        linearLayoutdoukrywaniatekstu=findViewById(R.id.nowanotatka_linear_doukrywania_tekstu);
        linearLayoutdoukrywaniafilmu=findViewById(R.id.nowanotatka_linear_doukrywania_filmu);
        linearLayoutdoukrywaniaaudio=findViewById(R.id.nowanotatka_linear_doukrywania_audio);

        buttonZrobzdj=findViewById(R.id.nowanotatka_zrobzdj);
        buttonNagrajfilm=findViewById(R.id.nowanotatka_nagrajfilm);

        editTrescNotatki=findViewById(R.id.nowanotatka_textinputedittext_trescnotatki);
        imageView = findViewById(R.id.nowanotatka_image_view);
        videoView = findViewById(R.id.nowanotatka_video_view);
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
        //obsługa AUDIO
        mediaRecorder = new MediaRecorder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }

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
                idWybranegoKaktusa = kaktustemp.getIdkaktus();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //OBSLUGA PRZYCISKU DODAJ NOTATKE
        nazwa=findViewById(R.id.nowanotatka_textinputedittext_1_nazwanotatki);
        buttonDodajNotatke=findViewById(R.id.nowanotatka_button_dodaj_notatke);

        buttonDodajNotatke.setEnabled(false);

        buttonDodajNotatke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    String pathDoAudio;
                //    String pathDoFilmu;
                //    String trescNotatki;
                trescNotatki=editTrescNotatki.getText().toString();
                Date dataDodaniaNotatki = Calendar.getInstance().getTime();

                //    public Notatka(String typNotatki, String nazwaNotatki, String trescNotatki, String sciezkaDoZdjecia, String sciezkaDoAudio, String sciezkaDoFilmu, Date dataDodania, long kaktusid) {
                db.notatkaDAO().insertAll(new Notatka( typNotatki, nazwa.getText().toString(), trescNotatki, pathDoZdjecia,pathDoAudio,pathDoFilmu,dataDodaniaNotatki ,idWybranegoKaktusa ));

                startActivity(new Intent(NowaNotatka.this,Notatki.class));
            }
        });


        //obsluga ZDJECIA
        if(ContextCompat.checkSelfPermission(NowaNotatka.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NowaNotatka.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        buttonZrobzdj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                  dispatchTakePictureIntent();

            }
        });

        //obsluga FILMU
        buttonNagrajfilm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
          //      Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String strFileName = "Clip_" + timeStamp +"_";
                File fileFile;


                try {
                    fileFile = File.createTempFile(strFileName,".mp4",storageDir);
                    currentVideoPath=fileFile.getAbsolutePath();
                    Uri contentUri = FileProvider.getUriForFile(NowaNotatka.this, "com.example.android.fileprovider", fileFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,contentUri);

                    startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            //    launchNagrajFilmActivity.launch(intent);

            }
        });
    }
    static final int REQUEST_VIDEO_CAPTURE = 111;

    String currentPhotoPath;
    String currentVideoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                pathDoZdjecia = contentUri.toString();

                Toast.makeText(this, "uri " + contentUri, Toast.LENGTH_LONG).show();

                setPic();


            }
            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

                File f = new File(currentVideoPath);
                Uri contentUri = Uri.fromFile(f);
                pathDoFilmu=contentUri.toString();

                Toast.makeText(this, "uri " + contentUri, Toast.LENGTH_LONG).show();

                videoView.setVideoURI(Uri.parse(pathDoFilmu));
                videoView.start();

            }
        }
    }

    public void setPic(){
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.nowanotatka_radiobutton_czytekstowa:
                linearLayoutdoukrywaniatekstu.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);
                typNotatki="tekstowa";
                buttonDodajNotatke.setEnabled(true);
                break;

            case R.id.nowanotatka_radiobutton_czyzdj:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.VISIBLE);
                typNotatki="zdjecie";
                buttonDodajNotatke.setEnabled(true);
                break;

            case R.id.nowanotatka_radiobutton_czyaudio:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.VISIBLE);
                typNotatki="audio";
                buttonDodajNotatke.setEnabled(true);
                break;

            case R.id.nowanotatka_radiobutton_czyfilm:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);
                typNotatki="film";
                buttonDodajNotatke.setEnabled(true);
                break;
        }
    }


    ActivityResultLauncher<Intent> launchNagrajFilmActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri videoUri = data.getData();
                        videoView.setVideoURI(videoUri);
                        videoView.start();
                        // akcje



                    }
                }
            });

    public void rozpocznijNagrywanieDzwieku(){
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "3GP_" + timeStamp + "_";
            fileName = imageFileName;
            File storageDir2 = getExternalFilesDir(Environment.DIRECTORY_MUSIC);

            file = storageDir2+"/"+imageFileName+".3gp";
            pathDoAudio=file;

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(file);
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

