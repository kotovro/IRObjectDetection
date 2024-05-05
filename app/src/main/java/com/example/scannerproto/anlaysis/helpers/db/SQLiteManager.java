package com.example.scannerproto.anlaysis.helpers.db;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

public class SQLiteManager extends SQLiteOpenHelper {
    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "ThingsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Things";
    private static final String COUNTER = "Counter";




    public SQLiteManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context)
    {
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sql = ThingWithId.getDBStructure(sql);

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
//        switch (oldVersion)
//        {
//            case 1:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
//            case 2:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
//        }
    }

    public void addNoteToDatabase(ThingWithId thingWithId)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = thingWithId.toContentValues();

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT last_insert_rowid()", null))
        {
            if(result.getCount() != 0)
            {
                int id = result.getInt(1);
                thingWithId.setId(id);
            }
        }

    }

    public void populateNoteListArray(ArrayList<ThingWithId> thingsArray)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
        {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    ThingWithId thing = new ThingWithId(result);
                    thingsArray.add(thing);
                }
            }
        }
    }

    public void updateNoteInDB(ThingWithId note)
    {
    }

}