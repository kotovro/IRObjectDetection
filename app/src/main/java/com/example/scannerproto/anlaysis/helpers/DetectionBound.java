package com.example.scannerproto.anlaysis.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scannerproto.MainActivity;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.Objects;
//import com.google.mlkit.vision.face.Face;

public class DetectionBound {
    static int COLOR = Color.BLUE;

//    public static Bitmap drawDetection(Bitmap bitmap, String id, Barcode barcode) {
//        if (barcode == null) {
//            return bitmap;
//        }
//        int intID = -1;
//        try {
//            intID = Integer.parseInt(id);
//        } catch (Exception e) {
//
//        }
//
//        return drawDetection(bitmap, intID, barcode.getBoundingBox());
//    }
    public static Bitmap drawDetection(Bitmap bitmap, String barcodeContents, Rect barcodeBounds) {

        int intID = -1;
        try {
            intID = Integer.parseInt(barcodeContents);
        } catch (Exception e) {

        }
        if (intID > 0) {
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(COLOR);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);


            assert barcodeBounds != null;
            canvas.drawRect(barcodeBounds, paint);

            paint.setTextSize(50);
            paint.setColor(COLOR);
            Thing p = null;
//            if (id > 0) {
//                p = MainActivity.base.getThing(id);
//            }

            canvas.drawText((Objects.equals(barcodeContents, "") ? barcodeContents : "Неизвестный объект"),
                    barcodeBounds.right + 10, barcodeBounds.top + 20, paint);
//            canvas.drawText((Objects.equals(barcodeContents, "") ? "" : p.getInfo()),
//                    barcodeBounds.right + 10, barcodeBounds.top + 160, paint);
        }

        return bitmap;
    }
}
