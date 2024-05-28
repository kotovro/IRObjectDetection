package com.example.scannerproto.anlaysis.helpers.db;

public class Thing {
    private String name;
    private String info;


    public Thing(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public Thing() {
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }


}