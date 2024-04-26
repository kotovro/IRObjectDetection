package com.example.scannerproto.anlaysis.helpers.mockdb;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoAdder;
import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

public class SimpleObjectAdder implements IObjectInfoAdder<Thing> {
    @Override
    public void addInfo(Thing info) {
        ThingsBase.db.put(info.getId(), info);
    }
}
