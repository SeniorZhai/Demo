package com.zoe.weather;

import java.util.ArrayList;

import android.app.Application;
import android.content.SharedPreferences;

import com.zoe.data.MyData_1;
import com.zoe.data.MyData_2;
import com.zoe.data.MyData_3;

public class MyApplication extends Application {
	public MyData_1 myData_1;
	public MyData_2 myData_2;
	public MyData_3 myData_3;
	public String cityName,city_id;
	public Refresh refresh;
	public SharedPreferences preferences;
	public ArrayList<String> cityList;
	public DataMan dataMan;
	@Override
	public void onCreate() {
		super.onCreate();	
		cityList = new ArrayList<String>();
	}
}
