package com.example.mkaktusow.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NowyKaktus extends AppCompatActivity {

    EditText nazwaKaktusa;
    EditText gatunek;
    EditText nazwaMiejsca;
    Button buttonDodajKaktusa;

    ImageView imageView;
    Button buttonZrobzdj;

    LinearLayout linearLayoutdoukrywaniazdjecia;
    String pathDoZdjecia;

    LatLng lokalizacja;
    FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowy_kaktus);
        nazwaKaktusa = findViewById(R.id.nowykaktus_textinputedittext_1_nazwakaktusa);
        gatunek = findViewById(R.id.nowykaktus_textinputedittext_2_gatunek);
        nazwaMiejsca = findViewById(R.id.nowykaktus_textinputedittext_3_miejsce);
        buttonDodajKaktusa = findViewById(R.id.nowykaktus_button_dodajkaktus);
        imageView = findViewById(R.id.new_kaktus_image_view);
        buttonZrobzdj = findViewById(R.id.nowykaktus_zrobzdj);
        linearLayoutdoukrywaniazdjecia = findViewById(R.id.nowykaktus_linear_doukrywania_zdjecia);
        imageView.setVisibility(View.GONE);
        client = LocationServices.getFusedLocationProviderClient(this);


        //obsluga zdjecia
        if (ContextCompat.checkSelfPermission(NowyKaktus.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NowyKaktus.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        buttonZrobzdj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });

        if (ActivityCompat.checkSelfPermission(NowyKaktus.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lokalizacja = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(NowyKaktus.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lokalizacja = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        }


        //baza danych
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        buttonDodajKaktusa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save to datebase
                //Log.d("StworzKaktus", "onClick: Nazwa:" + nazwa.getText().toString()+ "onClick: typNotatki:" + typNotatki.getText().toString());

                if (TextUtils.isEmpty(pathDoZdjecia)) {
                    db.kaktusDAO().insertAll(new Kaktus(nazwaKaktusa.getText().toString(), gatunek.getText().toString(), nazwaMiejsca.getText().toString(), lokalizacja.latitude, lokalizacja.longitude));
                } else {
                    db.kaktusDAO().insertAll(new Kaktus(nazwaKaktusa.getText().toString(), gatunek.getText().toString(), nazwaMiejsca.getText().toString(), pathDoZdjecia, lokalizacja.latitude, lokalizacja.longitude));
                }

                startActivity(new Intent(NowyKaktus.this, Kaktusy.class));

            }
        });
    }


    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
                imageView.setVisibility(View.VISIBLE);
                buttonZrobzdj.setEnabled(false);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "KAKTUS_" + timeStamp + "_";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                pathDoZdjecia = contentUri.toString();

                Toast.makeText(this, "uri " + contentUri, Toast.LENGTH_LONG).show();

                setPic();

            }
        }
    }

    public void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                lokalizacja = new LatLng(location.getLatitude(),location.getLongitude());
                            }
                        }
                    });
                }

            }
        }
    }
}

