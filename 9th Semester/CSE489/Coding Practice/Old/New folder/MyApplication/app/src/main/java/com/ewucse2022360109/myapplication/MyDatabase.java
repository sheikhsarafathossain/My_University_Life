package com.ewucse2022360109.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper{

    public MyDatabase (Context context){
        super(context, "Contactform.db" ,null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable=  "CREATE TABLE contactform ("+
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name" + " TEXT," +
                "email" + " TEXT," + // FIXED: Added a space before "TEXT"
                "phone_home" + " TEXT," +
                "phone_office" + " TEXT," +
                "userimage" + " TEXT" +
                ")";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // You might want to add logic here for future app versions
        // For example: db.execSQL("DROP TABLE IF EXISTS contactform");
        // onCreate(db);
    }

    public void insertForm(String name, String email, String phonehome, String phoneoffice , String userimage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues col = new ContentValues();

        col.put("name",name);
        col.put("email",email);
        col.put("phone_home",phonehome);
        col.put("phone_office",phoneoffice);
        col.put("userimage",userimage);

        db.insert("contactform", null ,  col);
        db.close();
    }

    public Cursor showcontact(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("contactform",null,null,null,null,null,"id"+" DESC","1");
    }
}