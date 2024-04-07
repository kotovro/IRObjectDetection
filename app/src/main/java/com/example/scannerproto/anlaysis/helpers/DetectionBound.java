package com.example.scannerproto.anlaysis.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scannerproto.MainActivity;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.objects.DetectedObject;

import java.util.List;
import java.util.Objects;
import java.util.Set;
//import com.google.mlkit.vision.face.Face;

public class DetectionBound {
    static int COLOR = Color.BLUE;
    static  int COLOR2 = Color.RED;

    public static Bitmap drawDetection(Bitmap frame, DetectedObject obj, List<ObjectDetectionResult> barcodes, int rotationAngle) {
        Canvas canvas = new Canvas(frame);
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        int shiftX = 0;
        int shiftY = 0;
        if (obj != null) {
            canvas.rotate(rotationAngle);
            paint.setColor(COLOR2);
            canvas.drawRect(obj.getBoundingBox(), paint);
            shiftX = obj.getBoundingBox().left;
            shiftY = obj.getBoundingBox().top;
        }

        for (ObjectDetectionResult code: barcodes) {
            drawBarcode(canvas, code.getBarcode(), code.getBarcodeMessage(), shiftX, shiftY);
        }
        return frame;
    }
    public static void drawBarcode(Canvas canvas, Barcode barcode, String barcodeContents,int shiftX, int shiftY) {
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawLine(barcode.getCornerPoints()[0].x + shiftX, barcode.getCornerPoints()[0].y + shiftY, barcode.getCornerPoints()[1].x + shiftX, barcode.getCornerPoints()[1].y + shiftY, paint);
        canvas.drawLine(barcode.getCornerPoints()[1].x + shiftX, barcode.getCornerPoints()[1].y + shiftY, barcode.getCornerPoints()[2].x + shiftX, barcode.getCornerPoints()[2].y + shiftY, paint);
        canvas.drawLine(barcode.getCornerPoints()[2].x + shiftX, barcode.getCornerPoints()[2].y + shiftY, barcode.getCornerPoints()[3].x + shiftX, barcode.getCornerPoints()[3].y + shiftY, paint);
        canvas.drawLine(barcode.getCornerPoints()[3].x + shiftX, barcode.getCornerPoints()[3].y + shiftY, barcode.getCornerPoints()[0].x + shiftX, barcode.getCornerPoints()[0].y + shiftY, paint);
        paint.setTextSize(50);
        paint.setColor(COLOR);

        canvas.drawText((Objects.equals(barcodeContents, "") ? "Неизвестный объект": barcodeContents),
                barcode.getCornerPoints()[0].x + shiftX + 10,  barcode.getCornerPoints()[0].y + shiftY + 20, paint);
    }
}
