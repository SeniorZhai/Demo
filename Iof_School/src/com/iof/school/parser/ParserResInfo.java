package com.iof.school.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.school.utils.Const;
import com.iof.showlib.obj.Res;
import com.iof.showlib.utils.TaskBase.Parser;

public class ParserResInfo implements Parser
{
	private Res mRes;
	
	public void setInfo(Res res)
	{
		mRes = res;
	}

	@Override
	public String getUrl()
	{
		return Const.URL_RES_INFO;
	}

	@Override
	public String getParam()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("resourceId=");
		sb.append(mRes.mId);
		return sb.toString();
	}

	@Override
	public void parserJson(String json) throws JSONException
	{
		//System.out.println("-----------------------------json=" + json);
		JSONTokener jTokener = new JSONTokener(json);
		JSONObject jObject = ((JSONObject) jTokener.nextValue()).getJSONObject("entity");
		int id = jObject.getInt("id");
		if (id == mRes.mId)
		{
			mRes.parserJson(jObject, false);
		}
		else
		{
			throw new JSONException("error res id!");
		}
	}
}
