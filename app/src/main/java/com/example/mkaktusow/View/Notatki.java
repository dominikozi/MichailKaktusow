package com.example.mkaktusow.View;
import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Notatki extends AppCompatActivity implements NotatkaAdapter.OnNotatkaListener, MediaPlayer.OnPreparedListener {


    RecyclerView recyclerView;
    NotatkaAdapter adapter;

    List<Notatka> notatki;
    List<Notatka> notatki_wszystkie;
    FloatingActionButton fabDuzy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notatki);


        //TODO robić to w background threat a nie w main!
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        notatki = db.notatkaDAO().getAllNotatki();
        notatki_wszystkie= db.notatkaDAO().getAllNotatki();


        //recycler view
        recyclerView = findViewById(R.id.notatkiRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NotatkaAdapter(notatki, this);

        recyclerView.setAdapter(adapter);


        //----bottom navigation bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set Miejsca selected
        bottomNavigationView.setSelectedItemId(R.id.Notatki);
        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Notatki:
                        return true;
                    case R.id.Encyklopedia:
                        startActivity(new Intent(getApplicationContext(),Encyklopedia.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Miejsca:
                        startActivity(new Intent(getApplicationContext(), Kaktusy.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Mapa:
                        startActivity(new Intent(getApplicationContext(),Mapa.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //!!!!!----bottom navigation bar


        //fab
        fabDuzy=findViewById(R.id.notatki_floatingActionButtonDuzy);
        fabDuzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notatki.this, NowaNotatka.class);
                intent.putExtra("idkaktusap",-1L);
                intent.putExtra("liczbanotatek",adapter.getItemCount());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case 221:
                int id = item.getGroupId();
                displayMessage("Usunięto notatke o nazwie: "+notatki.get(id).getNazwaNotatki());
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                db.notatkaDAO().deleteByNotatkaId(notatki.get(id).getIdnotatka());
                adapter.removeNotatka(id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void displayMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    MediaPlayer mediaPlayer;
    TextView remaining;
    TextView elapsed;
    SeekBar seekbar;
    @Override
    public void onNotatkaClick(int position) {
        if (notatki.get(position).getTypNotatki() .equals( "audio")) {
            Dialog myDialog = new Dialog(Notatki.this);
            myDialog.setContentView(R.layout.dialog_notatka_audio);
            TextView dialog_nazwanotatki = (TextView) myDialog.findViewById(R.id.dialog_nazwa);
            RelativeLayout imageButton = (RelativeLayout) myDialog.findViewById(R.id.dialog_odtworz_audio);
            RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialog_layout_zamknij);
            dialog_nazwanotatki.setText(notatki.get(position).getNazwaNotatki());
            TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            myDialog.show();
            seekbar = (SeekBar) myDialog.findViewById(R.id.dialog_notatka_audio_seekbar);

            remaining = (TextView) myDialog.findViewById(R.id.dialog_notatka_seekbar1);
            elapsed = (TextView) myDialog.findViewById(R.id.dialog_notatka_seekbar2);
            myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    stopPlaying();
                }
            });
            relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mediaPlayer = MediaPlayer.create(Notatki.this,Uri.parse(notatki.get(position).getSciezkaDoAudio()));
                        mediaPlayer.start();
                        seekbar.setProgress(0);
                        seekbar.setMax(mediaPlayer.getDuration());

                        seekbar.postDelayed(updateSeekBar, 15);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else if (notatki.get(position).getTypNotatki() .equals( "zdjecie")) {
            Dialog myDialog = new Dialog(Notatki.this);
            myDialog.setContentView(R.layout.dialog_notatka_zdjecie);
            ImageView imageView = (ImageView) myDialog.findViewById(R.id.dialog_notatkazdjecie_zdjecie);
            Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).fit().centerCrop().into(imageView);
            TextView textView = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_tekst_zawartosc);
            textView.setText(notatki.get(position).getTrescNotatki());
            textView.setVisibility(View.GONE);

            TextView dialog_nazwanotatki = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa);
            RelativeLayout relativeLayoutEdytuj = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_edytuj);
            RelativeLayout relativeLayoutAudio = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_audio);
            RelativeLayout relativeLayoutTekst = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_tekst);
            RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_zamknij);
            TextView textView2 = (TextView) myDialog.findViewById(R.id.dialognotatkazdjecie_zobacz_tekst_notatki);
            TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            dialog_nazwanotatki.setText(notatki.get(position).getNazwaNotatki());
            myDialog.show();

            relativeLayoutEdytuj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Notatki.this, JednaNotatka.class);
                    Long tempId = notatki.get(position).getIdnotatka();
                    intent.putExtra("id_notatki", tempId);
                    startActivity(intent);
                }
            });
            relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });

            relativeLayoutAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((notatki.get(position).getSciezkaDoAudio()!=null)&&(!notatki.get(position).getSciezkaDoAudio().equals(""))){
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(notatki.get(position).getSciezkaDoAudio());
                           mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(Notatki.this, "Brak przypisanego audio",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            relativeLayoutTekst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textView2.getText().equals("ZOBACZ TEKST NOTATKI")){
                        if(notatki.get(position).getTrescNotatki().length()>0) {
                            textView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            textView2.setText("ZOBACZ ZDJECIE");
                        }else{
                            Toast.makeText(Notatki.this, "Brak przypisanej notatki",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        textView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        textView2.setText("ZOBACZ TEKST NOTATKI");
                    }
                }
            });
        }else if (notatki.get(position).getTypNotatki() .equals( "film")) {
            Dialog myDialog = new Dialog(Notatki.this);
            myDialog.setContentView(R.layout.dialog_notatka_film);
            VideoView videoView = (VideoView) myDialog.findViewById(R.id.dialog_notatka_film_filmview);
            RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatka_film_zamknij);
            TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            videoView.setVideoURI(Uri.parse(notatki.get(position).getSciezkaDoFilmu()));
            RelativeLayout filmlinearwrapper = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatka_film_filmview_wrapper);
            videoView.seekTo(1);
            myDialog.show();

            filmlinearwrapper.setOnClickListener(new View.OnClickListener() {
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
            relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
        } else if (notatki.get(position).getTypNotatki() .equals( "tekstowa")) {
            Dialog myDialog = new Dialog(Notatki.this);
            myDialog.setContentView(R.layout.dialog_notatka_tekstowa);
            TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            TextView textView = (TextView) myDialog.findViewById(R.id.dialognotatka_tekst_zawartosc);
            textView.setText(notatki.get(position).getTrescNotatki());
            TextView textViewNazwa = (TextView) myDialog.findViewById(R.id.dialognotatka_tekst_nazwa);
            textViewNazwa.setText(notatki.get(position).getNazwaNotatki());

            RelativeLayout relativeLayoutEdytuj = (RelativeLayout) myDialog.findViewById(R.id.dialognotatka_tekst_edytuj);
            RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialognotatka_tekst_zamknij);

            myDialog.show();

            relativeLayoutEdytuj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Notatki.this, JednaNotatka.class);
                    Long tempId = notatki.get(position).getIdnotatka();
                    intent.putExtra("id_notatki", tempId);
                    startActivity(intent);
                }
            });

            relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_sortowanie_n,menu);

        return true;
    }
    int c1=0;
    int c2=0;
    int c3=0;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String komunikat=null;
        switch(item.getItemId()){
            case R.id.om_n_filtr_reset:
                notatki.clear();
                for (Notatka notatka:notatki_wszystkie){
                    notatki.add(notatka);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Odswiezono notatki",Toast.LENGTH_SHORT).show();
                break;
            case R.id.om_n_sort_nazwa:
                if(c1==0){
                    Collections.sort(notatki, Notatka.NotatkaNazwaAZComparaotr);
                    c1++;
                    komunikat = "A->Z";
                }else if(c1==1){
                    Collections.sort(notatki, Notatka.NotatkaNazwaZAComparaotr);
                    c1--;
                    komunikat = "Z->A";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po nazwie "+komunikat,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_n_sort_data:
                if(c2==0){
                    Collections.sort(notatki,Notatka.NotatkaDataRosnacoComparaotr);
                    c2++;
                    komunikat = "Rosnąco";
                }else if(c2==1){
                    Collections.sort(notatki,Notatka.NotatkaDataMalejacoComparaotr);
                    c2--;
                    komunikat = "Malejąco";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po dacie "+komunikat,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_n_sort_typ:
                if(c3==0){
                    Collections.sort(notatki,Notatka.NotatkaTypAZComparaotr);
                    c3++;
                    komunikat = "A->Z";
                }else if(c3==1){
                    Collections.sort(notatki,Notatka.NotatkaTypZAComparaotr);
                    c3--;
                    komunikat = "Z->A";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po gatunku "+komunikat,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_n_filtr_z:
                ArrayList<Notatka> notatkitemp=new ArrayList<>();
                for (Notatka notatka:notatki){
                    if(notatka.getTypNotatki().equals("zdjecie")){
                        notatkitemp.add(notatka);
                    }
                }
                notatki.clear();
                for (Notatka notatka:notatkitemp){
                    notatki.add(notatka);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"filtr tylko zdjecia",Toast.LENGTH_SHORT).show();
                break;
            case R.id.om_n_filtr_t:
                ArrayList<Notatka> notatkitemp2=new ArrayList<>();
                for (Notatka notatka:notatki){
                    if(notatka.getTypNotatki().equals("tekstowa")){
                        notatkitemp2.add(notatka);
                    }
                }
                notatki.clear();
                for (Notatka notatka:notatkitemp2){
                    notatki.add(notatka);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"filtr tylko tekst",Toast.LENGTH_SHORT).show();
                break;
            case R.id.om_n_filtr_f:
                ArrayList<Notatka> notatkitemp3=new ArrayList<>();
                for (Notatka notatka:notatki){
                    if(notatka.getTypNotatki().equals("film")){
                        notatkitemp3.add(notatka);
                    }
                }
                notatki.clear();
                for (Notatka notatka:notatkitemp3){
                    notatki.add(notatka);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"filtr tylko film",Toast.LENGTH_SHORT).show();
                break;
            case R.id.om_n_filtr_a:
                ArrayList<Notatka> notatkitemp4=new ArrayList<>();
                for (Notatka notatka:notatki){
                    if(notatka.getTypNotatki().equals("audio")){
                        notatkitemp4.add(notatka);
                    }
                }
                notatki.clear();
                for (Notatka notatka:notatkitemp4){
                    notatki.add(notatka);
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"filtr tylko audio",Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
     //   recyclerQuestion.setAdapter(adapterRecyclerQuestion);
    }

    @Override
    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        mediaPlayer.start();
    }
    private Runnable updateSeekBar = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            elapsed.setText(""+ milliSecondsToTimer(totalDuration-currentDuration));
            // Displaying time completed playing
            remaining.setText(""+ milliSecondsToTimer(currentDuration));

            // Updating progress bar
            seekbar.setProgress((int)currentDuration);

            // Call this thread again after 15 milliseconds => ~ 1000/60fps
            seekbar.postDelayed(this, 15);
        }
    };

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10) {
            secondsString = "0" + seconds;
        }else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}