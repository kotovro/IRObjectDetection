package com.example.scannerproto.anlaysis.helpers;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.scannerproto.MainActivity;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.objects.DetectedObject;

import java.util.List;
import java.util.Objects;
import java.util.Set;
//import com.google.mlkit.vision.face.Face;

public class DetectionBound {
    static int COLOR = Color.BLUE;
    static  int COLOR2 = Color.RED;

    public static Bitmap drawDetection(Bitmap frame, List<DetectedObject> obj,  List<ObjectDetectionResult> barcodes, List<Thing> objectsInfo, int rotationAngle) {
        Canvas canvas = new Canvas(frame);
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        int shiftX = 0;
        int shiftY = 0;
        if (obj != null) {
//            canvas.rotate(rotationAngle);
//            paint.setColor(COLOR2);
//            canvas.drawRect(obj.getBoundingBox(), paint);
//            shiftX = obj.getBoundingBox().left;
//            shiftY = obj.getBoundingBox().top;
        }
        int i = 0;
        for (ObjectDetectionResult code: barcodes) {
            Log.println(Log.VERBOSE, TAG, Boolean.toString(objectsInfo.get(0) == null));
            drawBarcode(canvas, code.getBarcode(), objectsInfo.get(i), shiftX, shiftY);
            i++;
        }
        return frame;
    }

    public static void drawBoundingBox(Canvas canvas, DetectedObject obj, Paint paint, int rotationAngle){
            canvas.rotate(rotationAngle);
            paint.setColor(COLOR2);
            canvas.drawRect(obj.getBoundingBox(), paint);
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

        canvas.drawText((Objects.equals(objectInfo, null) ? "Неизвестный объект": objectInfo.getId()),
                barcode.getCornerPoints()[0].x + shiftX + 20,  barcode.getCornerPoints()[0].y + shiftY + 20, paint);
        canvas.drawText((Objects.equals(objectInfo, null) ? "Нет информации": objectInfo.getInfo()),
                barcode.getCornerPoints()[0].x + shiftX,  barcode.getCornerPoints()[0].y + shiftY + 70, paint);
    }

    public static void drawObjectDetection(Bitmap frame, List<DetectedObject> detObjects, int rotation) {
        Canvas canvas = new Canvas(frame);
        Paint paint = new Paint();
        paint.setColor(COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (DetectedObject obj: detObjects) {
            canvas.drawRect(obj.getBoundingBox(), paint);
        }
    }

    public static void drawObjectWithData(Bitmap frame, List<Pair> pairs, List<Thing> info, int rotation) {
        int i = 0;
        Log.println(Log.VERBOSE, TAG, "There are " + Integer.toString(pairs.size()) + " objects");
        Log.println(Log.VERBOSE, TAG, "We have info about  " + info.size() + " objects");
        for (Pair pair: pairs) {
            Canvas canvas = new Canvas(frame);
            Paint paint = new Paint();
            paint.setColor(COLOR);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            int shiftX = 0;
            int shiftY = 0;
            drawBoundingBox(canvas, pair.getObj(), paint, rotation);
            shiftX = pair.getObj().getBoundingBox().left;
            shiftY = pair.getObj().getBoundingBox().top;
            drawBarcode(canvas, pair.getBarInfo(), info.get(i), shiftX, shiftY);

            i++;
        }
    }
}
