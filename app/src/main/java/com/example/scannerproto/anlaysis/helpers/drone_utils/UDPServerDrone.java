package com.example.scannerproto.anlaysis.helpers.drone_utils;

import static android.content.ContentValues.TAG;
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
        ports[0] = new DroneMovements("Вправо", "Боковое торможение", "Влево", DroneMovements.right, null, DroneMovements.left);
        ports[1] = new DroneMovements("Назад", "Прямое торможение", "Вперед", null, null, null);
        ports[2] = new DroneMovements("Вниз", "Вертикальное торможение", "Вверх", DroneMovements.bottom, null, DroneMovements.top);
        ports[3] = new DroneMovements("Против часовой", "Остановка поворота", "По часовой", null, null, null);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String command = new String(receivePacket.getData(), 0, receivePacket.getLength());
            Log.println(Log.VERBOSE, TAG, Arrays.toString(SocketUtils.parseInput(command)));
            int[] commands = SocketUtils.parseInput(command);
            curState = commands;
        }
    }

    public void updateChat(Chat chat) {
        for (int i = 0; i < curState.length; i++) {
            if (curState[i] != prevState[i]) {
                chat.addMessage(ports[i].getMessage(curState[i]), ports[i].getRect(curState[i]));
            }
        }
        prevState = curState;
    }
}
