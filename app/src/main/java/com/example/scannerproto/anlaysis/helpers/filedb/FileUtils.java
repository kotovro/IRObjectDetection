package com.example.scannerproto.anlaysis.helpers.filedb;

import android.content.Context;
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
        File file = new File("base.txt");
        return file.exists();
    }
    public static LinkedList<Filethings> readListFromFile(String file) throws IOException {
        Scanner sc;
        File temp = new File(String.valueOf(Context.MODE_PRIVATE), file);
        if (!(temp.exists())) {
            temp.createNewFile();
        }
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
    public static void writeListToFile(String fName, LinkedList<Filethings> list) throws IOException {
        if (checkFile()) {
            FileWriter fw = new FileWriter(fName);
            ObjectMapper mapper = new ObjectMapper();
            for (Filethings st : list) {
                String str = String.format("%s%n", mapper.writeValueAsString(st));
                fw.write(str);
            }
            fw.close();
        }
    }
}
