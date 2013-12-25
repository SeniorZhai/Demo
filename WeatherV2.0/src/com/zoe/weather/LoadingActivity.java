package com.zoe.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zoe.data.MyData_1;
import com.zoe.data.MyData_2;
import com.zoe.data.MyData_3;

public class LoadingActivity extends Activity implements Runnable {
	MyApplication application;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading);
		application = (MyApplication) getApplication();
		new Thread(LoadingActivity.this).start();
	}

	@Override
	public void run() {
		application.myData_1 = new MyData_1();
		application.myData_2 = new MyData_2();
		application.myData_3 = new MyData_3();
		application.preferences = getSharedPreferences("zoe", MODE_PRIVATE);
		application.dataMan = new DataMan(application.preferences, application,application.cityList);
		application.refresh = new Refresh(application);
		application.refresh.start();
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		try {
			Thread.sleep(1000L);
		} catch (Exception e) {

		}
		startActivity(intent);
		finish();
	}
}
