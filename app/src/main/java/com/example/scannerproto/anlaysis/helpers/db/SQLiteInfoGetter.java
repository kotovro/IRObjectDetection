package com.example.scannerproto.anlaysis.helpers.db;

import android.content.Context;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoSetter;
import com.example.scannerproto.MainActivity;
import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;

import java.util.ArrayList;

public class SQLiteInfoGetter implements IObjectInfoGetter, IObjectInfoSetter {

    public static ArrayList<ThingWithId> things = new ArrayList<>();
    private Context context;

    public SQLiteInfoGetter(Context context) {
        this.context = context;
    }
    @Override
    public Thing getObjectInfo(String id) {
        if (things.isEmpty()) {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
            sqLiteManager.populateNoteListArray(things);
        }
        for (ThingWithId thing : things)
        {
            if(thing.getNameId() != null && thing.getNameId().equals(id))
                return new Thing(thing.getName(), thing.getInfo());
        }
        return null;
    }

    @Override
    public void setObjectInfo(ThingWithId thing) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
        sqLiteManager.addNoteToDatabase(thing);
        things.add(thing);
    }


}