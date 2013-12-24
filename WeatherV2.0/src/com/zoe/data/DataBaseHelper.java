package com.zoe.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	@SuppressLint("SdCardPath")
	private String DB_PATH;
	private String MY_PATH;

	public static String DB_NAME = "weather.db3";

	public SQLiteDatabase myDataBase;

	private final Context myContext;

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = myContext.getFilesDir().getAbsolutePath() + "/";
		MY_PATH = DB_PATH + DB_NAME;
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (!dbExist) {
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		File file = new File(MY_PATH);
		return file.exists();
	}

	public void copyDataBase() throws IOException {

		// 打开本地的数据库到输入流中
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// 创建数据库存放位置
		

		// 打开文件到输出流
		OutputStream myOutput = new FileOutputStream(MY_PATH);

		// 从输入流传到输出流，即将APK自带数据库传到指定位置
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// 关闭流
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		myDataBase = SQLiteDatabase.openDatabase(MY_PATH, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);

	}

	public Cursor query(String name) {
		return myDataBase.rawQuery("select * from user where name like '%"
				+ name + "%'", null);
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
