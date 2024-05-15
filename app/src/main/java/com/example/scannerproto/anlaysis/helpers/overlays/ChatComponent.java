package com.example.scannerproto.anlaysis.helpers.overlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ChatComponent {
    private static int lifeTime = 25;
    private String text;
    private int time;

    public ChatComponent(String text) {
        this.text = text;
        this.time = lifeTime;
    }

    public void drawComponent(Canvas canvas, Paint textPaint, Paint backPaint, Rect rect) {
        int prevBAlfa = backPaint.getAlpha();
        int prevTAlfa = textPaint.getAlpha();
        int textX = rect.left + 5;
        int textY = rect.bottom - (rect.bottom - rect.top - (int) textPaint.getTextSize()) / 2;

        textPaint.setAlpha(time > lifeTime / 2 ? prevTAlfa : prevTAlfa * time * 2 / lifeTime);
        backPaint.setAlpha(time > lifeTime / 2 ? prevBAlfa : prevBAlfa * time * 2 / lifeTime);

        canvas.drawRect(rect, backPaint);
        canvas.drawText(text, textX, textY, textPaint);

        textPaint.setAlpha(prevTAlfa);
        backPaint.setAlpha(prevBAlfa);
    }

    public void tick() {
        time--;
    }

    public boolean isOver() {
        return time <= 0;
    }
}
