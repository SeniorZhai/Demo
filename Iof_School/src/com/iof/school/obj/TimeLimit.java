package com.iof.school.obj;

import java.util.Calendar;

public class TimeLimit
{
	public static final int LIMIT_MODE_NONE = 0; //无限制
	public static final int LIMIT_MODE_EVERY = 1; //每天
	public static final int LIMIT_MODE_WORK = 2; //平时/周末
	
	public int mMode; //每天、平时/周末
	public TimeValue mTime1;
	public TimeValue mTime2;
	
	public TimeLimit()
	{
	}
	
	public TimeLimit(String limit)
	{
		try
		{
			String[] data = limit.split(";|,");
			mMode = Integer.parseInt(data[0]);
			int index = 1;
			if (mMode == LIMIT_MODE_NONE)
			{
				return;
			}
			else if (mMode == LIMIT_MODE_EVERY)
			{
				mTime1 = new TimeValue();
				getTime(data, index, mTime1);
			}
			else
			{
				mTime1 = new TimeValue();
				mTime2 = new TimeValue();
				index = getTime(data, index + 1, mTime1);
				index = getTime(data, index + 1, mTime2);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	private int getTime(String[] data, int index, TimeValue time)
	{
		time.mType = Integer.parseInt(data[index]);
		if (time.mType == 0)
		{
			return index + 1;
		}
		else if (time.mType == 1)
		{
			time.mValue1 = Integer.parseInt(data[index + 1]);
			return index + 2;
		}
		else
		{
			time.mValue1 = Integer.parseInt(data[index + 1]);
			time.mValue2 = Integer.parseInt(data[index + 2]);
			return index + 3;
		}
	}
	
	public TimeValue getTimeValue()
	{
		if (mMode == LIMIT_MODE_WORK)
		{
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			if (day == Calendar.SUNDAY || day == Calendar.SATURDAY)
			{
				return mTime2;
			}
		}
		
		return mTime1;
	}
	
	public void clone(TimeLimit time)
	{
		mMode = time.mMode;
		if (time.mTime1 == null)
		{
			mTime1 = null;
		}
		else
		{
			if (mTime1 == null)
			{
				mTime1 = new TimeValue();
			}
			mTime1.mType = time.mTime1.mType;
			mTime1.mValue1 = time.mTime1.mValue1;
			mTime1.mValue2 = time.mTime1.mValue2;
		}
		if (time.mTime2 == null)
		{
			mTime2 = null;
		}
		else
		{
			if (mTime2 == null)
			{
				mTime2 = new TimeValue();
			}
			mTime2.mType = time.mTime2.mType;
			mTime2.mValue1 = time.mTime2.mValue1;
			mTime2.mValue2 = time.mTime2.mValue2;
		}
	}
	
	public String getStr()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(mMode);
		if (mMode == LIMIT_MODE_NONE)
		{
			sb.append('0');
		}
		else
		{
			sb.append(';');
			if (mMode == LIMIT_MODE_WORK)
			{
				sb.append("1,");
			}
			if (mTime1 == null)
			{
				sb.append("0");
			}
			else
			{
				sb.append(mTime1.mType);
				sb.append(',');
				if (mTime1.mType == 1)
				{
					sb.append(mTime1.mValue1);
				}
				else
				{
					sb.append(mTime1.mValue1);
					sb.append(',');
					sb.append(mTime1.mValue2);
				}
			}
			if (mMode == LIMIT_MODE_WORK)
			{
				sb.append(";2,");
				if (mTime2 == null)
				{
					sb.append("0");
				}
				else
				{
					sb.append(mTime2.mType);
					sb.append(',');
					if (mTime2.mType == 1)
					{
						sb.append(mTime2.mValue1);
					}
					else
					{
						sb.append(mTime2.mValue1);
						sb.append(',');
						sb.append(mTime2.mValue2);
					}
				}
			}
		}
		return sb.toString();
	}
}
