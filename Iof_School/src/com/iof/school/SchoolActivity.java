package com.iof.school;

import com.iof.school.views.ViewHome;
import com.iof.showlib.BaseActivity;
import android.os.Bundle;

public class SchoolActivity extends BaseActivity
{
	private ViewHome mViewHome;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mViewHome = new ViewHome(this);
		showHomeView(mViewHome);
	}
}