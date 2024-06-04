package com.example.scannerproto.anlaysis.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.scannerproto.MainActivity;
import com.example.scannerproto.anlaysis.helpers.detection.ObjectDetectionResult;
import com.example.scannerproto.anlaysis.helpers.db.Thing;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetectionBound {
    static final int COLOR = Color.WHITE;
    static final int COLOR2 = Color.RED;


    static final int OFFSET = 50;


    static final int COLOR3 = Color.BLACK;
    static int extractionRate = 14;

    public static Bitmap drawDetection(Bitmap frame, Iterable<ObjectDetectionResult> barcodes, Map<ObjectDetectionResult, Integer> barcodeList, List<Thing> curInfo, int rotationAngle) {
        Canvas canvas = new Canvas(frame);
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        int i = 0;
        for (ObjectDetectionResult code: barcodes) {
            drawBarcode(canvas, code.getBarcode(), curInfo.get(i), barcodeList.get(code));
            i++;
        }
        return frame;
    }
    public static void drawBarcode(Canvas canvas, Barcode barcode, Thing objectInfo, int lifeTime) {
        Paint paint = new Paint();
        boolean isUnKnown = Objects.equals(objectInfo, null);
        paint.setColor(isUnKnown ? COLOR2 : COLOR);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(Math.min(150, 150 * 2 * lifeTime / MainActivity.DECAY_TIME));


        Path path = new Path();
        Point[] points = barcode.getCornerPoints();
        path.moveTo(points[0].x - OFFSET, points[0].y - OFFSET);
        path.lineTo(points[1].x + OFFSET, points[1].y - OFFSET);
        path.lineTo(points[2].x + OFFSET, points[2].y + OFFSET);
        path.lineTo(points[3].x - OFFSET, points[3].y + OFFSET);
        path.lineTo(points[0].x - OFFSET, points[0].y - OFFSET);

        canvas.drawPath(path, paint);
//
//        canvas.drawLine(barcode.getCornerPoints()[0].x, barcode.getCornerPoints()[0].y, barcode.getCornerPoints()[1].x, barcode.getCornerPoints()[1].y , paint);
//        canvas.drawLine(barcode.getCornerPoints()[1].x, barcode.getCornerPoints()[1].y, barcode.getCornerPoints()[2].x, barcode.getCornerPoints()[2].y, paint);
//        canvas.drawLine(barcode.getCornerPoints()[2].x, barcode.getCornerPoints()[2].y, barcode.getCornerPoints()[3].x, barcode.getCornerPoints()[3].y, paint);
//        canvas.drawLine(barcode.getCornerPoints()[3].x, barcode.getCornerPoints()[3].y, barcode.getCornerPoints()[0].x, barcode.getCornerPoints()[0].y, paint);
//        paint.setTextSize(50);

        paint.setColor(isUnKnown ? COLOR2 : COLOR3);
        paint.setAlpha(Math.min(150, 150 * 2 * lifeTime / MainActivity.DECAY_TIME));

        int fontSize = (points[1].x - points[0].x) / 5;
        paint.setTextSize(fontSize);
        paint.setTextAlign(Paint.Align.LEFT);



        int separate = 12;


        canvas.drawText((isUnKnown ? "Неизвестный объект": objectInfo.getName()),
                barcode.getCornerPoints()[0].x - 40,  barcode.getCornerPoints()[0].y, paint);

        for (int i = 0; i < (objectInfo.getInfo().length() / separate) + 1; i++) {
            canvas.drawText((isUnKnown ? "Нет информации": objectInfo.getInfo()),
                    i * separate,  Math.min((i + 1) * separate, objectInfo.getInfo().length()),
                    barcode.getCornerPoints()[0].x - 30,  barcode.getCornerPoints()[0].y + 50 + i * fontSize, paint);
        }

    }

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