package com.iof.school.obj;

public class TimeValue
{
	public static final int HOUR_8 = 8 * 60 * 60 * 1000;
	
	public int mType = 1; //0-无限制；1-小时；2-时间段
	public int mValue1 = 2;
	public int mValue2;
	
	/**
	 * 返回剩余时间：0表示没有剩余
	 * @return
	 */
	public int hasTime()
	{
		int time = (int)((System.currentTimeMillis() + HOUR_8) / 1000 / 60 % (24 * 60)); //当前分钟值
		if (time < mValue1 || time >= mValue2)
		{
			return 0;
		}
		return mValue2 - time;
	}
}
