package com.example.mkaktusow.View;
import com.example.mkaktusow.Controller.KaktusAdapter;

import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;

import com.example.mkaktusow.Model.MKaktusow;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.support.v4.app.*;

public class Kaktusy extends AppCompatActivity implements KaktusAdapter.OnKaktusListener {

    public static final int POWIADOMIENIE_DZIENNIK_ILE_DNI=1;

    RecyclerView recyclerView;
    KaktusAdapter adapter;
    List<Kaktus> kaktusy;

    FloatingActionButton fabDuzy;
    FloatingActionButton fabMalyZdj;
    FloatingActionButton fabMalyBezZdj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miejsca);

        notificationManagerCompat=NotificationManagerCompat.from(this);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        kaktusy = db.kaktusDAO().getAllKaktusy();

        //recycler view
        recyclerView = findViewById(R.id.kaktusyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new KaktusAdapter(kaktusy, this);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabDuzy.isShown())
                    fabDuzy.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fabDuzy.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        //----bottom navigation bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set Miejsca selected
        bottomNavigationView.setSelectedItemId(R.id.Miejsca);
        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.Miejsca:
                        return true;
                    case R.id.Notatki:
                        startActivity(new Intent(getApplicationContext(),Notatki.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Encyklopedia:
                        startActivity(new Intent(getApplicationContext(),Encyklopedia.class));
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
        //----bottom navigation bar

        //fab
        fabDuzy = findViewById(R.id.kaktusy_floatingActionButtonDuzy);
        fabMalyZdj = findViewById(R.id.kaktusy_floatingActionButtonZdjecie);
        fabMalyBezZdj = findViewById(R.id.kaktusy_floatingActionButtonBezZdjecia);

        fabDuzy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(fabMalyZdj.getVisibility()!=View.VISIBLE) {
                    fabMalyZdj.setVisibility(View.VISIBLE);
                    fabMalyBezZdj.setVisibility(View.VISIBLE);
                }else{
                    fabMalyZdj.setVisibility(View.GONE);
                    fabMalyBezZdj.setVisibility(View.GONE);
                }
            }
        });

        fabMalyZdj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"nowy kaktus ze zdj", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Kaktusy.this, NowyKaktus.class);
                intent.putExtra("czyZdj", 1);

                startActivity(intent);

            }
        });
        fabMalyBezZdj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendNotificationPodlanie("kaktus na oknie");


                String listaK= getListOfDziennik();
                if(!listaK.equals("BRAK")) {
                    SendNotificationDziennik(listaK);
                }

                startActivity(new Intent(Kaktusy.this, NowyKaktus.class));
            }
        });
    }

    @Override
    public void onKaktusClick(int position) {
        Intent intent = new Intent(this, JedenKaktusTL.class);
        //intent.putExtra("wybrany_kaktus", kaktusy.get(position) );

        Long tempId = kaktusy.get(position).getIdkaktus();
        intent.putExtra("id_kaktusa", tempId);

        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case 121:
                int id = item.getGroupId();
                displayMessage("Usunięto kaktus o nazwie: "+kaktusy.get(id).getNazwaKaktusa());
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                db.kaktusDAO().deleteByKaktusId(kaktusy.get(id).getIdkaktus());
                adapter.removeItem(id);
                return true;
            case 122:
                displayMessage("edytuj");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void displayMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_sortowanie_k,menu);

        return true;
    }
    int c1=0;
    int c2=0;
    int c3=0;
    int c4=0;
    int c5=0;
    int c6=0;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String komunikat=null;
        switch(item.getItemId()){

            case R.id.om_k_sort_nazwa:
                if(c1==0){
                    Collections.sort(kaktusy,Kaktus.KaktusNazwaAZComparaotr);
                    c1++;
                    komunikat = "A->Z";
                }else if(c1==1){
                    Collections.sort(kaktusy,Kaktus.KaktusNazwaZAComparaotr);
                    c1--;
                    komunikat = "Z->A";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po nazwie "+komunikat,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_k_sort_data:
                if(c2==0){
                    Collections.sort(kaktusy,Kaktus.KaktusDataRosnacoComparaotr);
                    c2++;
                    komunikat = "Rosnąco";
                }else if(c2==1){
                    Collections.sort(kaktusy,Kaktus.KaktusDataDescendingComparaotr);
                    c2--;
                    komunikat = "Malejąco";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po dacie "+komunikat,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_k_sort_gatunek:
                if(c3==0){
                    Collections.sort(kaktusy,Kaktus.KaktusGatunbekAZComparaotr);
                    c3++;
                    komunikat = "A->Z";
                }else if(c3==1){
                    Collections.sort(kaktusy,Kaktus.KaktusGatunbekZAComparaotr);
                    c3--;
                    komunikat = "Z->A";
                }

                Toast.makeText(getApplicationContext(),"Posortowano po gatunku "+komunikat,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_k_sort_miejscu:
                if(c4==0){
                    Collections.sort(kaktusy,Kaktus.KaktusMiejsceAZComparaotr);
                    c4++;
                    komunikat = "A->Z";
                }else if(c4==1){
                    Collections.sort(kaktusy,Kaktus.KaktusMiejsceZAComparaotr);
                    c4--;
                    komunikat = "Z->A";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po miejscu "+komunikat ,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_k_sort_data_k:
                if(c5==0){
                    Collections.sort(kaktusy,Kaktus.KaktusDataKRosnacoComparaotr);
                    c5++;
                    komunikat = "rosnąco";
                }else if(c5==1){
                    Collections.sort(kaktusy,Kaktus.KaktusDataKDescendingComparaotr);
                    c5--;
                    komunikat = "malejąco";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po dacie ost zakwitu "+komunikat ,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.om_k_sort_data_p:
                if(c6==0){
                    Collections.sort(kaktusy,Kaktus.KaktusDataPRosnacoComparaotr);
                    c6++;
                    komunikat = "rosnąco";
                }else if(c6==1){
                    Collections.sort(kaktusy,Kaktus.KaktusDataPDescendingComparaotr);
                    c6--;
                    komunikat = "malejąco";
                }
                Toast.makeText(getApplicationContext(),"Posortowano po dacie ost podlania "+komunikat ,Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
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
    }

    public String getListOfDziennik(){
        String result="\n";
        int puste=0;
        Date teraz = new java.util.Date();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        ArrayList<Notatka> notatki;
        for(Kaktus kaktus : kaktusy){
            System.out.println(kaktusy.size());
            System.out.println(kaktus.getNazwaKaktusa());
            notatki = new ArrayList<Notatka>(db.notatkaDAO().getAllNotatkiWithID(kaktus.getIdkaktus()));

            int liczban=0;

            if(notatki.size()>0) {
                for (Notatka notatka : notatki) {

                    if (getDifferenceDays(teraz,notatka.getDataDodania())>POWIADOMIENIE_DZIENNIK_ILE_DNI){
                        liczban++;
                    }
                }
                if(liczban==notatki.size()){
                    result+=kaktus.getNazwaKaktusa()+", \n";
                }
                continue;
            }else{
                result+=kaktus.getNazwaKaktusa()+", \n";
                puste++;
                continue;
            }


        }
        System.out.println(result.substring(0,result.length()-2));
        if(puste==kaktusy.size()){ //jesli masz nie wyswietlic notyfikacji
            return "BRAK";
        }
        return result.substring(0,result.length()-3)+".";
    }

    public long getDifferenceDays(Date d1, Date d2){
        long diff = d1.getTime() - d2.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    NotificationManagerCompat  notificationManagerCompat;

    public void SendNotificationPodlanie(String listaK){
        Notification notification = new NotificationCompat.Builder(this, MKaktusow.CHANNEL_PODLEWANIE)
                .setSmallIcon(R.drawable.ic_woda_24)
                .setContentTitle("Nie zapomnij podlać kaktusa")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Od miesiąca nie podlałeś kaktusów: "+ listaK))
                .build();

        notificationManagerCompat.notify(1,notification);
    }
    public void SendNotificationDziennik(String listaK){
        Notification notification = new NotificationCompat.Builder(this, MKaktusow.CHANNEL_DZIENNIK)
                .setSmallIcon(R.drawable.ic_calendar_24)
                .setContentTitle("Dawno nie odwiedziłeś kaktusów")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Od miesiąca nie dodałeś opisu multimedialnego do kaktusów: "+ listaK))
                .build();

        notificationManagerCompat.notify(2,notification);
    }

    public void NieOdwiedzoneKaktusy(){

    }

}