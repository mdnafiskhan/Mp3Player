package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by mdnafiskhan on 18/03/2018.
 */

@Entity
public class EqualiserEntity {

    @PrimaryKey
    @NonNull
    int id;

    @ColumnInfo(name = "band0")
    Short Band0;

    @ColumnInfo(name = "band1")
    Short Band1;

    @ColumnInfo(name = "band2")
    Short Band2;

    @ColumnInfo(name = "band3")
    Short Band3;

    @ColumnInfo(name = "band4")
    Short Band4;

    @ColumnInfo(name = "bassboost")
    Short BassBoost;

    @ColumnInfo(name = "visualizer")
    Short Visualizer;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public Short getBand0() {
        return Band0;
    }

    public void setBand0(Short band0) {
        Band0 = band0;
    }

    public Short getBand1() {
        return Band1;
    }

    public void setBand1(Short band1) {
        Band1 = band1;
    }

    public Short getBand2() {
        return Band2;
    }

    public void setBand2(Short band2) {
        Band2 = band2;
    }

    public Short getBand3() {
        return Band3;
    }

    public void setBand3(Short band3) {
        Band3 = band3;
    }

    public Short getBand4() {
        return Band4;
    }

    public void setBand4(Short band4) {
        Band4 = band4;
    }

    public Short getBassBoost() {
        return BassBoost;
    }

    public void setBassBoost(Short bassBoost) {
        BassBoost = bassBoost;
    }

    public Short getVisualizer() {
        return Visualizer;
    }

    public void setVisualizer(Short visualizer) {
        Visualizer = visualizer;
    }
}
