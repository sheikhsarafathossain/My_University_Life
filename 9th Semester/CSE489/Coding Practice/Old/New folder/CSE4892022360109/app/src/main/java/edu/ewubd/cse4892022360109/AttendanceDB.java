package edu.ewubd.cse4892022360109;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttendanceDB extends SQLiteOpenHelper {

	public AttendanceDB(Context context) {
		super(context, "ClassAttendanceDB.db", null, 2);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("DB@OnCreate");
		String sql = "CREATE TABLE attendance  ("
				+ "name TEXT,"
				+ "course TEXT,"
				+ "datetime INT,"
				+ "status INT,"
				+ "remarks TEXT,"
				+ "PRIMARY KEY(name, course, datetime))";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Write code to modify database schema here");
		// db.execSQL("ALTER table my_table  ......");
		// db.execSQL("CREATE TABLE  ......");
	}
	public void insert(StudentAttendance sa, long dateTime, String course) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("name", sa.name);
		cols.put("course", course);
		cols.put("datetime", dateTime);
		cols.put("status", sa.status ? 1 : 0);
		cols.put("remarks", sa.remarks);
		db.insert("attendance", null ,  cols);
		db.close();
	}
	public void update(StudentAttendance sa, long dateTime, String course) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("status", sa.status ? 1 : 0);
		values.put("remarks", sa.remarks);
		db.update("attendance", values, "name=?,course=?,datetime=?", new String[ ] {sa.name, course, ""+dateTime} );
		db.close();
	}
	public void delete(StudentAttendance sa, long dateTime, String course) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("attendance", "name=?,course=?,datetime=?", new String[ ] {sa.name, course, ""+dateTime} );
		db.close();
	}
	public Cursor select(String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = null;
		try {
			res = db.rawQuery(query, null);
		} catch (Exception e){
			e.printStackTrace();
		}
		return res;
	}
}