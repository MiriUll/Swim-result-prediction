package org.swimpredictor.database;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import org.swimpredictor.ui.home.HomeFragment;

@Entity
public class DataSample {

    @PrimaryKey @NonNull
    String description;

    @ColumnInfo(name = "Gender")
    int gender;

    @ColumnInfo(name = "Age")
    int age;

    @ColumnInfo(name = "Training Age")
    int training_age;

    @ColumnInfo(name = "50m time")
    double time50m;
    String time50_string;

    @ColumnInfo(name = "100m time")
    double time100m;
    String time100m_string;

    @ColumnInfo(name = "200m time")
    double time200m;
    String time200m_string;

    public DataSample(@NonNull String description, int gender, int age, int training_age, double time50m, double time100m, double time200m) {
        this.description = description;
        this.gender = gender;
        this.age = age;
        this.training_age = training_age;
        this.time50m = time50m;
        this.time100m = time100m;
        this.time200m = time200m;
        this.time50_string = HomeFragment.toHumanReadbleTime((long) (time50m*1000));
        this.time100m_string = HomeFragment.toHumanReadbleTime((long) (time100m*1000));
        this.time200m_string = HomeFragment.toHumanReadbleTime((long) (time200m*1000));
    }

    @NotNull
    public String toString() {
        return "Swimmer: " +description +'\n'
                + "gender: " + (gender == 1 ? "female" : "male") + ", age: " + age +
                ", training age: " + training_age + "\nswims:\n" + "50m: " + time50_string + ", 100m: " +
                time100m_string + ", 200m: " + time200m_string;
    }

    public String[] toStringArray(){
        String[] ar = {description, time50_string, time100m_string, time200m_string};
        return ar;
    }

    public int getArrayLength(){
        return 5;
    }
}
