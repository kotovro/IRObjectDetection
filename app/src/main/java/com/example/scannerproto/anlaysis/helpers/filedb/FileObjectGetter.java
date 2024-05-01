package com.example.scannerproto.anlaysis.helpers.filedb;

import android.os.Environment;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileObjectGetter implements IObjectInfoGetter {

    public  static LinkedList<Filethings> things;

    static {
        try {
            things = FileUtils.readListFromFile("base.txt");
        } catch (IOException e) {
            things = new LinkedList<>();
//            throw new RuntimeException(e);
        }
    }

    public FileObjectGetter() {}

    public static void writeToFile() throws IOException {
        FileUtils.writeListToFile("base.txt", things);
    }
    @Override
    public  Thing getObjectInfo(String id) {
        Thing res = null;
        for (Filethings thing: things) {
            if (thing.getId().equals(id)) {
                res = new Thing(thing.getName(), thing.getInfo());
            }
        }
        return res;
    }
}
