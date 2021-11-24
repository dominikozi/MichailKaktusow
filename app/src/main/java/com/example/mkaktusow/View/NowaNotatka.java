package com.example.mkaktusow.View;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mkaktusow.BuildConfig;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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

    public static int count;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };
    public static final int PICK_IMAGE = 1111;

    EditText nazwa;
    EditText editTrescNotatki;
    RelativeLayout buttonZrobzdj;
    RelativeLayout buttonNagrajfilm;

    RadioButton radiobutton1;
    RadioButton radiobutton2;
    RadioButton radiobutton3;
    RadioButton radiobutton4;
    TextView trescnotatki_wiadomosc;
    LinearLayout linearLayoutdoukrywaniazdjecia;
    LinearLayout linearLayoutdoukrywaniatekstu;
    RelativeLayout linearLayoutdoukrywaniafilmu;
    LinearLayout linearLayoutdoukrywaniaaudio;
    LinearLayout linearvideoviewwrapper;
    ImageView imageView;
    VideoView videoView;
    MediaRecorder mediaRecorder;
    public static String fileName = "recorded.3gp";
    RelativeLayout imageButtonStart;
    RelativeLayout imageButtonOdtworz;
    RelativeLayout buttonZGalerii;
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
    TextView textViewTrescKaktus;
    Button buttonDodaj;
    boolean widzialnosc;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        buttonDodaj=findViewById(R.id.nowanotatka_button_dodajkaktus);

        linearLayoutdoukrywaniazdjecia=findViewById(R.id.nowanotatka_linear_doukrywania_zdjecia);
        linearLayoutdoukrywaniatekstu=findViewById(R.id.nowanotatka_linear_doukrywania_tekstu);
        linearLayoutdoukrywaniafilmu=findViewById(R.id.nowanotatka_linear_doukrywania_filmu);
        linearLayoutdoukrywaniaaudio=findViewById(R.id.nowanotatka_linear_doukrywania_audio);
        linearvideoviewwrapper = findViewById(R.id.nowanotatka_linear_videoview_wrapper);
        trescnotatki_wiadomosc=findViewById(R.id.nowanotatka_trescnotatki_);
        buttonZrobzdj=findViewById(R.id.nowanotatka_zrobzdj);
        buttonNagrajfilm=findViewById(R.id.nowanotatka_nagrajfilm);
        buttonZGalerii=findViewById(R.id.nowanotatka_dodajzdj);
        editTrescNotatki=findViewById(R.id.nowanotatka_textinputedittext_trescnotatki);
        imageView = findViewById(R.id.nowanotatka_image_view);
        videoView = findViewById(R.id.nowanotatka_video_view);
  //      videoView.setVisibility(View.GONE);
        //ustawienie poczatkowe widzialnosci
        linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
        linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
        trescnotatki_wiadomosc.setVisibility(View.GONE);
        linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
        linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);

        textViewTrescKaktus = findViewById(R.id.textviewtextkaktus);

        radiobutton1= findViewById(R.id.nowanotatka_radiobutton_czytekstowa);
        radiobutton2= findViewById(R.id.nowanotatka_radiobutton_czyzdj);
        radiobutton3= findViewById(R.id.nowanotatka_radiobutton_czyaudio);
        radiobutton4= findViewById(R.id.nowanotatka_radiobutton_czyfilm);

        radiobutton1.setOnClickListener(this);
        radiobutton2.setOnClickListener(this);
        radiobutton3.setOnClickListener(this);
        radiobutton4.setOnClickListener(this);

        imageButtonStart = findViewById(R.id.nowanotatka_przycisk_nagraj_audio);
        imageButtonOdtworz = findViewById(R.id.nowanotatka_przycisk_odtworz_audio);


        //OBSLUGA FAB ZAPISZ
        buttonDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (widzialnosc==false) {
                    Toast.makeText(getApplicationContext(),"Wypelnij dane",Toast.LENGTH_LONG).show();
                }else{
                    if(typNotatki=="zdjecie"&&pathDoZdjecia==null){
                        Toast.makeText(getApplicationContext(),"musi byc zdjecie",Toast.LENGTH_LONG).show();
                    }else {
                        trescNotatki = editTrescNotatki.getText().toString();
                        if(!trescNotatki.equals("")){
                            if(trescNotatki.length()>23){
                                nazwa.setText(trescNotatki.substring(0,23)+"...");
                            }else{
                                nazwa.setText(trescNotatki);
                            }
                        }
                        Date dataDodaniaNotatki = Calendar.getInstance().getTime();
                        //    public Notatka(String typNotatki, String nazwaNotatki, String trescNotatki, String sciezkaDoZdjecia, String sciezkaDoAudio, String sciezkaDoFilmu, Date dataDodania, long kaktusid) {
                        db.notatkaDAO().insertAll(new Notatka(typNotatki, nazwa.getText().toString(), trescNotatki, pathDoZdjecia, pathDoAudio, pathDoFilmu, dataDodaniaNotatki, idWybranegoKaktusa));

                        count++;
                        startActivity(new Intent(NowaNotatka.this, Notatki.class));
                    }
                }
            }
        });

        buttonZGalerii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG); */

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        //obsługa AUDIO
        mediaRecorder = new MediaRecorder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }

        TextView tekstaudio = findViewById(R.id.nowa_notatka_tekstaudio);
        TextView tekstaudioodtworz = findViewById(R.id.nowa_notatka_tekstodtworzaudio);

        imageButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ktora_akcja == 0) {
                    rozpocznijNagrywanieDzwieku();
                    tekstaudio.setText("ZATRZYMAJ NAGRYWANIE");
                } else if (ktora_akcja == 1) {
                    przerwijNagrywanieDzwieku();
                    tekstaudio.setText("NAGRANIE ZAPISANE");
                }else if(ktora_akcja == 2){
                    Toast.makeText(NowaNotatka.this,"Nagranie zostało już zapisane",Toast.LENGTH_SHORT).show();
                }
            }
        });


        imageButtonOdtworz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ktora_akcja==2){
                     OdtworzNagranieDzwieku();
                }
            }
        });

        //Spinner z lista kaktusow do wyboru
        spinnerlistakaktusow = findViewById(R.id.nowanotatka_spinner_listakaktusow);
        List<Kaktus> kaktusy = db.kaktusDAO().getAllKaktusy();

        ArrayAdapter<Kaktus> spinnnerKaktusyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kaktusy);
        spinnnerKaktusyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlistakaktusow.setAdapter(spinnnerKaktusyAdapter);

        Long idkaktusaprzekazana=getIntent().getLongExtra("idkaktusap",0L);
        if(idkaktusaprzekazana==-1){
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
        }else{
            spinnerlistakaktusow.setVisibility(View.GONE);
            textViewTrescKaktus.setVisibility(View.GONE);
            idWybranegoKaktusa=idkaktusaprzekazana;
        }

        //OBSLUGA PRZYCISKU DODAJ NOTATKE
        nazwa=findViewById(R.id.nowanotatka_textinputedittext_1_nazwanotatki);
        int idtemp = getIntent().getIntExtra("liczbanotatek", 0)+1;
        nazwa.setText("Notatka #"+idtemp);

        widzialnosc=false;

 //       imageView.setVisibility(View.GONE);

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

        linearvideoviewwrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()) {
                    videoView.pause();
                }
                else {
                    videoView.start();
                }
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
        imageView.setVisibility(View.VISIBLE);
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
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                pathDoZdjecia = contentUri.toString();

                Toast.makeText(this, "uri " + contentUri, Toast.LENGTH_LONG).show();

                Picasso.get().load(contentUri).fit().centerCrop().into(imageView);
                //  setPic();


            }
            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

                File f = new File(currentVideoPath);
                Uri contentUri = Uri.fromFile(f);
                pathDoFilmu = contentUri.toString();

                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(currentVideoPath);

                Bitmap pierwszaKlatka = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
                Bitmap croppedPKlatka = ThumbnailUtils.extractThumbnail(pierwszaKlatka, 300, 400); //zmiana na 4:3 z 16:9(defaultowe camery)

                //
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);

                File ff = new File(currentPhotoPath);
                Uri contentUrii = Uri.fromFile(ff);
                pathDoZdjecia = contentUrii.toString();

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(ff);
                    croppedPKlatka.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //


                Toast.makeText(this, "uri " + contentUrii, Toast.LENGTH_LONG).show();

                videoView.setVideoURI(Uri.parse(pathDoFilmu));
                videoView.seekTo(1);
                videoView.setVisibility(View.VISIBLE);
                buttonNagrajfilm.setEnabled(false);
            }
            if (requestCode == PICK_IMAGE) {
                if (resultCode == RESULT_OK) {

                    Uri imageUri = data.getData();

                    Picasso.get().load(imageUri).fit().centerCrop().into(imageView);

                    Toast.makeText(NowaNotatka.this, "Ładowanie...", Toast.LENGTH_LONG).show();

                    Glide.with(this)
                            .asBitmap()
                            .load(imageUri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                    String imageFileName = "JPEG_" + timeStamp + "_";
                                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                    File image = null;
                                    try {
                                        image = File.createTempFile(
                                                imageFileName,  /* prefix */
                                                ".jpg",         /* suffix */
                                                storageDir      /* directory */
                                        );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    // Save a file: path for use with ACTION_VIEW intents
                                    currentPhotoPath = image.getAbsolutePath();
                                    File f = new File(currentPhotoPath);
                                    Uri contentUri = Uri.fromFile(f);
                                    pathDoZdjecia = contentUri.toString();
                                    System.out.println(pathDoZdjecia);

                                    boolean success = false;
                                    FileOutputStream outStream;
                                    try {

                                        outStream = new FileOutputStream(f);
                                        resource.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                                        /* 100 to keep full quality of the image */

                                        outStream.flush();
                                        outStream.close();
                                        success = true;
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "Zdjęcie zapisane pomyślnie",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Błąd podczas zapisywania zdjęcia", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });


                } else {
                    Toast.makeText(NowaNotatka.this, "Nie wybrano zdjecia", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.nowanotatka_radiobutton_czytekstowa:
                linearLayoutdoukrywaniatekstu.setVisibility(View.VISIBLE);
                trescnotatki_wiadomosc.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);
                typNotatki="tekstowa";
                widzialnosc=true;
                break;

            case R.id.nowanotatka_radiobutton_czyzdj:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.VISIBLE);
                trescnotatki_wiadomosc.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.VISIBLE);
                typNotatki="zdjecie";
                widzialnosc=true;
                break;

            case R.id.nowanotatka_radiobutton_czyaudio:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.GONE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.VISIBLE);
                trescnotatki_wiadomosc.setVisibility(View.GONE);

                typNotatki="audio";
                widzialnosc=true;
                break;

            case R.id.nowanotatka_radiobutton_czyfilm:
                linearLayoutdoukrywaniazdjecia.setVisibility(View.GONE);
                linearLayoutdoukrywaniatekstu.setVisibility(View.GONE);
                linearLayoutdoukrywaniafilmu.setVisibility(View.VISIBLE);
                linearLayoutdoukrywaniaaudio.setVisibility(View.GONE);
                trescnotatki_wiadomosc.setVisibility(View.GONE);

                typNotatki="film";
                widzialnosc=true;
                break;
        }
    }
    int ktora_akcja=0;
    public void rozpocznijNagrywanieDzwieku(){
        try {
            if(pathDoAudio==null) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "3GP_" + timeStamp + "_";
                fileName = imageFileName;
                File storageDir2 = getExternalFilesDir(Environment.DIRECTORY_MUSIC);

                file = storageDir2 + "/" + imageFileName + ".3gp";
                pathDoAudio = file;

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(file);
                mediaRecorder.prepare();
                mediaRecorder.start();
                ktora_akcja=1;
            }else{
                Toast.makeText(this, "Nagrałeś już audio",Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void przerwijNagrywanieDzwieku(){
        try {

            mediaRecorder.stop();
            mediaRecorder.release();

            ktora_akcja=2;
        }
        catch(Exception e){
            Toast.makeText(this,"Odtrwórz nagranie by móc je zatrzymać",Toast.LENGTH_SHORT).show();
        }
    }
    public void OdtworzNagranieDzwieku(){

        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(this,"Nie ma zapisanego nagrania do odtworzenia",Toast.LENGTH_SHORT).show();

        }


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

