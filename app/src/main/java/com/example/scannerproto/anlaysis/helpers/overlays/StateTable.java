package com.example.scannerproto.anlaysis.helpers.overlays;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scannerproto.anlaysis.helpers.drone_utils.DroneActionState;

public class StateTable {
    String[] messages = new String[8];
    Paint textPaint;
    Paint backPaint;

    public StateTable() {
        textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);

        backPaint = new Paint();
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setAlpha(127);

        messages[0] = "Движение вверх";
        messages[1] = "Движение вниз";
        messages[2] = "Движение влево";
        messages[3] = "Движение вправо";
        messages[4] = "Движение вперёд";
        messages[5] = "Движение назад";
        messages[6] = "Поворот по часовой";
        messages[7] ="Поворот против часовой";
    }

    public void drawTable(Canvas canvas, DroneActionState[] states) {
        int textSize = (int) textPaint.getTextSize();
        Rect rect = new Rect();
        for (int i = 0; i < messages.length; i++) {
            rect.set(700, 310 + i * textSize * 12 / 10, 1024, 310 + (i + 1) * textSize * 12 / 10);
            drawComponent(canvas, textPaint, backPaint, rect, messages[i], states[i]);
        }
    }

    private void drawComponent(Canvas canvas, Paint textPaint, Paint backPaint, Rect rect, String text, DroneActionState state) {
        int textX = rect.left + 5;
        int textY = rect.bottom - (rect.bottom - rect.top - (int) textPaint.getTextSize()) / 2;

        backPaint.setColor(state.getColor());
        canvas.drawRect(rect, backPaint);
        canvas.drawText(text, textX, textY, textPaint);
    }
}
