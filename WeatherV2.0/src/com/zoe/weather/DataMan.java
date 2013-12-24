package com.zoe.weather;

import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

public class DataMan {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private MyApplication application;
	private ArrayList<String> cityArrayList;
	private final int MAX_LENGTH = 10;

	@SuppressLint("CommitPrefEdits")
	public DataMan(SharedPreferences sharedPreferences,
			Application application,ArrayList<String> cityList) {
		this.sharedPreferences = sharedPreferences;
		this.application = (MyApplication) application;
		this.cityArrayList = cityList;
		editor = sharedPreferences.edit();
		init();
	}

	public void init() {
		int i = 0;
		for (; i < MAX_LENGTH; i++) {
			String city = sharedPreferences.getString("city" + i, null);
			if (city != null) {
				cityArrayList.add(city);
			} else {
				break;
			}
		}
		if (i == 0) {
			editor.clear();
			editor.putString("city0", "北京");
			editor.commit();
			cityArrayList.add("北京");
			application.city_id = "101010100";
		}
		application.city_id = sharedPreferences
				.getString("cityID", "101010100");
		application.cityName = cityArrayList.get(0);
	}

	public boolean add(String str) {
		int len = cityArrayList.size();
		if (len < 10) {
			cityArrayList.add(len, str);
			return true;
		} else
			return false;
	}

	public void remove(int index) {
		cityArrayList.remove(index);
	}

	public void onStop() {
		int len = cityArrayList.size();
		editor.clear();
		for (int i = 0; i < len; i++) {
			editor.putString("city" + i, cityArrayList.get(i));
		}
		editor.putString("cityID", application.city_id);
		editor.commit();
	}
}
