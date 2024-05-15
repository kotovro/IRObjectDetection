package com.example.scannerproto.anlaysis.helpers.drone_utils;

import android.graphics.Rect;

public class DroneMovements {
    private String forNeg;
    private String forZero;
    private String forPos;

    private Rect negRect;
    private Rect posRect;
    private Rect zeroRect;



    public DroneMovements(String forNeg, String forZero, String forPos, Rect negRect, Rect zeroRect, Rect posRect) {
        this.forNeg = forNeg;
        this.forZero = forZero;
        this.forPos = forPos;
        this.negRect = negRect;
        this.zeroRect = zeroRect;
        this.posRect = posRect;
    }

    public String getMessage(int intensity) {
        if (intensity == 0) {
            return forZero;
        } else if (intensity > 0) {
            return forPos;
        }
        return forNeg;
    }

    public Rect getRect(int intensity) {
        if (intensity == 0) {
            return zeroRect;
        } else if (intensity > 0) {
            return posRect;
        }
        return negRect;
    }
}
