package com.example.scannerproto.anlaysis.helpers.mockdb;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoAdder;

public class SimpleObjectAdder implements IObjectInfoAdder<Thing> {
    @Override
    public void addInfo(String id, Thing info) {
        ThingsBase.db.put(id, info);
    }
}
