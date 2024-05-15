package com.example.scannerproto.anlaysis.helpers.drone_utils;

import static android.content.ContentValues.TAG;
import android.os.AsyncTask;
import android.util.Log;

import com.example.scannerproto.anlaysis.helpers.overlays.Chat;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPServer extends AsyncTask<Void, Void, Void> {
    private int serverPort = 8080;
    private DatagramSocket serverSocket;
    private byte[] receiveData = new byte[1024];
    private DroneActionState[] states = new DroneActionState[4];

    private int[] prevState = new int[4];
    private int[] curState = new int[4];

    private DroneMovements[] ports = new DroneMovements[4];

    public UDPServer() {
        try {
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < states.length; i++) {
            states[i] = new DroneActionState(0);
        }
        ports[0] = new DroneMovements("Вправо", "Боковое торможение", "Влево", Chat.right, null, Chat.left);
        ports[1] = new DroneMovements("Назад", "Прямое торможение", "Вперед", null, null, null);
        ports[2] = new DroneMovements("Вниз", "Вертикальное торможение", "Вверх", Chat.bottom, null, Chat.top);
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
//            for (int i = 0; i < states.length; i++) {
//                states[i].setIntensity(commands[i]);
//            }
            curState = commands;

        }
    }

    public void updateChat(Chat chat) {
        for (int i = 0; i < 4; i++) {
            if (curState[i] != prevState[i]) {
                chat.addMessage(ports[i].getMessage(curState[i]), ports[i].getRect(curState[i]));
            }
        }
        prevState = curState;
    }
}
