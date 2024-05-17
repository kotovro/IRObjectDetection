package com.example.scannerproto.anlaysis.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class DetectionBound {
    static int extractionRate = 14;

    public static Bitmap extractBitmap(Bitmap bitmap) {
        int startX = 2 * bitmap.getWidth() / 9;
        int newWidthStart = (bitmap.getWidth() - startX) / extractionRate + startX;
        int newWidthEnd = (extractionRate - 1) * (bitmap.getWidth() - startX) / extractionRate + startX;

        Bitmap res = Bitmap.createBitmap(bitmap, newWidthStart, 0, newWidthEnd - newWidthStart, bitmap.getHeight());
        return res;
    }
    public static Bitmap prepareBitmap(Bitmap bitmap)
    {
        Bitmap comboBitmap;

        int width, height;

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        comboBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(comboBitmap);
        Rect rectInR = new Rect(0, 0, -bitmap.getWidth() * 2 / extractionRate + bitmap.getWidth(), bitmap.getHeight());
        Rect rectOutR = new Rect(0, 0, width / 2, comboImage.getHeight());

        Rect rectInL = new Rect(bitmap.getWidth() * 2 / extractionRate, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect rectOutL = new Rect(width / 2, 0, width, comboImage.getHeight());

        comboImage.drawBitmap(bitmap, rectInR, rectOutR, null);
        comboImage.drawBitmap(bitmap, rectInL, rectOutL , null);
        return comboBitmap;

    }
}
