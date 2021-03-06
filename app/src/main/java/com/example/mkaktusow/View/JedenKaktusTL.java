package com.example.mkaktusow.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.mkaktusow.Controller.FragmentAdapter;
import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JedenKaktusTL extends AppCompatActivity implements NotatkaAdapter.OnNotatkaListener, DatePickerDialog.OnDateSetListener {


    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentadapter;
    AppDatabase db;
    Long kaktusid;

    Kaktus kaktus;



    List<Notatka> notatki;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeden_kaktus_tl);

        Long idKaktusa = getIntent().getExtras().getLong("id_kaktusa");
        kaktusid=idKaktusa;

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        kaktus = db.kaktusDAO().getKaktusWithID(idKaktusa);
        notatki = db.notatkaDAO().getAllNotatkiWithID(kaktus.getIdkaktus());

        getSupportActionBar().setTitle(kaktus.getNazwaKaktusa());

        tabLayout=findViewById(R.id.jedenkaktustl_tablayout);
        viewPager2=findViewById(R.id.jedenkaktustl_viewpager2);

        FragmentManager fm = getSupportFragmentManager();
        fragmentadapter = new FragmentAdapter(fm,getLifecycle());
        viewPager2.setAdapter(fragmentadapter);
        viewPager2.setUserInputEnabled(false);

        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Mapa"));
        tabLayout.addTab(tabLayout.newTab().setText("Zdjecia i filmy"));
        tabLayout.addTab(tabLayout.newTab().setText("Teksty i audio"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    public Long getkaktusIDzaktywnosci(){
        return kaktusid;
    }

    public Kaktus getCurrentKaktusZActivity(){
        return kaktus;
    }

    public  List<Notatka> getCurrentNotatkiZActivity(){
        return notatki;
    }

    @Override
    public void onNotatkaClick(int position) {
        Intent intent = new Intent(this, JednaNotatka.class);
        Long tempId = notatki.get(position).getIdnotatka();

        intent.putExtra("id_notatki",tempId);

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_kaktus,menu);

        return true;
    }

    int ktoradata; // 1-datapodlania, 0-datakwitniecia
    Date datetemp;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     //   AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        switch(item.getItemId()){
            case R.id.om_k_podlanie:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker podlanie");
                ktoradata=1;
                return true;
            case R.id.om_k_kwitniecie:
                DialogFragment datePicker2 = new DatePickerFragment();
                datePicker2.show(getSupportFragmentManager(),"date picker podlanie");
                ktoradata=0;
                return true;
            case R.id.om_k_usun:

                AlertDialog.Builder alertPotwierdzenie = new AlertDialog.Builder(this);
                alertPotwierdzenie.setCancelable(true);
                alertPotwierdzenie.setTitle("Usuni??cie kaktusa");
                alertPotwierdzenie.setMessage("Czy potwierdzasz usuni??cie kaktusa?");
                alertPotwierdzenie.setPositiveButton("Potwierdz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.kaktusDAO().deleteByKaktusId(kaktus.getIdkaktus());
                        Toast.makeText(getApplicationContext(),"Usunieto kaktusa "+kaktus.getNazwaKaktusa()+".",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Kaktusy.class);

                        startActivity(intent);

                    }
                });
                alertPotwierdzenie.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Usuniecie kaktusa anulowane.",Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog alertDialog = alertPotwierdzenie.create();
                alertDialog.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        datetemp = calendar.getTime();
     //   AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        if(ktoradata==1){
            Toast.makeText(this,"Data ost. podlania ustawiona: "+df.format("yyyy-MM-dd hh:mm", datetemp) , Toast.LENGTH_SHORT).show();

            db.kaktusDAO().updateDataOstPodlania(datetemp, kaktus.getIdkaktus());
        }else if (ktoradata==0){
            Toast.makeText(this,"Data ost. kwitniecia ustawiona: "+df.format("yyyy-MM-dd hh:mm", datetemp) , Toast.LENGTH_SHORT).show();
            db.kaktusDAO().updateDataOstKwitniecia(datetemp, kaktus.getIdkaktus());
        }


    }
}