package com.iof.school.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.school.utils.Const;
import com.iof.showlib.obj.Res;
import com.iof.showlib.utils.TaskBase.Parser;

public class ParserResList implements Parser
{
	public ArrayList<Res> mRes = new ArrayList<Res>();
	public int mPage;
	public int mTotal;
	private int mCategory;
	
	public void setInfo(int cate, int page)
	{
		mCategory = cate;
		mPage = page;
	}
	
	public void nextPage()
	{
		mPage++;
	}

	@Override
	public String getUrl()
	{
		return Const.URL_RES_LIST;
	}

	@Override
	public String getParam()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("size=24&page=");
		sb.append(mPage);
		sb.append("&category=");
		sb.append(mCategory);
		return sb.toString();
	}

	@Override
	public void parserJson(String json) throws JSONException
	{
		//System.out.println("-----------------------------json=" + json);
		mRes.clear();
		JSONTokener jTokener = new JSONTokener(json);
		JSONObject jObject = ((JSONObject) jTokener.nextValue()).getJSONObject("page");
		mTotal = jObject.getInt("totalElements");
		JSONArray jArray = (JSONArray)jObject.getJSONArray("content");
		Res res;
		for (int i = 0; i < jArray.length(); i++)
		{
			jObject = jArray.getJSONObject(i);
			res = Res.getInstance(jObject.getInt("type"));
			res.parserJson(jObject, true);
			mRes.add(res);
		}
	}
}
