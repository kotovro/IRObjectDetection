package com.example.scannerproto.anlaysis.helpers;

public class Thing {
    private int id = -10;
    private String name;
    private String info;


    public Thing(String id, String name, String info) {
        this.id = Integer.parseInt(id);
        this.info = info;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}
