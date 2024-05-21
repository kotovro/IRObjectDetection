package com.example.scannerproto.anlaysis.helpers.drone_utils;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.scannerproto.anlaysis.helpers.overlays.StaticChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ServerConnection extends AsyncTask<Void, Void, Void> {
    private Socket socket;

    private String hostName = "192.168.100.135";
    private int portNumber = 8000;
    private BufferedReader in;
    private PrintWriter out;

    private int[] prevState = new int[5];
    private int[] curState = new int[5];

    public ServerConnection() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        init();
        register();
        while (true){
            updateStates();
        }
    }

    public void init() {
        try {
            socket = new Socket(hostName, portNumber);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (UnknownHostException e) {
            Log.println(Log.VERBOSE, TAG, "unknown host");
            throw new RuntimeException("Unknown host");
        } catch (IOException e) {
            Log.println(Log.VERBOSE, TAG, "IO");
            throw new RuntimeException("Can not connect to socket");
        } catch (Exception e) {
            Log.println(Log.VERBOSE, TAG, "NOOOOOOOOOOOOOOOOOOO");
            throw new RuntimeException("Unexpected exception");
        }
    }

    public void register() {
        String name = "Hand_status";
        char[] charName = SocketUtils.stringToChar(name);
        char[] len = SocketUtils.lenToChar(name.length());

        out.print(len);
        out.flush();
        out.print(charName);
        out.flush();
    }

    public void updateStates() {
        try {
            if (in.ready()) {
                char[] input = new char[20];
                while (in.ready()) {
                    in.read(input);
                }

                prevState = curState;
                curState = SocketUtils.byteToInt(SocketUtils.toBytes(input));
                Log.println(Log.VERBOSE, TAG, Arrays.toString(curState));
            }
        } catch (IOException e) {
            throw new RuntimeException("Socket read error");
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
