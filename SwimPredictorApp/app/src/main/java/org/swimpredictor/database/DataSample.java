package org.swimpredictor.database;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

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

    @ColumnInfo(name = "100m time")
    double time100m;

    @ColumnInfo(name = "200m time")
    double time200m;

    public DataSample(@NonNull String description, int gender, int age, int training_age, double time50m, double time100m, double time200m) {
        this.description = description;
        this.gender = gender;
        this.age = age;
        this.training_age = training_age;
        this.time50m = time50m;
        this.time100m = time100m;
        this.time200m = time200m;
    }

    @NotNull
    public String toString() {
        return description + "(gender: " + (gender == 1 ? "female" : "male") + ", age: " + age +
                ", training age: " + training_age + ") swims:\n" + "50m: " + time50m + ", 100m: " +
                time100m + ", 200m: " + time200m;
    }
}
