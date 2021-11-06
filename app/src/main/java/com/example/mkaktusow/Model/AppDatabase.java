package com.example.mkaktusow.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mkaktusow.Controller.Converters;

@Database(entities={Notatka.class,Kaktus.class}, version=14)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract NotatkaDAO notatkaDAO();

    public abstract KaktusDAO kaktusDAO();


}
