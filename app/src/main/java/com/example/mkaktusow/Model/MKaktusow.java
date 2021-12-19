package com.example.mkaktusow.Model;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class MKaktusow extends Application {

    public static final String CHANNEL_PODLEWANIE = "podlewanie";
    public static final String CHANNEL_DZIENNIK= "dziennik";



    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production").allowMainThreadQueries().fallbackToDestructiveMigration().build();


        List<Gatunek> gatunkiCurr = new ArrayList<>();
        gatunkiCurr= db.gatunekDAO().getAllGatunki();
        if (gatunkiCurr.size() == 0) {
            List<Gatunek> gatunki = new ArrayList<Gatunek>();
            db.gatunekDAO().insertAll(new Gatunek("Opuncja drobnokolczasta", 14));
            db.gatunekDAO().insertAll(new Gatunek("Opuncja figowa", 14));
            db.gatunekDAO().insertAll(new Gatunek("Wielomlecz trójżebrowy", 14));
            db.gatunekDAO().insertAll(new Gatunek("Cereus repandus", 14));
            db.gatunekDAO().insertAll(new Gatunek("Echinokaktus Grusonii", 14));
            db.gatunekDAO().insertAll(new Gatunek("Echinopsis Eyriesa", 14));
            db.gatunekDAO().insertAll(new Gatunek("Echinopsis spachiana", 14));
            db.gatunekDAO().insertAll(new Gatunek("Mammillaria Haw.", 14));
            db.gatunekDAO().insertAll(new Gatunek("Jazgrza Williamsa", 14));
            db.gatunekDAO().insertAll(new Gatunek("Ferokaktus", 14));
            db.gatunekDAO().insertAll(new Gatunek("Gymnocalycium Monvillei", 14));
        }

    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel podlewanie = new NotificationChannel(
                    CHANNEL_PODLEWANIE,
                    "Podlewanie",
                    NotificationManager.IMPORTANCE_LOW
            );
            podlewanie.setDescription("info o podlewaniu");

            NotificationChannel dziennik = new NotificationChannel(
                    CHANNEL_DZIENNIK,
                    "Dziennik",
                    NotificationManager.IMPORTANCE_LOW
            );
            dziennik.setDescription("info o opisach multimedialnych");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(podlewanie);
            manager.createNotificationChannel(dziennik);

        }
    }


}
