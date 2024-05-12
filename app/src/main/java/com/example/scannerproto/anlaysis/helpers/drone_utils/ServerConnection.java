package com.example.scannerproto.anlaysis.helpers.drone_utils;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection extends AsyncTask<Void, Void, Void> {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private DroneActionState[] states = new DroneActionState[8];

    public ServerConnection() {
        for (int i = 0; i < states.length; i++) {
            states[i] = DroneActionState.DISABLED;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        init();
        register();
        for (int i = 0; i < 100000000; i++) {
            updateStates();
        }

        return null;
    }

    public void init() {
        try {
            socket = new Socket("192.168.31.5", 8000);
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
        String name = "Drone_status";
        char[] charName = SocketUtils.stringToChar(name);
        char[] len = SocketUtils.lenToChar(name.length());

        out.print(len);
        out.flush();
        out.print(charName);
        out.flush();

//        out.close();
    }

    public void updateStates() {
        try {
            if (in.ready()) {
                char[] input = new char[32];
                while (in.ready()) {
                    in.read(input);
                }

                SocketUtils.updateCharsToState(input, states);
            }
        } catch (IOException e) {
            throw new RuntimeException("Socket read error");
        }
    }

    public void closeSocket() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DroneActionState[] getStates() {
        return states;
    }
}
