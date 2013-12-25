package com.iof.school.obj;

public class Child
{
	public int mId;
	public int mSex;
	public int mResId;
	public int mYear;
	public int mMonth;
	public int mDay;
	public boolean mHasPsw;
	public String mName;
	public String mPsw;
	public TimeLimit mTime;
	
	public void clone(Child child)
	{
		mId = child.mId;
		mSex = child.mSex;
		mResId = child.mResId;
		mName = child.mName;
		mPsw = child.mPsw;
		mYear = child.mYear;
		mMonth = child.mMonth;
		mDay = child.mDay;
		mHasPsw = child.mHasPsw;
		//mTime.clone(child.mTime);
	}
}
