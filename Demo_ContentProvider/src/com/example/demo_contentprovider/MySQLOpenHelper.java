package com.example.demo_contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLOpenHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "myDatabase.db";
	private static final String DATABASE_TABLE = "GoldHoards";
	private static final int DATABASE_VERSION = 1;
	
	private static final String KEY_ID = "_id";
	private static final String KEY_GOLD
	private static final String DATABASE_CREATE  = "create table "+DATABASE_TABLE +" ("+KEY_ID+" integer primary key autoincrement, "+KEY_GO
	
	public MySQLOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
