package com.example.scannerproto.anlaysis.helpers.overlays;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scannerproto.anlaysis.helpers.drone_utils.DroneActionState;

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

    public void drawTable(Canvas canvas, DroneActionState[] states) {
        int textSize = (int) textPaint.getTextSize();
        Rect rect = new Rect();
        for (int i = 0; i < messages.length; i++) {
            rect.set(400, 310 + i * textSize * 12 / 10, 1024, 310 + (i + 1) * textSize * 12 / 10);
            drawComponent(canvas, textPaint, backPaint, rect, messages[i], states[i]);
        }
    }

    private void drawComponent(Canvas canvas, Paint textPaint, Paint backPaint, Rect rect, String text, DroneActionState state) {
        int textX = rect.left + 5;
        int textY = rect.bottom - (rect.bottom - rect.top - (int) textPaint.getTextSize()) / 2;

        backPaint.setColor(state.getColor());
        backPaint.setAlpha(127);
        canvas.drawRect(rect, backPaint);
        canvas.drawText(text, textX, textY, textPaint);
    }
}
