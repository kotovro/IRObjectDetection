package com.example.scannerproto.anlaysis.helpers.filedb;

import android.os.Environment;

import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
public class FileUtils {


    public static boolean checkFile(){
        File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "base.txt");
        return file.exists();
    }
    public static LinkedList<Filethings> readListFromFile(String file) throws IOException {
        Scanner sc;
        if (checkFile()) {
            try {
                sc = new Scanner(new FileReader(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            LinkedList<Filethings> res = new LinkedList<>();
            ObjectMapper mapper = new ObjectMapper();
            while (sc.hasNextLine()) {
                Filethings st = null;
                try {
                    st = mapper.readValue(sc.nextLine(), Filethings.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                res.add(st);
            }
            return res;
        }
        return new LinkedList<>();
    }
    public static void writeListToFile(String fName, LinkedList<Filethings> list) throws IOException {
        FileWriter fw = new FileWriter(fName);
        ObjectMapper mapper = new ObjectMapper();
        for (Filethings st: list) {
            String str = String.format("%s%n", mapper.writeValueAsString(st));
            fw.write(str);
        }
        fw.close();
    }
}
