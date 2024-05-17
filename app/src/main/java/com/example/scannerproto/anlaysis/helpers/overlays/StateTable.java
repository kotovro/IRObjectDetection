package com.example.scannerproto.anlaysis.helpers.overlays;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class StateTable {
    String[] messages = new String[4];
    Paint textPaint;
    Paint backPaint;

    public StateTable() {
        textPaint = new Paint();
        textPaint.setTextSize(22);
        textPaint.setColor(Color.WHITE);

        backPaint = new Paint();
        backPaint.setStyle(Paint.Style.FILL);

        messages[0] = "Движение влево";
        messages[1] = "Движение вперёд";
        messages[2] = "Движение вверх";
        messages[3] = "Поворот по часовой";
    }

    private void drawComponent(Canvas canvas, Paint textPaint, Paint backPaint, Rect rect, String text) {
        int textX = rect.left + 5;
        int textY = rect.bottom - (rect.bottom - rect.top - (int) textPaint.getTextSize()) / 2;

        backPaint.setAlpha(127);
        canvas.drawRect(rect, backPaint);
        canvas.drawText(text, textX, textY, textPaint);
    }
}
