package com.example.scannerproto.anlaysis.helpers.drone_utils;

public class SocketUtils {
    public static char[] stringToChar(String str) {
        char[] arr = new char[str.length() * 2];
        char[] chars = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            arr[i * 2] = chars[i];
        }

        return arr;
    }

    public static char[] lenToChar(int len) {
        if (len > 15) throw new RuntimeException("to big name");
        char[] arr = {(char) len, 0x00, 0x00, 0x00 };
        return arr;
    }

    public static void updateCharsToState(char[] arr, DroneActionState[] states){
        for (int i = 0; i < states.length; i++) {
            states[i] = arr[i * 4] == 0 ? DroneActionState.DISABLED : DroneActionState.ENABLED;
        }
    }
}
