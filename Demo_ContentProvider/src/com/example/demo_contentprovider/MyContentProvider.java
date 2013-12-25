package com.example.demo_contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.example.my_content_provider");
	private static final int ALLROWS = 1;
	private static final int SINGLE_ROW = 2;
	
	private static final UriMatcher URI_MATCHER;
	static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI("com.example.my_content_provider", "element", ALLROWS);
		URI_MATCHER.addURI("com.example.my_content_provider", "element/#", SINGLE_ROW);
	}
	private MySQLOpenHelper mySQLOpenHelperl;
	@Override
	public boolean onCreate() {
	//	构造底层的数据库
	//	延迟打开数据库，直到需要执行
	//	一个查询或者事务时再打开
		mySQLOpenHelperl = new MySQLOpenHelper(getContext(), MySQLOpenHelper.D, factory, version)
		return false;
	}
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
