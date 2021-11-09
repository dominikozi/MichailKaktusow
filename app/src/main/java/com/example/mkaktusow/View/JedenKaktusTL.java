package com.example.mkaktusow.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mkaktusow.Controller.FragmentAdapter;
import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Model.AppDatabase;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class JedenKaktusTL extends AppCompatActivity implements NotatkaAdapter.OnNotatkaListener{


    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentadapter;

    Long kaktusid;

    Kaktus kaktus;

    List<Notatka> notatki;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeden_kaktus_tl);



        Long idKaktusa = getIntent().getExtras().getLong("id_kaktusa");
        kaktusid=idKaktusa;

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        kaktus = db.kaktusDAO().getKaktusWithID(idKaktusa);
        notatki = db.notatkaDAO().getAllNotatkiWithID(kaktus.getIdkaktus());

        getSupportActionBar().setTitle("Kaktus: " + kaktus.getNazwaKaktusa());

        tabLayout=findViewById(R.id.jedenkaktustl_tablayout);
        viewPager2=findViewById(R.id.jedenkaktustl_viewpager2);

        FragmentManager fm = getSupportFragmentManager();
        fragmentadapter = new FragmentAdapter(fm,getLifecycle());
        viewPager2.setAdapter(fragmentadapter);

        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Mapa"));
        tabLayout.addTab(tabLayout.newTab().setText("Zdjecia"));
        tabLayout.addTab(tabLayout.newTab().setText("Notatki"));

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.om_k_podlanie:
                Toast.makeText(this,"podlanie", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.om_k_kwitniecie:
                Toast.makeText(this,"kwitniecie", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}