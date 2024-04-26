package com.example.scannerproto.anlaysis.helpers;

import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

@FunctionalInterface
public interface IObjectInfoAdder<T> {
    void addInfo(T info);
}
