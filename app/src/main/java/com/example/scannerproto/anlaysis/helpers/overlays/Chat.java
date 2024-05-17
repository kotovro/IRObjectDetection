package com.example.scannerproto.anlaysis.helpers.overlays;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.LinkedList;

public class Chat {
    LinkedList<ChatComponent> messages = new LinkedList<>();
    LinkedList<ChatComponent> specialMessages = new LinkedList<>();
    LinkedList<Rect> rects = new LinkedList<>();
    Paint textPaint;
    Paint backPaint;




    public Chat() {
        textPaint = new Paint();
        textPaint.setTextSize(15);
        textPaint.setColor(Color.WHITE);

        backPaint = new Paint();
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(Color.BLACK);
        backPaint.setAlpha(127);
    }

    public void addMessage(String message, Rect rect) {
        if (rect != null) {
            specialMessages.addLast(new ChatComponent(message));
            rects.addLast(rect);
        } else {
            messages.addLast(new ChatComponent(message));
        }
    }

    public void drawChat(Canvas canvas) {

        int textSize = (int) textPaint.getTextSize();
        Rect rect = new Rect();
        for (int i = 0; i < messages.size(); i++) {
            rect.set(500, 250 + i * textSize * 12 / 10, 1024, 250 + (i + 1) * textSize * 12 / 10);
            messages.get(messages.size() - 1 - i).drawComponent(canvas, textPaint, backPaint, rect);
        }
        for (int i = 0; i < specialMessages.size(); i++) {
            specialMessages.get(i).drawComponent(canvas, textPaint, backPaint, rects.get(i));
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
        toDel = 0;
        for (int i = 0; i < specialMessages.size(); i++) {
            specialMessages.get(i).tick();
            if (specialMessages.get(i).isOver()) {
                toDel++;
            }
        }
        if (toDel > 0) {
            rects.subList(0, toDel).clear();
            specialMessages.subList(0, toDel).clear();
        }
    }

    public void addSpecialMessage(String message, Rect rect) {
        specialMessages.addLast(new ChatComponent(message));
        rects.addLast(rect);
    }
}
