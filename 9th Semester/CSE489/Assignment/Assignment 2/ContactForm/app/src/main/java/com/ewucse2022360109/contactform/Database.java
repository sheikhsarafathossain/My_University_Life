package com.ewucse2022360109.contactform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "contact_info.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE contact_info (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name" + " TEXT, " +
                "email" + " TEXT, " +
                "phone_home" + " TEXT, " +
                "phone_office" + " TEXT, " +
                "photo" + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void insertContact(String name, String email, String phoneHome, String phoneOffice, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("email",email);
        values.put("phone_home", phoneHome);
        values.put("phone_office", phoneOffice);
        values.put("photo", photo);

        db.insert("contact_info", null ,  values);
        db.close();
    }

    public Cursor getContact(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("contact_info", null, null, null, null, null, "id" + " DESC", "1");
    }
}
