package com.example.scannerproto.anlaysis.helpers.mockdb;

import com.example.scannerproto.anlaysis.helpers.mockdb.Thing;

import java.util.TreeMap;


public class ThingsBase {
    static TreeMap<String, Thing> db = new TreeMap();
    static {
        db.put("Spoon", new Thing("Ложка",  "столовый прибор"));
        db.put("Book", new Thing("Книга",  "предметъ"));
        db.put("Fork", new Thing("Вилка" , "столовый прибор"));
        db.put("Mouse", new Thing("Мышь", "устройство ввода"));
        db.put("Keyboard", new Thing("Клавиатура", "уствройство ввода"));
        db.put("Table", new Thing("Стол", "сущность"));
        db.put("Headphones", new Thing("Наушники", "устройство вывода"));
        db.put("Phone", new Thing("Телефон", "многофункционален"));
        db.put("Display", new Thing("Монитор", "устройство вывода"));
        db.put("Workbook", new Thing("Тетрадь", "тетрардь"));
    }


}
