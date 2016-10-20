package com.wmx.android.wrstar.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
	private static final String DB_NAME = "WRSTAR.db";
	private static final int VERSION = 1;
	public static final String TABLE_NAME = "WATCHRECORD"+"_"+VERSION;


	public static final String VIDEO_ID = "videoid";
	public static final String TITLE = "title";
	public static final String TIMESTAMP = "timestamp";
	public static final String IMG_URL = "imgurl";
	public static final String TYPE = "type";  // 0 点播  1直播

	public static final String TYPE_Ondemand ="0";
	public static final String TYPE_Live ="1";

	private static final String TABLE_CREATE = "create table " + TABLE_NAME + " (id integer primary key autoincrement,"+VIDEO_ID+" text,"+TIMESTAMP+" text,"+IMG_URL+" text,"+TYPE+" text,"+TITLE+" text)";

	protected DBHandler(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("alter table " + TABLE_NAME + " add typeName text");
	}

}
