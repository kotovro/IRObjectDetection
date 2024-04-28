package com.example.scannerproto.anlaysis.helpers.filedb;

public class Filethings {
    private String id;
    private String name;
    private String info;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public Filethings(String id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }
}
