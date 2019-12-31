package org.swimpredictor.database;

import java.util.List;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;

@Dao
public interface SampleDao {

    @Query("SELECT * FROM datasample")
    List<DataSample> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inset(DataSample... dataSamples);

    @Delete
    void delete(DataSample dataSample);
}
