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

    private String hostName = "192.168.31.5";
    private int portNumber = 8000;
    private BufferedReader in;
    private PrintWriter out;
    private DroneActionState[] states = new DroneActionState[8];

    public ServerConnection() {
        for (int i = 0; i < states.length; i++) {
            states[i] = DroneActionState.DISABLED;
        }
    }
    public ServerConnection(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        for (int i = 0; i < states.length; i++) {
            states[i] = DroneActionState.DISABLED;
        }
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

    public DroneActionState[] getStates() {
        return states;
    }
}
