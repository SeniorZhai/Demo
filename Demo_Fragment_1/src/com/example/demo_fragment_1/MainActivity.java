package com.example.demo_fragment_1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Display display = getWindowManager().getDefaultDisplay();
		if (display.getWidth() > display.getHeight()) {
			Fragment1 fragment1 = new Fragment1();
			getFragmentManager().beginTransaction()
					.replace(R.id.main_layout, fragment1).commit();
		} else {
			Fragment2 fragment2 = new Fragment2();
			getFragmentManager().beginTransaction()
					.replace(R.id.main_layout, fragment2).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
