package com.example.fragamenttest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MainActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ArticleFragment fragment = new ArticleFragment();
		FragmentManager fragmentManager = getFragmentManager();
	//	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
	}
}
