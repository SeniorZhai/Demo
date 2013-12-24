package com.iof.school.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.school.obj.Child;
import com.iof.school.obj.TimeLimit;
import com.iof.school.utils.Const;
import com.iof.showlib.utils.TaskBase.Parser;

public class ParserLimitTime implements Parser
{
	public int mResult = 100;
	public Child mChild;
	public TimeLimit mLimit;
	
	public void setInfo(Child child, TimeLimit limit)
	{
		mChild = child;
		mLimit = limit;
	}

	@Override
	public String getUrl()
	{
		return Const.URL_SET_TIME_LIMIT;
	}

	@Override
	public String getParam()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("childId=" + mChild.mId);
		sb.append("&param=" + mLimit.getStr());
		return sb.toString();
	}

	@Override
	public void parserJson(String json) throws JSONException
	{
		//System.out.println("-----------------------------json=" + json);
		JSONTokener jTokener = new JSONTokener(json);
		JSONObject jObject = (JSONObject) jTokener.nextValue();
		mResult = jObject.getInt("code");
		if (mResult == 0)
		{
			mChild.mTime.clone(mLimit);
		}
	}
}
