package com.example.mkaktusow.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Gatunek;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NowyKaktus extends AppCompatActivity {

    EditText nazwaKaktusa;
    EditText gatunek;
    EditText nazwaMiejsca;

    ImageView imageView;
    RelativeLayout buttonZrobzdj;

    String pathDoZdjecia;

    LatLng lokalizacja;
    FusedLocationProviderClient client;

    Long idWybranegoGatunku;

    Button dodajKaktus;

    Spinner spinnerGatunek;
    List<String> gatunkiKaktusow;
    LocationManager mLocationManager;

    public static final int PICK_IMAGE = 1111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_nowy_kaktus);
        nazwaKaktusa = findViewById(R.id.nowykaktus_textinputedittext_1_nazwakaktusa);
    //    gatunek = findViewById(R.id.nowykaktus_textinputedittext_2_gatunek);
        nazwaMiejsca = findViewById(R.id.nowykaktus_textinputedittext_3_miejsce);
        imageView = findViewById(R.id.new_kaktus_image_view);
        buttonZrobzdj = findViewById(R.id.nowykaktus_zrobzdj);
        client = LocationServices.getFusedLocationProviderClient(this);
    //    fabDodajKaktus=findViewById(R.id.nowykaktus_fab_zapisz);
        dodajKaktus=findViewById(R.id.nowykaktus_button_dodajkaktus);
        //obsluga zdjecia
        if (ContextCompat.checkSelfPermission(NowyKaktus.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NowyKaktus.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }
        RelativeLayout buttonzGaleri = findViewById(R.id.nowykaktus_dodajzdj);

        //Spinner z lista kaktusow do wyboru
        spinnerGatunek = findViewById(R.id.nowykaktus_spinner);
        List<Gatunek> gatunki = db.gatunekDAO().getAllGatunki();
        gatunki.add(new Gatunek("Dodaj nowy gatunek...",6666));

        ArrayAdapter<Gatunek> spinnnerGatunkiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gatunki);
        spinnnerGatunkiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGatunek.setAdapter(spinnnerGatunkiAdapter);

        spinnerGatunek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id == gatunki.size()-1){

                    Dialog myDialog = new Dialog(NowyKaktus.this);
                    myDialog.setContentView(R.layout.dialog_dodanie_gatunku);

                    RelativeLayout relativeLayoutDodaj = (RelativeLayout) myDialog.findViewById(R.id.dialog_new_gatunek_dodaj);
                    RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialog_new_gatunek_zamknij);

                    myDialog.show();

                    relativeLayoutDodaj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialogObj = Dialog.class.cast(myDialog);
                            EditText nazwa = (EditText) dialogObj.findViewById(R.id.nowykaktus_textinputedittext_1_nazwakaktusa);
                            EditText liczba = (EditText) dialogObj.findViewById(R.id.nowykaktus_textinputedittext_2_nazwakaktusa);

                            if(nazwa.length()>0 && liczba.length()>0 && isNumeric(liczba.getText().toString())){
                                int liczbaDni = Integer.parseInt(liczba.getText().toString());
                                String nazwaG = nazwa.getText().toString();

                                db.gatunekDAO().insertAll(new Gatunek(nazwaG,  liczbaDni));
                                gatunki.remove(gatunki.get(gatunki.size()-1));
                                gatunki.add(new Gatunek(nazwaG,  liczbaDni));
                                gatunki.add(new Gatunek("Dodaj nowy gatunek...",6666));
                                idWybranegoGatunku = gatunki.get(gatunki.size()-2).getIdgatunek();
                                spinnnerGatunkiAdapter.notifyDataSetChanged();

                                Toast.makeText(NowyKaktus.this, "Pomyslnie dodano gatunek "+ nazwaG, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(NowyKaktus.this, "??le podane dane", Toast.LENGTH_SHORT).show();
                            }
                            myDialog.hide();
                        }
                    });
                    relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.hide();
                        }
                    });



                }else {
                    Gatunek gatunekTemp = (Gatunek) spinnerGatunek.getSelectedItem();
                    idWybranegoGatunku = gatunekTemp.getIdgatunek();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonzGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG); */

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        //button zrob zdjecie
        buttonZrobzdj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        askForLocPerm();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,
               1, mLocationListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("czyZdj")) {
                dispatchTakePictureIntent();

            }
        }
        dodajKaktus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if(isLocationEnabled(NowyKaktus.this)) {
                      //  if(lokalizacja!=null){

                            Date dataDodaniaKaktusa = Calendar.getInstance().getTime();

                            if (nazwaKaktusa.getText().toString().equals("") || nazwaMiejsca.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Musisz poda?? imi?? i nazwe miejsca.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (TextUtils.isEmpty(pathDoZdjecia)) {

                                    db.kaktusDAO().insertAll(new Kaktus(nazwaKaktusa.getText().toString(), idWybranegoGatunku, nazwaMiejsca.getText().toString(), null, lokalizacja.latitude, lokalizacja.longitude, dataDodaniaKaktusa));

                                    // db.kaktusDAO().insertAll(new Kaktus(nazwaKaktusa.getText().toString(), gatunek.getText().toString(), nazwaMiejsca.getText().toString(), null, lokalizacja.latitude, lokalizacja.longitude, dataDodaniaKaktusa));
                                } else {
                                    db.kaktusDAO().insertAll(new Kaktus(nazwaKaktusa.getText().toString(), idWybranegoGatunku, nazwaMiejsca.getText().toString(), pathDoZdjecia, lokalizacja.latitude, lokalizacja.longitude, dataDodaniaKaktusa));

                                    // db.kaktusDAO().insertAll(new Kaktus(nazwaKaktusa.getText().toString(), gatunek.getText().toString(), nazwaMiejsca.getText().toString(), pathDoZdjecia, lokalizacja.latitude, lokalizacja.longitude, dataDodaniaKaktusa));
                                }

                                startActivity(new Intent(NowyKaktus.this, Kaktusy.class));
                            }
                     //   }else{
                   //         askForLocPerm();
                   //     }
                    }else{
                        Toast.makeText(NowyKaktus.this, "Nie mo??na stworzy?? kaktusa bez dost??pu do lokalizacji.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(NowyKaktus.this, "Nie mo??na stworzy?? kaktusa bez po????czenia z internetem.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @SuppressWarnings("deprecation")
    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This was deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean isNumeric(String string){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (string == null) {
            return false;
        }
        return pattern.matcher(string).matches();
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
                System.out.println(pathDoZdjecia);

                Toast.makeText(this, "uri " + contentUri, Toast.LENGTH_LONG).show();
                Picasso.get().load(contentUri).fit().centerCrop().into(imageView);

                //                setPic();
            }
            if(requestCode==PICK_IMAGE){
                if(resultCode==RESULT_OK) {

                    Uri imageUri = data.getData();
              //      NowyKaktus.zrobnadrugimThreadzie load = new NowyKaktus.zrobnadrugimThreadzie();
           //         load.execute(imageUri);
                    Picasso.get().load(imageUri).fit().centerCrop().into(imageView);

                    Toast.makeText(NowyKaktus.this, "??adowanie...",Toast.LENGTH_LONG).show();

                    Glide.with(this)
                            .asBitmap()
                            .load(imageUri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                    String imageFileName = "KAKTUS_" + timeStamp + "_";
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
                                        Toast.makeText(getApplicationContext(), "Zdj??cie zapisane pomy??lnie",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "B????d podczas zapisywania zdj??cia", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });




                }else{
                    Toast.makeText(NowyKaktus.this, "Nie wybrano zdjecia.",Toast.LENGTH_LONG).show();
                }
            }
        }

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

                        }
                    });
                }

            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    private class zrobnadrugimThreadzie extends AsyncTask<Uri,Void,Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }

//        @Override
//        protected Void doInBackground(Uri... params) {
//            Uri imageUri = params[0];
//            runOnUiThread(new Runnable() {
//                  @Override
//                  public void run() {
//                      Picasso.get().load(imageUri).fit().centerCrop().into(imageView);
//                      Toast.makeText(NowyKaktus.this, "??adowanie...",Toast.LENGTH_LONG).show();
//                  }
//            });
//
//
//            Glide.with(NowyKaktus.this)
//                    .asBitmap()
//                    .load(imageUri)
//                    .into(new CustomTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                            String imageFileName = "KAKTUS_" + timeStamp + "_";
//                            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                            File image = null;
//                            try {
//                                image = File.createTempFile(
//                                        imageFileName,  /* prefix */
//                                        ".jpg",         /* suffix */
//                                        storageDir      /* directory */
//                                );
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            // Save a file: path for use with ACTION_VIEW intents
//                            currentPhotoPath = image.getAbsolutePath();
//                            File f = new File(currentPhotoPath);
//                            Uri contentUri = Uri.fromFile(f);
//                            pathDoZdjecia = contentUri.toString();
//                            System.out.println(pathDoZdjecia);
//
//                            boolean success = false;
//                            FileOutputStream outStream;
//                            try {
//
//                                outStream = new FileOutputStream(f);
//                                resource.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//                                /* 100 to keep full quality of the image */
//
//                                outStream.flush();
//                                outStream.close();
//                                success = true;
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            if (success) {runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(), "Zdj??cie zapisane pomy??lnie",
//                                        Toast.LENGTH_LONG).show();}
//                               });
//                            } else {runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getApplicationContext(),
//                                                "B????d podczas zapisywania zdj??cia", Toast.LENGTH_LONG).show();}
//                                        });
//                            }
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//                        }
//                    });
//
//
//
//
//
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//        }
//    }

    public void askForLocPerm(){
        if (ActivityCompat.checkSelfPermission(NowyKaktus.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Toast.makeText(getApplicationContext(),"tost2",Toast.LENGTH_SHORT).show();
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
                    // Toast.makeText(getApplicationContext(),"tost",Toast.LENGTH_SHORT).show();
                    if (location != null) {
                        lokalizacja = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        }
    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                lokalizacja = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            askForLocPerm();
            Toast.makeText(NowyKaktus.this, "Lokalizacja jest wy????czona.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText(NowyKaktus.this, "Lokalizacja jest w????czona.", Toast.LENGTH_SHORT).show();
        }
    };
}

