package com.example.mkaktusow.View;
import com.example.mkaktusow.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class Encyklopedia extends AppCompatActivity {

    ImageView imageView;
    Button button;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyklopedia);

        imageView=findViewById(R.id.encyklopedia_imageview);
        button = findViewById(R.id.encyklopedia_zrobzdj);
        textView= findViewById(R.id.encyklopedia_tekstimage_ML);

        //----bottom navigation bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set encyklopedia selected
        bottomNavigationView.setSelectedItemId(R.id.Encyklopedia);
        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
                switch(menuItem.getItemId()){
                    case R.id.Encyklopedia:
                        return true;
                    case R.id.Notatki:
                        startActivity(new Intent(getApplicationContext(),Notatki.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Miejsca:
                        startActivity(new Intent(getApplicationContext(), Kaktusy.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;

        });

        //----bottom navigation bar
        //
        /*
        button. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
*/
        button.setOnClickListener(v -> pickImage());
    }


    private int pickPhotoRequestCode = 665;

    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,pickPhotoRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==665){
                try {
                    textView.setText("");
                    Bitmap bitmap = getImageFomData(data);
                    if (bitmap!=null) {
                        imageView.setImageBitmap(bitmap);
                        InputImage image = InputImage.fromBitmap(bitmap,0);
                     //   processImageTagging(bitmap);
                        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
                        labeler.process(image).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                            @Override
                            public void onSuccess(@NonNull List<ImageLabel> imageLabels) {
                                for(ImageLabel label : imageLabels){
                                    textView.setText(textView.getText()+" "+label.getText());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.wtf("BLAB",e);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap getImageFomData(Intent data) throws IOException {
        Uri selectedImage = data.getData();

        return MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);

    }
}