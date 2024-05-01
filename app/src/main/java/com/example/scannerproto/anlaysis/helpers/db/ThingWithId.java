package com.example.scannerproto.anlaysis.helpers.db;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

import java.util.ArrayList;

public class ThingWithId implements IObjectInfoGetter {


    private int id;
    private String idName;
    private String name;
    private String info;

    public static ArrayList<ThingWithId> thingsArrayList = new ArrayList<>();

    public ThingWithId(int id, String idName, String name, String info) {
        this.id = id;
        this.idName = idName;
        this.name = name;
        this.info = info;
    }

    public ThingWithId() {
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getNameId() {
        return idName;
    }

    @Override
    public Thing getObjectInfo(String id) {
        for (ThingWithId thing : thingsArrayList)
        {
            if(thing.getNameId() != null && thing.getNameId().equals(id))
                return new Thing(thing.getName(), thing.getInfo());
        }
        return null;
    }



}
