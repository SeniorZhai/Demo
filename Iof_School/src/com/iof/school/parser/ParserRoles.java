package com.iof.school.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.school.obj.Child;
import com.iof.school.obj.TimeLimit;
import com.iof.school.utils.Const;
import com.iof.showlib.utils.TaskBase.Parser;

public class ParserRoles implements Parser
{
	public int mUserId;
	public String mUserName;
	public boolean mHasPsw;
	public ArrayList<Child> mRoles = new ArrayList<Child>();

	@Override
	public String getUrl()
	{
		return Const.URL_ROLES;
	}

	@Override
	public String getParam()
	{
		return null;
	}

	@Override
	public void parserJson(String json) throws JSONException
	{
		System.out.println("-----------------------------json=" + json);
		mRoles.clear();
		
		JSONTokener jTokener = new JSONTokener(json);
		JSONObject jObject = ((JSONObject) jTokener.nextValue()).getJSONObject("entity");
		//mUserId = jObject.getInt("userId");
		//mUserName = jObject.getString("userName");
		mHasPsw = jObject.getString("hasPassword").equals("yes");
		JSONArray jArray = (JSONArray)jObject.getJSONArray("children");
		Child child;
		for (int i = 0; i < jArray.length(); i++)
		{
			jObject = jArray.getJSONObject(i);
			child = new Child();
			child.mId = jObject.getInt("id");
			child.mName = jObject.getString("name");
			child.mSex = jObject.getInt("sex");
			parserBirthday(child, jObject.getString("birthday"));
			child.mHasPsw = jObject.getString("hasPassword").equals("yes");
			child.mTime = new TimeLimit(jObject.getString("childTime"));
			mRoles.add(child);
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
