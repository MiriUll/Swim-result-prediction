package org.swimpredictor.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities =  {DataSample.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{
    public abstract SampleDao sampleDao();
}
