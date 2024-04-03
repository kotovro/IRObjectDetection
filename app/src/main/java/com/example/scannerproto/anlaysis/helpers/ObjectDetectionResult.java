package com.example.scannerproto.anlaysis.helpers;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.objects.ObjectDetector;


public class ObjectDetectionResult {
    private String barcodeMessage = " ";


    private Barcode barcode;
    public Barcode getBarcode() {
        return barcode;
    }

    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
    }


    public void setBarcodeMessage(String message) {
        this.barcodeMessage = message;
    }
    public String getBarcodeMessage() {
        String temp = String.copyValueOf(barcodeMessage.toCharArray());
        return temp;
    }

}
