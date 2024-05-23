package com.example.scannerproto.anlaysis.helpers.drone_utils;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.CYAN;
import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.LTGRAY;
import static android.graphics.Color.MAGENTA;
import static android.graphics.Color.RED;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.scannerproto.anlaysis.helpers.overlays.Chat;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPServerDrone extends AsyncTask<Void, Void, Void> {
    private int serverPort = 8080;
    private DatagramSocket serverSocket;
    private byte[] receiveData = new byte[1024];

    private int[] prevState = new int[4];
    private int[] curState = new int[4];

    private DroneMovements[] ports = new DroneMovements[4];

    public UDPServerDrone() {
        try {
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        ports[0] = new DroneMovements("Вправо", "Боковое торможение", "Влево", DroneMovements.right, null, DroneMovements.left, Color.rgb(30, 40 , 30), Color.rgb(30, 80 , 30), Color.rgb(30, 120 , 30));
        ports[1] = new DroneMovements("Назад", "Прямое торможение", "Вперед", null, null, null, Color.rgb(50, 50 , 100), Color.rgb(50, 50 , 150), Color.rgb(50, 50 , 200));
        ports[2] = new DroneMovements("Вниз", "Вертикальное торможение", "Вверх", DroneMovements.bottom, null, DroneMovements.top, Color.rgb(50, 100 , 50), Color.rgb(50, 150 , 50), Color.rgb(50, 200 , 50));
        ports[3] = new DroneMovements("Против часовой", "Остановка поворота", "По часовой", null, null, null, Color.rgb(100, 50 , 50), Color.rgb(150, 50, 50), Color.rgb(200, 50, 50));
    }

    @Override
    public Void doInBackground(Void... voids) {
        while (true) {
            Log.println(Log.VERBOSE, TAG, "Is chat enabled: ");
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String command = new String(receivePacket.getData(), 0, receivePacket.getLength());
//            Log.println(Log.VERBOSE, TAG, "New message");
            Log.println(Log.VERBOSE, TAG, Arrays.toString(SocketUtils.parseInput(command)));
            int[] commands = SocketUtils.parseInput(command);
            curState = commands;

            Log.println(Log.VERBOSE, TAG, "Is chat enabled: ");
        }
    }

    public void updateChat(Chat chat) {
        for (int i = 0; i < curState.length; i++) {
            if (curState[i] != prevState[i]) {
                chat.addMessage(ports[i].getMessage(curState[i]), ports[i].getRect(curState[i]), ports[i].getColor(curState[i]));
            }
        }
        prevState = curState;
    }
}