package com.example.scannerproto.anlaysis.helpers.drone_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private DroneActionState[] states = new DroneActionState[8];

    public ServerConnection() {
        try {
            socket =  new Socket(InetAddress.getByName("192.168.31.5"), 8000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void register() {
        String name = "Drone_status";
        char[] charName = SocketUtils.stringToChar(name);
        char[] len = SocketUtils.lenToChar(name.length());

        out.print(len);
        out.flush();
        out.print(name);
        out.flush();
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
