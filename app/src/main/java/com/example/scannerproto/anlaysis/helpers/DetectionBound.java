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
    static  int COLOR2 = Color.RED;

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
    public static Bitmap drawDetection(Bitmap bitmap, Barcode barcode, String barcodeContents, int angle) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);


        canvas.rotate(angle);
        paint.setColor(COLOR2);
        canvas.drawLine(barcode.getCornerPoints()[0].x, barcode.getCornerPoints()[0].y, barcode.getCornerPoints()[1].x, barcode.getCornerPoints()[1].y, paint);
        canvas.drawLine(barcode.getCornerPoints()[1].x, barcode.getCornerPoints()[1].y, barcode.getCornerPoints()[2].x, barcode.getCornerPoints()[2].y, paint);
        canvas.drawLine(barcode.getCornerPoints()[2].x, barcode.getCornerPoints()[2].y, barcode.getCornerPoints()[3].x, barcode.getCornerPoints()[3].y, paint);
        canvas.drawLine(barcode.getCornerPoints()[3].x, barcode.getCornerPoints()[3].y, barcode.getCornerPoints()[0].x, barcode.getCornerPoints()[0].y, paint);
        paint.setTextSize(50);
        paint.setColor(COLOR);
        Thing p = null;


        canvas.drawText((Objects.equals(barcodeContents, "") ? "Неизвестный объект": barcodeContents),
                barcode.getCornerPoints()[0].x + 10,  barcode.getCornerPoints()[0].y + 20, paint);
//            canvas.drawText((Objects.equals(barcodeContents, "") ? "" : p.getInfo()),
//                    barcodeBounds.right + 10, barcodeBounds.top + 160, paint);
        canvas.rotate(-angle);
        return bitmap;
    }
}
