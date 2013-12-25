package com.iof.school.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.school.utils.Const;
import com.iof.showlib.obj.Category;
import com.iof.showlib.utils.TaskBase.Parser;

public class ParserCategory implements Parser
{
	public ArrayList<Category> mCates = new ArrayList<Category>();

	@Override
	public String getUrl()
	{
		return Const.URL_CATEGORY;
	}

	@Override
	public String getParam()
	{
		return null;
	}

	@Override
	public void parserJson(String json) throws JSONException
	{
		//System.out.println("-----------------------------json=" + json);
		JSONTokener jTokener = new JSONTokener(json);
		JSONObject jObject = (JSONObject)jTokener.nextValue();
		JSONArray jArray = (JSONArray)jObject.getJSONArray("categories");
		Category cate;
		for (int i = 0; i < jArray.length(); i++)
		{
			jObject = jArray.getJSONObject(i);
			cate = new Category();
			cate.mId = jObject.getInt("id");
			cate.mName = jObject.getString("name");
			mCates.add(cate);
		}
	}
}
