package com.example.scannerproto.anlaysis.helpers;

import com.example.scannerproto.anlaysis.helpers.db.Thing;
import com.example.scannerproto.anlaysis.helpers.db.ThingWithId;

@FunctionalInterface
public interface IObjectInfoGetter {
    Thing getObjectInfo(String id);
}