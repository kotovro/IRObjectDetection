package com.example.scannerproto.anlaysis.helpers.mockdb;

import com.example.scannerproto.anlaysis.helpers.IObjectInfoGetter;

public class SimpleObjectInfoGetter implements IObjectInfoGetter {
    @Override
    public Thing getObjectInfo(String id) {
        return ThingsBase.db.get(id);
    }
}
