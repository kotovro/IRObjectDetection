package com.example.scannerproto.anlaysis.helpers.mockdb;

public class Thing {
    private String id;
    private String info;


    public Thing(String id, String info) {
        this.id = id;
        this.info = info;
    }
    public String  getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }
}
