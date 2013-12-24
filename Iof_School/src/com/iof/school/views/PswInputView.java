package com.iof.school.views;

import com.iof.school.R;
import com.iof.showlib.utils.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class PswInputView extends LinearLayout
{
	public static final int PSW_KEY_LEFT = 0;
	public static final int PSW_KEY_UP = 1;
	public static final int PSW_KEY_RIGHT = 2;
	public static final int PSW_KEY_DOWN = 3;
	
	private View mViewLab;
	private View[] mKeyViews = new View[6];
	/**
	 * 0-左；1-上；2-右；3-下；
	 */
	private int[] mKeys = new int[6];
	private int mKeyCount;

	public PswInputView(Context context)
	{
		super(context);
		((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.psw_input, this);
		
		mViewLab = findViewById(R.id.psw_input_lab);
		mKeyViews[0] = findViewById(R.id.psw_input_key1);
		mKeyViews[1] = findViewById(R.id.psw_input_key2);
		mKeyViews[2] = findViewById(R.id.psw_input_key3);
		mKeyViews[3] = findViewById(R.id.psw_input_key4);
		mKeyViews[4] = findViewById(R.id.psw_input_key5);
		mKeyViews[5] = findViewById(R.id.psw_input_key6);
	}

	public void show(boolean showLab)
	{
		if (showLab)
		{
			mViewLab.setVisibility(VISIBLE);
		}
		else
		{
			mViewLab.setVisibility(GONE);
		}
		reset();
		setVisibility(VISIBLE);
	}
	
	public void hide()
	{
		setVisibility(GONE);
	}
	
	public void reset()
	{
		mKeyCount = 0;
		for (int i = 0; i < mKeyViews.length; i++)
		{
			mKeyViews[i].setBackgroundDrawable(null);
		}
	}
	
	public void input(int key)
	{
		if (mKeyCount < mKeys.length)
		{
			mKeys[mKeyCount] = key;
			mKeyViews[mKeyCount].setBackgroundResource(R.drawable.psw_star);
			mKeyCount++;
		}
	}
	
	public int getPswCount()
	{
		return mKeyCount;
	}
	
	public String getPsw()
	{
		if (mKeyCount < 6)
		{
			Util.ShowToast(getContext(), "必须输入6位密码");
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mKeyCount; i++)
		{
			sb.append(mKeys[i]);
		}
		return sb.toString();
	}
	
	public void submit()
	{
		
	}
}
