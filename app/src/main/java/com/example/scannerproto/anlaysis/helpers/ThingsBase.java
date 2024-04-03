package com.example.scannerproto.anlaysis.helpers;

import java.util.TreeMap;

public class ThingsBase {
    public TreeMap<Integer, Thing> base = new TreeMap<>();

    public ThingsBase() {

    }
//    public int addToBase(Person person) {
//        int id = person.getId();
//        base.put(id, person);
//        return id;
//    }

    public Thing getThing(Integer id) {
        return base.get(id);
    }
}
