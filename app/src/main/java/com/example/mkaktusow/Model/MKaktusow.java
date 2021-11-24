package com.example.mkaktusow.Model;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MKaktusow extends Application {

    public static final String CHANNEL_PODLEWANIE = "podlewanie";
    public static final String CHANNEL_DZIENNIK= "dziennik";



    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
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
