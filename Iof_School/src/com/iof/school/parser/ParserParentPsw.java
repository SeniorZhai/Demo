package com.iof.school.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.iof.school.utils.Const;
import com.iof.showlib.utils.TaskBase.Parser;

public class ParserParentPsw implements Parser
{
	public int mResult = 100;
	private String mPswOld, mPswNew;
	
	public void setInfo(String pswOld, String pswNew)
	{
		mPswOld = pswOld;
		mPswNew = pswNew;
	}

	@Override
	public String getUrl()
	{
		return Const.URL_PARENT_PSW;
	}

	@Override
	public String getParam()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("&oldPassword=");
		if (mPswOld != null)
		{
			sb.append(mPswOld);
		}
		sb.append("&newPassword=");
		sb.append(mPswNew);
		return sb.toString();
	}

	@Override
	public void parserJson(String json) throws JSONException
	{
		//System.out.println("-----------------------------json=" + json);
		JSONTokener jTokener = new JSONTokener(json);
		JSONObject jObject = (JSONObject) jTokener.nextValue();
		mResult = jObject.getInt("code");
	}
}
