package com.example.scannerproto.anlaysis.helpers.drone_utils;

import static android.content.ContentValues.TAG;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPServer extends AsyncTask<Void, Void, Void> {
    private int serverPort = 8080;
    private DatagramSocket serverSocket;
    private byte[] receiveData = new byte[1024];
    private DroneActionState[] states = new DroneActionState[4];

    public UDPServer() {
        try {
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        Arrays.fill(states, DroneActionState.DISABLED);
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
            if (!command.isEmpty()) {
                throw new RuntimeException();
            }
            Log.println(Log.VERBOSE, TAG, "Получена команда от клиента: " + command);
        }
    }

    public DroneActionState[] getStates() {
        return states;
    }

//    import socket
//
//            java_host = '192.168.50.205'
//    java_port = 8080
//
//    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
//
//            while True:
//    command = input('Enter command: ')
//    udp_socket.sendto(command.encode('utf-8'), (java_host, java_port))
//
//    response = udp_socket.recv(2048)
//    print(response.decode('utf-8'))
//
//            udp_socket.close()
}
