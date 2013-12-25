package com.iof.school.parser;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.school.obj.Child;
import com.iof.school.obj.TimeLimit;
import com.iof.school.utils.Const;
import com.iof.showlib.utils.TaskBase.Parser;

public class ParserChild implements Parser
{
	public Child mChild;
	public int mResult;
	
	public void setInfo(Child child)
	{
		mChild = child;
	}

	@Override
	public String getUrl()
	{
		return Const.URL_CHILD;
	}

	@Override
	public String getParam()
	{
		StringBuffer sb = new StringBuffer();
		if (mChild.mId > 0)
		{
			sb.append("id=" + mChild.mId);
		}
		if (mChild.mName != null)
		{
			if (sb.length() > 0)
			{
				sb.append('&');
			}
			try
			{
				sb.append("name=" + URLEncoder.encode(mChild.mName, "UTF-8"));
			}
			catch (Exception e)
			{
				sb.append("name=");
			}
		}
		if (mChild.mSex > 0)
		{
			if (sb.length() > 0)
			{
				sb.append('&');
			}
			sb.append("sex=" + mChild.mSex);
		}
		if (mChild.mYear > 0)
		{
			if (sb.length() > 0)
			{
				sb.append('&');
			}
			sb.append(String.format("birthday=%d-%02d-%02d", mChild.mYear, mChild.mMonth, mChild.mDay));
		}
		if (mChild.mPsw != null)
		{
			if (sb.length() > 0)
			{
				sb.append('&');
			}
			sb.append("password=" + mChild.mPsw);
		}
		System.out.println("--------param=" + sb.toString());
		return sb.toString();
	}

	@Override
	public void parserJson(String json) throws JSONException
	{
		System.out.println("-----------------------------json=" + json);
		JSONTokener jTokener = new JSONTokener(json);
		JSONObject jObject = (JSONObject) jTokener.nextValue();
		mResult = jObject.getInt("code");
		if (mResult != 0)
		{
			return;
		}
		jObject = jObject.getJSONObject("entity");
		mChild.mId = jObject.getInt("id");
		mChild.mName = jObject.getString("name");
		mChild.mSex = jObject.getInt("sex");
		parserBirthday(mChild, jObject.getString("birthday"));
		mChild.mHasPsw = jObject.getString("hasPassword").equals("yes");
		try
		{
			mChild.mTime = new TimeLimit(jObject.getString("childTime"));
		}
		catch (Exception e)
		{
		}
	}
	
	private void parserBirthday(Child child, String day)
	{
		try
		{
			child.mYear = Integer.parseInt(day.substring(0, 4));
			child.mMonth = Integer.parseInt(day.substring(5, 7));
			child.mDay = Integer.parseInt(day.substring(8));
		}
		catch (Exception e)
		{
		}
	}
}
