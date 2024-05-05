package com.example.scannerproto.anlaysis.helpers.db;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

import java.util.ArrayList;
import java.util.List;

public class ThingWithId{


    private int id;
    private String idName;
    private String name;
    private String info;


    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String NAME_ID_FIELD = "name_id";
    private static final String DESC_FIELD = "desc";

    public ThingWithId(String idName, String name, String info) {
        this.idName = idName;
        this.name = name;
        this.info = info;
    }

    public ThingWithId(Cursor row) {
        id = row.getInt(1);
        idName = row.getString(2);
        name = row.getString(3);
        info = row.getString(4);
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




    public static StringBuilder getDBStructure(StringBuilder sb) {
        sb.append(ID_FIELD)
                .append(" INT, ")
                .append(NAME_ID_FIELD)
                .append(" TEXT, ")
                .append(NAME_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT)");
        return sb;
    }

    public ContentValues toContentValues() {
        ContentValues res = new ContentValues();
        res.put(NAME_ID_FIELD, this.getNameId());
        res.put(NAME_FIELD, this.getName());
        res.put(DESC_FIELD, this.getInfo());
        return res;
    }

    public void setId(int id) {
        this.id = id;
    }
}
