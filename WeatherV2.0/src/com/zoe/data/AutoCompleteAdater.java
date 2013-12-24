package com.zoe.data;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;


public class AutoCompleteAdater extends SimpleCursorAdapter {


	private DataBaseHelper helper;

	@SuppressWarnings("deprecation")
	public AutoCompleteAdater(Context context, int layout, Cursor c,
			String from, int to, DataBaseHelper helper) {
		super(context, layout, c, new String[] { from }, new int[] { to });
		this.helper = helper;
	}

	/**
	 * 动态查询数据库
	 */
	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (constraint != null) {
			return helper.query((String) constraint);
		} else {
			return null;
		}
	}

	/**
	 * 这里设置在弹出的提示列表中点击某一项后的返回值,返回值将被显示在文本框中
	 */
	@Override
	public CharSequence convertToString(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex("name"));
	}
}
