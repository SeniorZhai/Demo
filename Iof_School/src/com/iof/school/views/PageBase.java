package com.iof.school.views;

import java.util.Calendar;

import com.iof.school.R;
import com.iof.school.obj.Child;
import com.iof.school.utils.Const;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public abstract class PageBase extends LinearLayout
{
	protected ViewHome mHome;
	
	public PageBase(ViewHome home)
	{
		super(home.getContext());
		mHome = home;
	}
	
	public void showInputBoard(EditText view)
	{
		InputMethodManager imm= (InputMethodManager) mHome.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}
	
	public void hideInputBoard(EditText view)
	{
		InputMethodManager imm= (InputMethodManager) mHome.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
	}
	
	public void setHead(View view, Child child)
	{
		if (child.mResId > 0)
		{
			view.setBackgroundResource(child.mResId);
		}
		else if (child.mSex == 2)
		{
			view.setBackgroundResource(R.drawable.boy);
		}
		else
		{
			view.setBackgroundResource(R.drawable.girl);
		}
	}
	
	public boolean handleMessage(Message msg)
	{
		return true;
	}
	
	/**
	 * 隐藏
	 */
	public void onPause()
	{
	}
	
	/**
	 * 恢复
	 */
	public void onResume()
	{
	}
	
	public String getAge(Child child)
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month =  calendar.get(Calendar.MONTH) + 1;
		int day =  calendar.get(Calendar.DAY_OF_MONTH);
		
		int dY = year - child.mYear;
		int dM = month - child.mMonth;
		int dD = day - child.mDay;
		
		boolean bEstimate = dY < 0 || (dY == 0 && dM < 0) || (dY == 0 && dM == 0 && dD < 0);
				
		StringBuffer sb = new StringBuffer();
		//预产期转为整数
		if (bEstimate)
		{
			dY = -dY;
			dM = -dM;
			dD = -dD;
			month = child.mMonth;
		}
		if (dD < 0)
		{
			dM--;
			dD += Const.DAY_IN_MONTH[month - 1];
		}
		if (dM < 0)
		{
			dY--;
			dM += 12;
		}
		//预产期，计算已怀孕时间
		if (bEstimate)
		{
			sb.append("怀孕");
			dY = 0;
			if (dD == 0)
			{
				dM = 10 - dM;
			}
			else
			{
				dM = 9 - dM;
				dD = 30 - dD;
			}
		}

		if (dY > 0)
		{
			sb.append(dY);
			sb.append('岁');
		}
		else if (dM > 0)
		{
			sb.append(dM);
			sb.append("个月");
		}
		else if (dD > 0)
		{
			sb.append(dD);
			sb.append('天');
		}
		else
		{
			sb.append("0天");
		}
		return sb.toString();
	}
	
	abstract protected void enter(int arg1, int arg2, Object obj);
}
