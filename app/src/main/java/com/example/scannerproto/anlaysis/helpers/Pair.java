package com.example.scannerproto.anlaysis.helpers;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.objects.DetectedObject;

public class Pair {
    private DetectedObject obj;
    private Barcode barInfo;
    public Pair(DetectedObject obj, Barcode info) {
        this.obj = obj;
        this.barInfo = info;
    }

    public DetectedObject getObj() {
        return obj;
    }

    public Barcode getBarInfo() {
        return barInfo;
    }
}
