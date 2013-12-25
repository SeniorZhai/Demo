package com.zoe.jnitest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class JNItest extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView textView = (TextView) findViewById(R.id.text);
		Nadd nadd = new Nadd();
		nadd.nadd();
	}
}
