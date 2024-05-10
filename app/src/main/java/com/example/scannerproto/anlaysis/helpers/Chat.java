package com.example.scannerproto.anlaysis.helpers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.LinkedList;

public class Chat {
    LinkedList<ChatComponent> messages = new LinkedList<>();
    Paint textPaint;
    Paint backPaint;

    public Chat() {
        textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);

        backPaint = new Paint();
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(Color.BLACK);
        backPaint.setAlpha(127);
    }

    public void addMessage(String message) {
        messages.addLast(new ChatComponent(message));
    }

    public void drawChat(Canvas canvas) {
        int textSize = (int) textPaint.getTextSize();
        Rect rect = new Rect();
        for (int i = 0; i < messages.size(); i++) {
            rect.set(700, 310 + i * textSize * 12 / 10, 1024, 310 + (i + 1) * textSize * 12 / 10);
            messages.get(messages.size() - 1 - i).drawComponent(canvas, textPaint, backPaint, rect);
        }
    }

    public void tick() {
        int toDel = 0;
        for (ChatComponent message : messages) {
            message.tick();
            if (message.isOver()) {
                toDel++;
            }
        }
        if (toDel > 0) {
            messages.subList(0, toDel).clear();
        }
    }
}
