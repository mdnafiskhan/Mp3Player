package com.developmentforfun.mdnafiskhan.mp3player.Models;

/**
 * Created by mdnafiskhan on 18/03/2018.
 */

public class EqualizerBands {

    short band0,band1,band2,band3,band4;
    short bassBoost;
    short visualizer;
    int customEqualizerValue;

    public EqualizerBands() {
        super();
        this.band0=0;
        this.band1=0;
        this.band2=0;
        this.band3=0;
        this.band4=0;
        this.bassBoost=0;
        this.visualizer=0;
        this.customEqualizerValue=0;
    }

    public short getBand0() {
        return band0;
    }

    public void setBand0(short band0) {
        this.band0 = band0;
    }

    public short getBand1() {
        return band1;
    }

    public void setBand1(short band1) {
        this.band1 = band1;
    }

    public short getBand2() {
        return band2;
    }

    public void setBand2(short band2) {
        this.band2 = band2;
    }

    public short getBand3() {
        return band3;
    }

    public void setBand3(short band3) {
        this.band3 = band3;
    }

    public short getBand4() {
        return band4;
    }

    public void setBand4(short band4) {
        this.band4 = band4;
    }

    public short getBassBoost() {
        return bassBoost;
    }

    public void setBassBoost(short bassBoost) {
        this.bassBoost = bassBoost;
    }

    public short getVisualizer() {
        return visualizer;
    }

    public void setVisualizer(short visualizer) {
        this.visualizer = visualizer;
    }

    public int getCustomEqualizerValue() {
        return customEqualizerValue;
    }

    public void setCustomEqualizerValue(int customEqualizerValue) {
        this.customEqualizerValue = customEqualizerValue;
    }
}
