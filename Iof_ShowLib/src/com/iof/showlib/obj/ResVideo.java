package com.iof.showlib.obj;

import org.json.JSONException;
import org.json.JSONObject;

import com.iof.showlib.obj.Res;

public class ResVideo extends Res
{
	private static final long serialVersionUID = -7002589622530998060L;
	
	public int mDuration;
	public String mUrl;

	@Override
	public void parserJson(JSONObject jObject, boolean isBase) throws JSONException
	{
		super.parserJson(jObject, isBase);
		
		if (!isBase)
		{
			mDuration = jObject.getInt("duration");
			mUrl = jObject.getString("url");
		}
	}
}
