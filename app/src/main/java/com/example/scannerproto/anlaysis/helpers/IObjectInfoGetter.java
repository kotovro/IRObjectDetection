package com.example.scannerproto.anlaysis.helpers;

import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

@FunctionalInterface
public interface IObjectInfoGetter {
    Thing getObjectInfo(String id);
}