package com.example.scannerproto.anlaysis.helpers.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Thing {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "obj_name")
    public String name;
    @ColumnInfo(name = "info")
    public String info;


    public Thing(){};
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
