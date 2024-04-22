package com.example.scannerproto.anlaysis.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.List;
import java.util.Objects;
//import com.google.mlkit.vision.face.Face;

public class DetectionBound {
    static int COLOR = Color.BLUE;
    static  int COLOR2 = Color.RED;

    public static Bitmap drawDetection(Bitmap frame,  List<ObjectDetectionResult> barcodes, List<Thing> curInfo, int rotationAngle) {
        Canvas canvas = new Canvas(frame);
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        int shiftX = 0;
        int shiftY = 0;
//        if (obj != null) {
//            canvas.rotate(rotationAngle);
//            paint.setColor(COLOR2);
//            canvas.drawRect(obj.getBoundingBox(), paint);
//            shiftX = obj.getBoundingBox().left;
//            shiftY = obj.getBoundingBox().top;
//        }
        int i = 0;
        for (ObjectDetectionResult code: barcodes) {
//            Log.println(Log.VERBOSE, TAG, Boolean.toString(objectsInfo.get(0) == null));
            drawBarcode(canvas, code.getBarcode(), curInfo.get(i), shiftX, shiftY);
            i++;
        }
        return frame;
    }
    public static void drawBarcode(Canvas canvas, Barcode barcode, Thing objectInfo, int shiftX, int shiftY) {
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

        canvas.drawText((Objects.equals(objectInfo, "") ? "Неизвестный объект": objectInfo.getId()),
                barcode.getCornerPoints()[0].x + shiftX + 20,  barcode.getCornerPoints()[0].y + shiftY + 20, paint);
        canvas.drawText((Objects.equals(objectInfo, "") ? "Нет информации": objectInfo.getInfo()),
                barcode.getCornerPoints()[0].x + shiftX,  barcode.getCornerPoints()[0].y + shiftY + 70, paint);
    }

    public static Bitmap extractBitmap(Bitmap bitmap) {
        int newWidthStart = bitmap.getWidth() / 4;
        int newWidthEnd = 3 * bitmap.getWidth() / 4;

        Bitmap res = Bitmap.createBitmap(bitmap, newWidthStart, 0, newWidthEnd - newWidthStart, bitmap.getHeight());
        return res;
    }
    public static Bitmap mergeBitmap(Bitmap fr, Bitmap sc)
    {

        Bitmap comboBitmap;

        int width, height;

        width = fr.getWidth() + sc.getWidth();
        height = fr.getHeight();

        comboBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(comboBitmap);


        comboImage.drawBitmap(fr, 0f, 0f, null);
        comboImage.drawBitmap(sc, fr.getWidth(), 0f , null);
        return comboBitmap;

    }
}
