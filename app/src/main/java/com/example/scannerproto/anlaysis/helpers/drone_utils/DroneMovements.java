package com.example.scannerproto.anlaysis.helpers.drone_utils;

import android.graphics.Color;
import android.graphics.Rect;

public class DroneMovements {
    private String forNeg;
    private String forZero;
    private String forPos;

    private Rect negRect;
    private Rect posRect;
    private Rect zeroRect;

    private Color nRectCol;
    private Color zRectCol;
    private Color pRectCol;

    public static Rect left = new Rect(10, 375, 100, 375 + 15 * 12 / 10);
    public static Rect right = new Rect(600, 375, 700, 375 + 15 * 12 / 10);
    public static Rect top = new Rect(275, 250, 350, 250 + 15 * 12 / 10);
    public static Rect bottom = new Rect(275, 500, 350, 500 + 15 * 12 / 10);



    public DroneMovements(String forNeg, String forZero, String forPos, Rect negRect, Rect zeroRect, Rect posRect, int nRectCol, int zRectCol, int pRectCol) {
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
