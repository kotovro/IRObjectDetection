package com.example.scannerproto.anlaysis.helpers.overlays;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.scannerproto.anlaysis.helpers.drone_utils.FingerStatus;

import java.util.LinkedList;

public class StaticChat {
    LinkedList<ChatComponent> messages = new LinkedList<>();
    LinkedList<FingerStatus> fingers = new LinkedList<>();
    int[] intensities = new int[5];
    Paint textPaint;
    Paint backPaint;

    public StaticChat() {
        textPaint = new Paint();
        textPaint.setTextSize(15);
        textPaint.setColor(Color.WHITE);

        backPaint = new Paint();
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(Color.BLACK);
        backPaint.setAlpha(127);

        fingers.add(FingerStatus.INDEX);
        fingers.add(FingerStatus.MIDDLE);
        fingers.add(FingerStatus.RING);
        fingers.add(FingerStatus.PINKIE);
        fingers.add(FingerStatus.THUMB);

        for (int i = 0; i < 5; i++) {
            addMessage("");
        }
    }

    public void addMessage(String message) {
        messages.addLast(new ChatComponent(message));
    }

    public void drawChat(Canvas canvas) {

        int textSize = (int) textPaint.getTextSize();
        Rect rect = new Rect();
        for (int i = 0; i < messages.size(); i++) {
            rect.set(500, 250 + i * textSize * 12 / 10, 1024, 250 + (i + 1) * textSize * 12 / 10);
            backPaint.setColor(fingers.get(i).getColor(intensities[i]));
            messages.get(i).drawComponent(canvas, textPaint, backPaint, rect);
        }
    }

    public void tick() {
        for (ChatComponent message : messages) {
            message.tick();
        }
    }

    public void update(int pos, String text, int intensity) {
        messages.get(pos).resetTime();
        messages.get(pos).setText(text);
        intensities[pos] = intensity;
    }
}
