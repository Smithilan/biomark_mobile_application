package com.example.form;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.form.ModelBuildingData;


@Database(entities = {ModelBuildingData.class}, version = 2)
public abstract class BioMarkDatabase extends RoomDatabase {
    private static volatile BioMarkDatabase instance;
    public abstract ModelBuildingDao modelBuildingDao();

    public static synchronized BioMarkDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BioMarkDatabase.class, "biomark_database")
                    .fallbackToDestructiveMigration() // Handle migrations if necessary
                    .build();
        }
        return instance;
    }
}
