package com.example.scannerproto.anlaysis.helpers;

import androidx.annotation.Nullable;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.objects.ObjectDetector;

import java.util.Objects;


public class ObjectDetectionResult implements Comparable<ObjectDetectionResult>{
    private final float EPSILON = 10;
    private String barcodeMessage = " ";


    private Barcode barcode;

    public Barcode getBarcode() {
        return barcode;
    }

    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
    }

    public IObjectInfoGetter infoGetter;

    public ObjectDetectionResult(IObjectInfoGetter infoGetter) {
        this.infoGetter = infoGetter;
    }

    public void setBarcodeMessage(String message) {
        this.barcodeMessage = message;
    }
    public String getBarcodeMessage() {
        return this.barcodeMessage;
    }

    @Override
    public int compareTo(ObjectDetectionResult o) {
        int heightDiff = Math.abs(this.barcode.getBoundingBox().height() - o.barcode.getBoundingBox().height());
        int widthDiff = Math.abs(this.barcode.getBoundingBox().width() - o.barcode.getBoundingBox().width());
        int leftDiff = Math.abs(this.barcode.getBoundingBox().left - o.barcode.getBoundingBox().left);
        int topDiff = Math.abs(this.barcode.getBoundingBox().top - o.barcode.getBoundingBox().top);


        if (this.barcodeMessage.equals(o.barcodeMessage)
                && (heightDiff < EPSILON)
                && (widthDiff < EPSILON)
                && (leftDiff < EPSILON)
                && (topDiff < EPSILON)) {
            return 0;
        }
        return 1;
    }

}
