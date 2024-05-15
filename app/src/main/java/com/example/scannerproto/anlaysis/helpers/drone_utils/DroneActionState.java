package com.example.scannerproto.anlaysis.helpers.drone_utils;

import android.animation.ArgbEvaluator;
import android.graphics.Color;

import com.google.android.material.animation.ArgbEvaluatorCompat;

public class DroneActionState {


    private int intensity = 0;

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    DroneActionState(int intensity) {
        this.intensity = intensity;
    }



    public Integer getColor() {
        int color1 = Color.parseColor("grey");
        int color2 = intensity > 0 ? Color.parseColor("green") : Color.parseColor("red");
        return intensity == 0 ? color1 : color2;
    }
}
