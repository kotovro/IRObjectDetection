package com.example.scannerproto.anlaysis.helpers.drone_utils;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;
import com.example.scannerproto.anlaysis.helpers.overlays.StaticChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class UDPServerHand extends AsyncTask<Void, Void, Void> {
    private int serverPort = 8090;
    private DatagramSocket serverSocket;
    private byte[] receiveData = new byte[1024];

    private int[] prevState = new int[5];
    private int[] curState = new int[5];


    public UDPServerHand() {
        try {
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
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

    public void updateChat(StaticChat chat) {
        for (int i = 0; i < curState.length; i++) {
            if (curState[i] != prevState[i]) {
                chat.update(i, FingerStatus.values()[i].getMessage(curState[i]), curState[i]);
            }
        }
        prevState = curState;
    }
}