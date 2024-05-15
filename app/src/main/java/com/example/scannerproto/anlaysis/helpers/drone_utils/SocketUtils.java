package com.example.scannerproto.anlaysis.helpers.drone_utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

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
            states[i] = arr[i * 4] == 0 ? new DroneActionState(0) : new DroneActionState(0);
        }
    }

    public static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

    public static int[] byteToInt(byte[] bytes) {
        int[] ints = new int[bytes.length / 4];

        for (int i = 0; i < bytes.length / 4; i++) {
            byte[] subBytes = new byte[4];

            for (int j = 0; j < 4; j++) {
                subBytes[j] = bytes[4 * i + j];
            }

            ByteBuffer wrapped = ByteBuffer.wrap(subBytes);
            ints[i] = wrapped.getInt();
        }

        return ints;
    }
    public static int[] parseInput(String input) {
        String[] temp = input.split(" ");
        int[] res = new int[4];
        for(int i = 0; i < temp.length; i++) {
            res[i] = Integer.parseInt(temp[i]);
        }
        return res;
    }
}
