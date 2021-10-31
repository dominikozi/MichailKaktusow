package com.example.mkaktusow.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={Notatka.class,Kaktus.class}, version=7)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NotatkaDAO notatkaDAO();

    public abstract KaktusDAO kaktusDAO();


}
